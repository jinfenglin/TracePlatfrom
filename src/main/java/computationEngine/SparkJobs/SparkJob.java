package computationEngine.SparkJobs;

import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface SparkJob extends Callable<SparkJob>, Serializable {
    SparkSession getSession();

    void closeSession();

}
