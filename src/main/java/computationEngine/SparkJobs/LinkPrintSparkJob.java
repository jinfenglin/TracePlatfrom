package computationEngine.SparkJobs;

import computationEngine.Link;
import computationEngine.SparkJobs.ISparkJob.LinkRDDConsumer;
import computationEngine.SparkJobs.ISparkJob.LinkRDDProvider;
import computationEngine.SparkJobs.ISparkJob.SparkJob;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

/**
 * Print the links which are generated by another sparkjob to the console. This class is primarily for test usage
 * <p>
 * TODO modify the constructor to accept <RDD,SparkSession>. Each SparkJob should be capable to close the session it used.
 */
public class LinkPrintSparkJob implements LinkRDDConsumer {
    JavaRDD<Link> linkJavaRDD;
    SparkSession session;

    public LinkPrintSparkJob(LinkRDDProvider provider) {
        this.linkJavaRDD = provider.getLinkRDD();
        this.session = provider.getSession();
    }


    @Override
    public LinkPrintSparkJob call() throws Exception {
        linkJavaRDD.collect().forEach(x -> System.out.println(x.toString()));
        return this;
    }

    @Override
    public SparkSession getSession() {
        return session;
    }

    @Override
    public void closeSession() {
        session.close();
    }
}
