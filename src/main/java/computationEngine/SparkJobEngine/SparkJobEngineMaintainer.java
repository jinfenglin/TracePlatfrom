package computationEngine.SparkJobEngine;

import computationEngine.SparkJobs.SparkJob;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SparkJobEngineMaintainer implements Runnable {
    boolean exitFlag;
    private Map<SparkJob, Future<SparkJob>> runningJobs;
    private BlockingQueue<SparkJob> waitingList;
    private int MAX_WORK_NUM;
    private CompletionService<SparkJob> threadPoolTaskExecutor;

    public SparkJobEngineMaintainer(Map<SparkJob, Future<SparkJob>> runningJobs,
                                    BlockingQueue<SparkJob> waitingList,
                                    CompletionService<SparkJob> threadPoolTaskExecutor,
                                    int MAX_WORK_NUM
    ) {
        this.runningJobs = runningJobs;
        this.waitingList = waitingList;
        this.MAX_WORK_NUM = MAX_WORK_NUM;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        exitFlag = true;
    }

    public void stop() {
        exitFlag = true;
    }

    @Override
    public void run() {
        exitFlag = false;
        while (!exitFlag) {
            if (runningJobs.size() < MAX_WORK_NUM) {
                //Submit jobs when free slot available
                SparkJob job = null;
                try {
                    job = waitingList.take();
                    Future<SparkJob> jobFuture = threadPoolTaskExecutor.submit(job);
                    runningJobs.put(job, jobFuture);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Wait for the executor to finish the jobs and update runningJob list.
     */
    class CleanerWorker implements Runnable {
        public CleanerWorker() {

        }

        @Override
        public void run() {
            while (true) {
                Future<SparkJob> doneFuture = threadPoolTaskExecutor.poll();
                try {
                    SparkJob doneJob = doneFuture.get();
                    runningJobs.remove(doneJob);
                    doneJob.closeSession();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}