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
            Logger.getLogger(this.getClass().getName()).info("Dummy policy is running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
