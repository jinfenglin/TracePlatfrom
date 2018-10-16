package computationEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class SparkJobEngine {
    @Autowired
    public TaskExecutor threadPoolTaskExecutor;

    public SparkJobEngine()  {

    }

    public void submit(SparkJob job){
        threadPoolTaskExecutor.execute(job);
    }


}
