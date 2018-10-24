package computationEngine.SparkJobs;

import computationEngine.Artifact;
import computationEngine.Model.TraceModel;

import java.util.ArrayList;
import java.util.List;

public class DummySparkJob extends LinkGenerationSparkJob {
    public DummySparkJob(String jobID, String sparkMasterUrl, TraceModel model) {
        super(jobID, sparkMasterUrl, model);
        List<Artifact> fromArtifacts = new ArrayList<>();
        List<Artifact> toArtifacts = new ArrayList<>();
        Artifact f1 = new Artifact("f1");
        Artifact f2 = new Artifact("f2");
        Artifact f3 = new Artifact("f3");

        Artifact t1 = new Artifact("t1");
        Artifact t2 = new Artifact("t2");
        Artifact t3 = new Artifact("t3");

        fromArtifacts.add(f1);
        fromArtifacts.add(f2);
        fromArtifacts.add(f3);

        toArtifacts.add(t1);
        toArtifacts.add(t2);
        toArtifacts.add(t3);
        setFromArtifacts(fromArtifacts);
        setToArtifacts(toArtifacts);
    }
}
