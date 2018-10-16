package messageDeliver.dbSchema;

/**
 * A data structure that used to convert a json object into java object. This interface is used by DbschemaFactory to
 * register the schemas by ID
 */
public interface DbSchema {
    abstract String getSchemaID();

}
