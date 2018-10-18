package computationEngine.Model;

import computationEngine.Model.ModelUpdatePolicy.TModelUpdatePolicy;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage the life cycle of models. The responsible of manger is
 * 1. Providing models for computation by model name
 * 2. Update the model according to the given update policy.
 * <p>
 * One TraceModelManger can have only one policy right now, it is considered to give each model an individual updating
 * policy in the future
 */
public class TraceModelManger {
    private Map<TraceModelType, TraceModel> modelMap;

    private Thread modelMonitorThread;
    private TModelUpdatePolicy updatePolicy;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public TraceModelManger(TModelUpdatePolicy updatePolicy) {
        this.updatePolicy = updatePolicy;
        modelMonitorThread = new Thread(updatePolicy);

        modelMap = new HashMap<>();
        DummyTraceModel dummyTraceModel = new DummyTraceModel();
        modelMap.put(dummyTraceModel.getModelType(), dummyTraceModel);
    }

    public void startModelMonitor() {
        logger.info("start model updating thread...");
        modelMonitorThread.start();
    }

    public void stopModelMonitor() {
        logger.info("stop model updating thread...");
        updatePolicy.stop();
    }

    public TraceModel getModel(TraceModelType modelType) {
        return modelMap.get(modelType);
    }
}
