package fast.cloud.nacos.cat.monitor.enhance.redis;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class JedisInstrumenter implements Instrumenter {

    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {
        return agentBuilder.type(
            named("redis.clients.jedis.Jedis")
                .or(named("redis.clients.jedis.BinaryJedis"))
        ).transform(new Transformer());
    }

    private static class Transformer implements AgentBuilder.Transformer {
        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                TypeDescription typeDescription,
                                                ClassLoader classLoader,
                                                JavaModule module) {
            return builder.visit(Advice.to(Interceptor.class).on(
                RedisMethodMatch.INSTANCE.getJedisMethodMatcher()
            ));
        }
    }

    public static class Interceptor {

        @Advice.OnMethodEnter(inline = false)
        public static Transaction intercept(@Advice.Origin Method method,
                                            @Advice.AllArguments Object[] allArguments,
                                            @Advice.This Object obj) throws Exception {

            Transaction transaction = Cat.newTransaction("Redis", "Jedis/" + method.getName());
            try {
                if (allArguments == null) {
                    return transaction;
                }
                transaction.addData("key", getKey(allArguments[0]));
            } catch (Exception e) {
                Cat.logError(e);
            }
            return transaction;
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class)
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
            } catch (Exception e) {
                Cat.logError(e);
            } finally {
                transaction.complete();
            }
        }

        private static String getKey(Object data) {
            String ret = "";
            if (data instanceof String) {
                ret = (String) data;
            }
            if (data instanceof byte[]) {
                ret = new String((byte[]) data);
            }
            if (ret.length() > 1000) {
                ret = ret.substring(0, 1000);
            }
            return ret;
        }
    }

}
