package sample.dto;

/**
 * Created by Mitra on 09/11/18.
 */
public class GstDto {

    private String type;
    private double amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "GstDto{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
