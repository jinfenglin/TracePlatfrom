package computationEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class SparkJobEngine {
    @Autowired
    private TaskExecutor threadPoolTaskExecutor;
    @Autowired
    private SparkJobMaker sparkJobMaker;

    public SparkJobEngine() {

    }

    public void submit(SparkJob job) {
        threadPoolTaskExecutor.execute(job);
    }

    public SparkJobMaker getSparkJobMaker() {
        return sparkJobMaker;
    }
}
