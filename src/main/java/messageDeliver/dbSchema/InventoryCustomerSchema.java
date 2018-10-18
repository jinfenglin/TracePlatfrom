package messageDeliver.dbSchema;

/**
 * An example of concrete Dbschema. The DbSchemas should have getter and setter cause the content of
 * the object is fulfilled by Gson and should not be created manually.
 */
public class InventoryCustomerSchema implements DbSchema {
    private int id;
    private String first_name, last_name, email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getSchemaID() {
        return "inventory.customers";
    }

    @Override
    public String getTraceInfo() {
        return toString();
    }

    @Override
    public String toString() {
        return "InventoryCustomerSchema{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
