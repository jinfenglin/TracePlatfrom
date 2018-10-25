package computationEngine.SparkJobs.ISparkJob;

import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface SparkJob extends Callable<SparkJob>, Serializable {
    /**
     * Before a spark job is running, this method should be called to get an effective
     * SparkSession
     *
     * @return
     */
    SparkSession getSession();

    /**
     * This method should be called when a sparkJob is finished and the RDD produced by it will not be used by any other
     * SparkJob
     */
    void closeSession();

}
