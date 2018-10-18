package computationEngine.Model;

import computationEngine.Artifact;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Map;

public class DummyTraceModel implements TraceModel, Serializable {
    @Override
    public double getScore(Artifact fromArtifact, Artifact toArtifact) {
        return 1;
    }

    @Override
    public void trainModel(DataSet dataset) {
    }

    @Override
    public void updateConfiguration(Map<String, Object> config) {

    }

    @Override
    public void readModel(Path path) {

    }

    @Override
    public void writeModel(Path path) {

    }

    @Override
    public TraceModelType getModelType() {
        return TraceModelType.DUMMY;
    }
}
