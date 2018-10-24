package computationEngine.SparkJobs;

import computationEngine.Model.TraceModel;
import messageDeliver.DebeziumEvent;
import org.springframework.stereotype.Component;
import spring.services.TIMGraph;

import java.sql.Timestamp;
import java.util.StringJoiner;

@Component
public class SparkJobMaker {

    public SparkJobMaker(TIMGraph timGraph) {
    }

    public LinkGenerationSparkJob createLinkGenJobFromEvent(DebeziumEvent event, String sparkUrl, TraceModel model) {
        String jobID = createJobID(event, model);
        //TODO fetch the related artifacts from database and add them to source artifacts and target artifacts in spark job
        //TODO create different job based on the operation code (maybe not)
        //Will test this function with the dummy TIM
        switch (event.getOpCode()) {
            case "c"://create
                break;
            case "d"://delete
                break;
            case "u"://udpate
                break;
        }
        LinkGenerationSparkJob job = new DummySparkJob(jobID, sparkUrl, model); //now is empty job
        return job;
    }

    public static String createJobID(DebeziumEvent event, TraceModel model) {
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        StringJoiner sj = new StringJoiner("_");
        sj.add(event.getOpCode());
        sj.add(event.getAfterAsDbSchema().getSchemaID());
        sj.add(model.getModelType().toString());
        return sj.toString();
    }
}
