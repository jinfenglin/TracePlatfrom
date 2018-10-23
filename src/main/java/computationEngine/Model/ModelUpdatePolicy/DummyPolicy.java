package computationEngine.Model.ModelUpdatePolicy;


import org.apache.log4j.Logger;

/**
 * A dummy model update policy for test
 */
public class DummyPolicy extends TModelUpdatePolicy {
    @Override
    public void run() {
        exitFlag = false;
        while (!exitFlag) {
            try {
                Logger.getLogger(this.getClass().getName()).debug("Dummy policy is running");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
