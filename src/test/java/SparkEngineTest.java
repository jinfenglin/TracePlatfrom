import computationEngine.SparkJobs.DummySparkJob;
import computationEngine.Model.TraceModel;
import computationEngine.Model.TraceModelManger;
import computationEngine.Model.TraceModelType;
import computationEngine.SparkJobs.LinkPrintSparkJob;
import computationEngine.SparkJobs.LinkGenerationSparkJob;
import computationEngine.SparkJobEngine.SparkJobEngine;
import computationEngine.SparkJobs.ISparkJob.SparkJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.config.SpringRootConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRootConfig.class})
public class SparkEngineTest {
    @Autowired
    private SparkJobEngine sparkJobEngine;

    @Autowired
    private TraceModelManger traceModelManger;

    @Autowired
    private Environment environment;

    @Test
    public void TestCreateAndExecuteSparkJob() throws InterruptedException, ExecutionException {
        TraceModel model = traceModelManger.getModel(TraceModelType.DUMMY);
        String sparkUrl = environment.getProperty("spark.master");
        LinkGenerationSparkJob exampleJob = new DummySparkJob("exmapleJob", sparkUrl, model);
        LinkPrintSparkJob printJob = new LinkPrintSparkJob(exampleJob);
        sparkJobEngine.submit(printJob);
        Future<SparkJob> jobFuture = null;
        while (true) {
            if (sparkJobEngine.getRunningJobs().containsKey(printJob)) {
                jobFuture = sparkJobEngine.getRunningJobs().get(printJob);
                break;
            }
        }
        jobFuture.get();
    }
}
