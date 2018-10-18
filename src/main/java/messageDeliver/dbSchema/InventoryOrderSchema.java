package messageDeliver.dbSchema;

/**
 * An example of concrete schema
 */
public class InventoryOrderSchema implements DbSchema {

    private String order_number, order_date, purchaser, quantity;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getSchemaID() {
        return "inventory.orders";
    }

    @Override
    public String getTraceInfo() {
        return toString();
    }

    @Override
    public String toString() {
        return "InventoryOrderSchema{" +
                "order_number='" + order_number + '\'' +
                ", order_date='" + order_date + '\'' +
                ", purchaser='" + purchaser + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
