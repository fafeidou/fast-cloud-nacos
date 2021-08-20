package fast.cloud.nacos.warmup;


public abstract class AbstractWarmUpComponent implements WarmUpComponent {
    public AbstractWarmUpComponent() {
    }

    protected void doWarmUp(String resourceType, String resourceName, WarmUpAction warmUpAction, int warmUpTimes) throws Exception {
        for(int i = 0; i < warmUpTimes; ++i) {
//            Transaction transaction = Cat.newTransaction("WarmpUp." + resourceType, resourceName);

            try {
                warmUpAction.run();
//                transaction.setSuccessStatus();
            } catch (Throwable var11) {
//                transaction.setStatus(var11);
                throw var11;
            } finally {
//                transaction.complete();
            }
        }

    }

    public interface WarmUpAction {
        void run() throws Exception;
    }
}