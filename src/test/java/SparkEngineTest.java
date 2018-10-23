import computationEngine.DummySparkJob;
import computationEngine.Model.TraceModel;
import computationEngine.Model.TraceModelManger;
import computationEngine.Model.TraceModelType;
import computationEngine.SparkJob;
import computationEngine.SparkJobEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.config.SpringRootConfig;

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
    public void TestCreateAndExecuteSparkJob() {
        TraceModel model = traceModelManger.getModel(TraceModelType.DUMMY);
        String sparkUrl = environment.getProperty("spark.master");
        SparkJob exampleJob = new DummySparkJob("exmapleJob", sparkUrl, model);
        sparkJobEngine.submit(exampleJob);
    }
}
