package fast.cloud.nacos.cat.monitor.enhance.redis;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import io.netty.channel.Channel;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;
import org.redisson.client.RedisConnection;
import org.redisson.client.protocol.CommandData;
import org.redisson.client.protocol.CommandsData;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class RedissonInstrumenter implements Instrumenter {
    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {


        return agentBuilder.type(
            named("org.redisson.client.RedisConnection")
        ).transform(new Transformer());
    }


    private static class Transformer implements AgentBuilder.Transformer {


        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                TypeDescription typeDescription,
                                                ClassLoader classLoader,
                                                JavaModule module) {

            return builder.visit(Advice.to(Interceptor.class).on(
                named("send")
                    .and(takesArguments(1))
                    .and(
                        takesArgument(0, named("org.redisson.client.protocol.CommandData"))
                            .or(takesArgument(0, named("org.redisson.client.protocol.CommandsData")))
                    )
            ));


        }
    }

    public static class Interceptor {

        @Advice.OnMethodEnter(inline = true)
        public static Transaction enter(@Advice.Origin Method method,
                                        @Advice.AllArguments Object[] allArguments,
                                        @Advice.This Object obj){
            RedisConnection connection = (RedisConnection) obj;
            Channel channel = connection.getChannel();
            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
            String dbInstance = remoteAddress.getAddress().getHostAddress() + ":" + remoteAddress.getPort();

            StringBuilder dbStatement = new StringBuilder();
            String operationName = "Redisson/";
            if (allArguments[0] instanceof CommandData) {
                CommandData commandData = (CommandData) allArguments[0];
                String command = commandData.getCommand().getName();
                operationName = operationName + command;
                addCommandData(dbStatement, commandData);
            } else if (allArguments[0] instanceof CommandsData) {
                operationName = operationName + "BATCH_EXECUTE";
                CommandsData commands = (CommandsData) allArguments[0];
                for (CommandData commandData : commands.getCommands()) {
                    addCommandData(dbStatement, commandData);
                    dbStatement.append(";");
                }
            }

            Transaction transaction = Cat.newTransaction("Redis", operationName);
            transaction.addData("command", dbStatement.toString());
            Cat.logEvent("Redis.instance", dbInstance);
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

        public static void addCommandData(StringBuilder dbStatement, CommandData commandData) {
            dbStatement.append(commandData.getCommand().getName());
            if (commandData.getParams() != null) {
                for (Object param : commandData.getParams()) {
                    dbStatement.append(" ");
                    if (param instanceof Byte) {
                        dbStatement.append("?");
                    } else if (param instanceof byte[]) {
                        dbStatement.append(new String((byte[]) param));
                    } else {
                        dbStatement.append(param.toString());
                    }
                }
            }
        }

    }
}
