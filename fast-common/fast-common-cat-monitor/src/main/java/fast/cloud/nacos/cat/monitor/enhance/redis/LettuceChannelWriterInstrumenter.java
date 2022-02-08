package fast.cloud.nacos.cat.monitor.enhance.redis;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import io.lettuce.core.protocol.DefaultEndpoint;
import io.lettuce.core.protocol.RedisCommand;
import io.netty.channel.Channel;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class LettuceChannelWriterInstrumenter implements Instrumenter {

    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {

        return agentBuilder.type(
            named("io.lettuce.core.protocol.DefaultEndpoint")
        ).transform(new Transformer());
    }


    private static class Transformer implements AgentBuilder.Transformer {


        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                TypeDescription typeDescription,
                                                ClassLoader classLoader,
                                                JavaModule module) {

            return builder.visit(Advice.to(Interceptor.class).on(
                named("writeToChannelAndFlush")
                    .or(named("writeAndFlush"))
            ));


        }
    }

    public static class Interceptor {

        @Advice.OnMethodEnter
        public static Transaction enter(@Advice.Origin Method method,
                                        @Advice.This Object obj,
                                        @Advice.AllArguments Object[] allArguments) throws Exception {

            String instance = "";
            try {
                Field field = DefaultEndpoint.class.getDeclaredField("channel");
                field.setAccessible(true);
                Object channel = field.get(obj);
                SocketAddress socketAddress = ((Channel) channel).remoteAddress();
                InetSocketAddress remoteAddress = (InetSocketAddress) socketAddress;
                instance = remoteAddress.getAddress().getHostAddress() + ":" + remoteAddress.getPort();
            } catch (Exception e) {
                Cat.logError(e);
            }

            StringBuilder dbStatement = new StringBuilder();
            String operationName = "Lettuce/";
            if (allArguments[0] instanceof RedisCommand) {
                RedisCommand redisCommand = (RedisCommand) allArguments[0];
                String command = redisCommand.getType().name();
                operationName = operationName + command;
                dbStatement.append(command);
                if (redisCommand.getArgs() != null) {
                    dbStatement.append(" ").append(redisCommand.getArgs().toCommandString());
                }
            } else if (allArguments[0] instanceof Collection) {
                @SuppressWarnings("unchecked") Collection<RedisCommand> redisCommands = (Collection<RedisCommand>) allArguments[0];
                operationName = operationName + "BATCH_WRITE";
                for (RedisCommand redisCommand : redisCommands) {
                    dbStatement.append(redisCommand.getType().name());
                    if (redisCommand.getArgs() != null) {
                        dbStatement.append(" ").append(redisCommand.getArgs().toCommandString()).append(";");
                    }
                }
            }

            Transaction transaction = Cat.newTransaction("Redis", operationName);
            transaction.addData("command", dbStatement.toString());
            Cat.logEvent("Redis.instance", instance);
            return transaction;
        }


        @Advice.OnMethodExit(onThrowable = Throwable.class, inline = true)
        public static void exit(@Advice.Enter Transaction transaction,
                                @Advice.Origin Method method,
                                @Advice.Thrown Throwable throwable) {
            try {
                if (throwable != null) {
                    Cat.logError(throwable);
                    transaction.setStatus(throwable);
                } else {
                    transaction.setStatus(Transaction.SUCCESS);
                }
            } finally {
                transaction.complete();
            }
        }

    }
}