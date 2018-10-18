package messageDeliver.dbSchema;

/**
 * A data structure that used to convert a json object into java object. This interface is used by DbschemaFactory to
 * register the schemas by ID
 */
public interface DbSchema {
    String getSchemaID();

    /**
     * provide the text content that should be applied to TraceModel in later procedures
     *
     * @return
     */
    String getTraceInfo();

}
