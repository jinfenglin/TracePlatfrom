package computationEngine.Model;


import java.util.List;

public class DataSet {
    List<? extends DataEntry> dataEntries;
    public DataSet(List<? extends DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

}
