package fast.cloud.nacos.cat.monitor.common;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

import java.util.Map;
import java.util.function.Supplier;

public class CatTransactionManager {
    public CatTransactionManager() {
    }

    public static <T> T newTransaction(Supplier<T> function, String type, String name, Map<String, T> data) {
        if (!Cat.isInitialized()) {
            return function.get();
        } else {
            Transaction transaction = Cat.newTransaction(type, name);
            if (data != null && !data.isEmpty()) {
                data.forEach(transaction::addData);
            }

            T var6;
            try {
                T result = function.get();
                transaction.setStatus("0");
                var6 = result;
            } catch (Exception var10) {
                Cat.logError(var10);
                if (var10.getMessage() != null) {
                    Cat.logEvent(type + "_" + name + "_Error", var10.getMessage());
                }

                transaction.setStatus(var10);
                throw var10;
            } finally {
                transaction.complete();
            }

            return var6;
        }
    }

    public static <T> T newTransaction(Supplier<T> function, String type, String name) {
        return newTransaction(function, type, name, null);
    }

    public static void newTransaction(Runnable runnable, String type, String name, Map<String, Object> data) {
        if (Cat.isInitialized()) {
            Transaction transaction = Cat.newTransaction(type, name);
            if (data != null && !data.isEmpty()) {
                data.forEach(transaction::addData);
            }

            try {
                runnable.run();
                transaction.setStatus("0");
            } catch (Exception var9) {
                Cat.logError(var9);
                if (var9.getMessage() != null) {
                    Cat.logEvent(type + "_" + name + "_Error", var9.getMessage());
                }

                transaction.setStatus(var9);
                throw var9;
            } finally {
                transaction.complete();
            }
        } else {
            runnable.run();
        }

    }

    public static void newTransaction(Runnable runnable, String type, String name) {
        newTransaction(runnable, type, name, null);
    }
}