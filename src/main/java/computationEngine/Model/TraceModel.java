package computationEngine.Model;

import computationEngine.Artifact;

import java.nio.file.Path;
import java.util.Map;

public interface TraceModel {
    double getScore(Artifact fromArtifact, Artifact toArtifact);

    void trainModel(Dataset dataset);

    void updateConfiguration(Map<String, Object> config);

    void readModel(Path path);

    void writeModel(Path path);

    String getModelName();
}
