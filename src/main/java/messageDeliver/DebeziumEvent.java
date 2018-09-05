package messageDeliver;

import messageDeliver.dbSchema.DbSchema;
import messageDeliver.dbSchema.DbSchemaFactory;

/**
 * Convert a Kafka consumer record to an event data structure..
 */
public class DebeziumEvent {
    private String before, after;
    private String opCode;
    private String dbName;
    private String tableName;
    private String kafkaBrokerId;
    private String connectorName;

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKafkaBrokerId() {
        return kafkaBrokerId;
    }

    public void setKafkaBrokerId(String kafkaBrokerId) {
        this.kafkaBrokerId = kafkaBrokerId;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
    }

    public DbSchema getBeforeAsDbSchema() {
        return DbSchemaFactory.getFactory().getDbSchema(getDbName(), getTableName(), getBefore());
    }

    public DbSchema getAfterAsDbSchema() {
        return DbSchemaFactory.getFactory().getDbSchema(getDbName(), getTableName(), getAfter());
    }

    @Override
    public String toString() {
        return "DebeziumEvent{" +

                "before='" + before + '\'' +
                ", after='" + after + '\'' +
                ", opCode='" + opCode + '\'' +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", kafkaBrokerId='" + kafkaBrokerId + '\'' +
                '}';
    }
}
