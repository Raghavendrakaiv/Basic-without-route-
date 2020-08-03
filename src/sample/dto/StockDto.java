package sample.dto;

/**
 * Created by Mitra on 10/24/18.
 */
public class StockDto {

    private String productcode;
    private String hsn;
    private String stockaddeddate;
    private String productname;
    private double stockquantity;
    private double added_quantity;
    private double purchaseprice;
    private double mrp;
    private double cgst;
    private double sgst;
    private double total_amount;
    private String dealer_name;
    private int id;
    private double purchase_discount;
    private double selling_price;
    private String p_invoice;
    private String packing;

    public StockDto() {
        productcode = "";
        hsn = "";
        stockaddeddate = "";
        productname = "";
        stockquantity = 0;
        added_quantity = 0;
        purchaseprice = 0;
        mrp = 0;
        cgst = 0;
        sgst = 0;
        total_amount = 0;
        dealer_name = "";
        id = 0;
        purchase_discount = 0;
        selling_price = 0;
        p_invoice = "";
        packing = "";
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getStockaddeddate() {
        return stockaddeddate;
    }

    public void setStockaddeddate(String stockaddeddate) {
        this.stockaddeddate = stockaddeddate;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public double getStockquantity() {
        return stockquantity;
    }

    public void setStockquantity(double stockquantity) {
        this.stockquantity = stockquantity;
    }

    public double getAdded_quantity() {
        return added_quantity;
    }

    public void setAdded_quantity(double added_quantity) {
        this.added_quantity = added_quantity;
    }

    public double getPurchaseprice() {
        return purchaseprice;
    }

    public void setPurchaseprice(double purchaseprice) {
        this.purchaseprice = purchaseprice;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPurchase_discount() {
        return purchase_discount;
    }

    public void setPurchase_discount(double purchase_discount) {
        this.purchase_discount = purchase_discount;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public String getP_invoice() {
        return p_invoice;
    }

    public void setP_invoice(String p_invoice) {
        this.p_invoice = p_invoice;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    @Override
    public String toString() {
        return "StockDto{" +
                "productcode='" + productcode + '\'' +
                ", hsn='" + hsn + '\'' +
                ", stockaddeddate='" + stockaddeddate + '\'' +
                ", productname='" + productname + '\'' +
                ", stockquantity=" + stockquantity +
                ", added_quantity=" + added_quantity +
                ", purchaseprice=" + purchaseprice +
                ", mrp=" + mrp +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", total_amount=" + total_amount +
                ", dealer_name='" + dealer_name + '\'' +
                ", id=" + id +
                ", purchase_discount=" + purchase_discount +
                ", selling_price=" + selling_price +
                ", p_invoice='" + p_invoice + '\'' +
                ", packing='" + packing + '\'' +
                '}';
    }
}
