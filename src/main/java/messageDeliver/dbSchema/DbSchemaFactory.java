package messageDeliver.dbSchema;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class DbSchemaFactory {
    private Map<String, Class<? extends DbSchema>> schemaIdToType; // Map scheam ID to a
    Gson gson;
    private static DbSchemaFactory factory;

    private DbSchemaFactory() {
        schemaIdToType = new HashMap<>();
        gson = new Gson();

        //TODO register Schemas here
        registerSchema(new InventoryCustomerSchema().getSchemaID(), InventoryCustomerSchema.class);
    }

    public static DbSchemaFactory getFactory() {
        if (factory == null) {
            factory = new DbSchemaFactory();
        }
        return factory;
    }

    public void registerSchema(String schemaID, Class schemaType) {
        schemaIdToType.put(schemaID, schemaType);
    }

    /**
     * Create a schema ID from dbName and tableName. The format of schema ID is "{dbName}.{tableName}".
     * All letters are in lower case
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public String createSchemaId(String dbName, String tableName) {
        return String.join(".", dbName, tableName).toLowerCase();
    }

    /**
     * Get the DbSchema class by given dbName and tableName
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public DbSchema getDbSchema(String dbName, String tableName, String json) {
        Class<? extends DbSchema> schemaType = schemaIdToType.get(createSchemaId(dbName, tableName));
        return gson.fromJson(json, schemaType);
    }

}
