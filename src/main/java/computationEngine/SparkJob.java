package computationEngine;

import computationEngine.Model.TraceModel;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;
import scala.Serializable;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;


public class SparkJob implements Runnable, Serializable {
    private String jobID;
    private String sparkMasterUrl;
    private TraceModel model;
    private List<Artifact> fromArtifacts;
    private List<Artifact> toArtifacts;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public SparkJob(String jobID, String sparkMasterUrl, TraceModel model) {
        this.sparkMasterUrl = sparkMasterUrl;
        this.jobID = jobID;
        this.model = model;
        setFromArtifacts(new ArrayList<>());
        setToArtifacts(new ArrayList<>());
    }

    private SparkSession getSparkSession(String jobID, String masterUrl) {
        SparkConf conf = new SparkConf();
        conf.setMaster(masterUrl);
        conf.setAppName(jobID);
        SparkSession sparkSession = SparkSession.builder().config(conf).getOrCreate();
        System.out.println("Session created");
        return sparkSession;
    }

    /**
     * Transfer a JavaRDD object into JavaPaireRDD by setting a dummy key. This is used for join
     * 2 JavaRDD with Cartesian product.
     *
     * @param rdd
     * @param <T>
     * @return
     */
    private <T> JavaPairRDD<Integer, T> transferRDDToPairedRdd(JavaRDD<T> rdd) {
        JavaPairRDD<Integer, T> pairRDD = rdd.mapToPair(new PairFunction<T, Integer, T>() {
            @Override
            public Tuple2<Integer, T> call(T obj) throws Exception {
                return new Tuple2(1, obj);
            }
        });
        return pairRDD;
    }

    private JavaRDD<Link> genCandidateTraceLinks(JavaRDD<Artifact> fromArtifacts, JavaRDD<Artifact> toArtifacts) {
        JavaPairRDD<Integer, Artifact> pairedFromArtifacts = transferRDDToPairedRdd(fromArtifacts);
        JavaPairRDD<Integer, Artifact> pairedToArtifacts = transferRDDToPairedRdd(toArtifacts);
        JavaPairRDD<Integer, Tuple2<Artifact, Artifact>> joinedRDD = pairedFromArtifacts.join(pairedToArtifacts);
        JavaRDD<Link> linkRDD = joinedRDD.map(
                artifactKeyValuePair -> {
                    Artifact fromArtifact = artifactKeyValuePair._2()._1();
                    Artifact toArtifact = artifactKeyValuePair._2()._2();
                    double score = this.model.getScore(fromArtifact, toArtifact);
                    return new Link(fromArtifact, toArtifact, score);
                }
        );
        return linkRDD;
    }


    @Override
    public void run() {
        SparkSession sparkSession = getSparkSession(jobID, sparkMasterUrl);
        Artifact f1 = new Artifact("f1");
        Artifact f2 = new Artifact("f2");
        Artifact f3 = new Artifact("f3");

        Artifact t1 = new Artifact("t1");
        Artifact t2 = new Artifact("t2");
        Artifact t3 = new Artifact("t3");
        ;
        fromArtifacts.add(f1);
        fromArtifacts.add(f2);
        fromArtifacts.add(f3);

        toArtifacts.add(t1);
        toArtifacts.add(t2);
        toArtifacts.add(t3);

        JavaRDD<Artifact> fromArtifacts = JavaSparkContext.fromSparkContext(sparkSession.sparkContext()).parallelize(getFromArtifacts());
        JavaRDD<Artifact> toArtifacts = JavaSparkContext.fromSparkContext(sparkSession.sparkContext()).parallelize(getToArtifacts());


        JavaRDD<Link> linkRDD = genCandidateTraceLinks(fromArtifacts, toArtifacts);
        linkRDD.collect().forEach(x -> logger.debug(x.toString()));
        sparkSession.stop();
    }

    public TraceModel getModel() {
        return model;
    }

    public void setModel(TraceModel model) {
        this.model = model;
    }

    public List<Artifact> getFromArtifacts() {
        return fromArtifacts;
    }

    public void setFromArtifacts(List<Artifact> fromArtifacts) {
        this.fromArtifacts = fromArtifacts;
    }

    public List<Artifact> getToArtifacts() {
        return toArtifacts;
    }

    public void setToArtifacts(List<Artifact> toArtifacts) {
        this.toArtifacts = toArtifacts;
    }
}
