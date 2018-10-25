package computationEngine.SparkJobs.ISparkJob;

import computationEngine.Link;
import org.apache.spark.api.java.JavaRDD;

/**
 * Interface for sparkJobs which will produce JavaRDD that contains links for later jobs to consume.
 */
public interface LinkRDDProvider extends SparkJob{
    JavaRDD<Link> getLinkRDD();
}
