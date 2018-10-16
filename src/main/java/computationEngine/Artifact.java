package computationEngine;

import computationEngine.Model.DataEntry;

import java.io.Serializable;

public class Artifact implements Serializable, DataEntry{
    private String artifactID;

    public Artifact(String artifactID) {
        this.artifactID = artifactID;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "artifactID='" + artifactID + '\'' +
                '}';
    }
}
