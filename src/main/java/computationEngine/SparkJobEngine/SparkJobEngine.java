package computationEngine.SparkJobEngine;

import computationEngine.SparkJobs.ISparkJob.SparkJob;
import computationEngine.SparkJobs.SparkJobMaker;
import org.spark_project.jetty.util.BlockingArrayQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * An automated job management system which schedule the spark jobs.
 */
@Service
@PropertySource({"classpath:platform.properties"})
public class SparkJobEngine {
    @Autowired
    private CompletionService<SparkJob> threadPoolTaskExecutor;
    @Autowired
    private SparkJobMaker sparkJobMaker;
    @Autowired
    private Environment environment;

    private Map<SparkJob, Future<SparkJob>> runningJobs;
    private BlockingQueue<SparkJob> waitingList;

    private Thread maintainerThread;
    private SparkJobEngineMaintainer maintainer;

    public SparkJobEngine() {
        runningJobs = Collections.synchronizedMap(new HashMap<SparkJob, Future<SparkJob>>());
        waitingList = new BlockingArrayQueue<>();
    }

    /**
     * Skip the task scheduling and submit a job to executor directly
     */
    public Future forceSubmit(SparkJob job) {
        return threadPoolTaskExecutor.submit(job);
    }

    public void submit(SparkJob job) {
        waitingList.add(job);
    }

    public SparkJobMaker getSparkJobMaker() {
        return sparkJobMaker;
    }

    public Map<SparkJob, Future<SparkJob>> getRunningJobs() {
        return runningJobs;
    }

    public BlockingQueue<SparkJob> getWaitingList() {
        return waitingList;
    }

    /**
     * Start the spark job engine to submit
     */
    public void start() {
        int MAX_WORK_NUM = Integer.valueOf(environment.getProperty("platform.executorService.maxThread"));
        maintainer = new SparkJobEngineMaintainer(runningJobs, waitingList, threadPoolTaskExecutor, MAX_WORK_NUM);
        maintainerThread = new Thread(maintainer);
        maintainerThread.start();
    }

    public void stop() {
        maintainer.stop();
    }

}


