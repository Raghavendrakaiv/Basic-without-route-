package sample.dto;

/**
 * Created by Mitra on 10/24/18.
 */
public class DealerDto {

    private int dealer_id;
    private String dealer_name;
    private String dealer_gstn_number;
    private String dealer_contact_number;
    private String dealer_pan;
    private String dealer_account_details;
    private String dealer_address;
    private int id;

    public DealerDto() {
        dealer_id = 0;
        dealer_name = "";
        dealer_gstn_number = "";
        dealer_contact_number = "";
        dealer_pan = "";
        dealer_account_details = "";
        dealer_address = "";
        id = 0;
    }

    public int getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(int dealer_id) {
        this.dealer_id = dealer_id;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getDealer_gstn_number() {
        return dealer_gstn_number;
    }

    public void setDealer_gstn_number(String dealer_gstn_number) {
        this.dealer_gstn_number = dealer_gstn_number;
    }

    public String getDealer_contact_number() {
        return dealer_contact_number;
    }

    public void setDealer_contact_number(String dealer_contact_number) {
        this.dealer_contact_number = dealer_contact_number;
    }

    public String getDealer_pan() {
        return dealer_pan;
    }

    public void setDealer_pan(String dealer_pan) {
        this.dealer_pan = dealer_pan;
    }

    public String getDealer_account_details() {
        return dealer_account_details;
    }

    public void setDealer_account_details(String dealer_account_details) {
        this.dealer_account_details = dealer_account_details;
    }

    public String getDealer_address() {
        return dealer_address;
    }

    public void setDealer_address(String dealer_address) {
        this.dealer_address = dealer_address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DealerDto{" +
                "dealer_id=" + dealer_id +
                ", dealer_name='" + dealer_name + '\'' +
                ", dealer_gstn_number='" + dealer_gstn_number + '\'' +
                ", dealer_contact_number='" + dealer_contact_number + '\'' +
                ", dealer_pan='" + dealer_pan + '\'' +
                ", dealer_account_details='" + dealer_account_details + '\'' +
                ", dealer_address='" + dealer_address + '\'' +
                ", id=" + id +
                '}';
    }

}
