package computationEngine.Model;

import computationEngine.Artifact;

import java.nio.file.Path;
import java.util.Map;

public interface TraceModel {
    double getScore(Artifact fromArtifact, Artifact toArtifact);

    /**
     * Implement to train the model.
     * @param dataset
     */
    void trainModel(DataSet dataset);

    /**
     * Change the configuration of the model
     *
     * @param config
     */
    void updateConfiguration(Map<String, Object> config);

    /**
     * Save the model configurations
     *
     * @param path
     */
    void readModel(Path path);

    /**
     * Restore the model from a serialized object
     *
     * @param path
     */
    void writeModel(Path path);

    TraceModelType getModelType();
}
