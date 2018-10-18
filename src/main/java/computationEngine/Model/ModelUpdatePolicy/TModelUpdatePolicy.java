package computationEngine.Model.ModelUpdatePolicy;

/**
 * The run methods must be placed in a for loop and the terminate conditions should check the exitFlag.
 */
public abstract class TModelUpdatePolicy implements Runnable {
    protected volatile boolean exitFlag = false;

    public void stop() {
        exitFlag = true;
    }
}
