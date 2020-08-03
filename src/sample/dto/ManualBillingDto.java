package sample.dto;

/**
 * Created by Mitra on 10/12/18.
 */
public class ManualBillingDto {
    private int id;
    private String date;
    private int invoice_no;
    private String customer_name;
    private String customer_phno;
    private String customer_details;
    private String itemName;
    private String packing;
    private String hsnCode;
    private double quantity;
    private double rate;
    private double discPer;
    private double discAmount;
    private double taxableAmount;
    private double cgst;
    private double cgst_amount;
    private double sgst;
    private double sgst_amount;
    private double net_total;
    private String bill_status;
    private String billed_by;
    private double deductionAmount;
    private double billAmount;
    private String dispatchDetails;
    private String paymentDetails;
    private String paymentType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(int invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phno() {
        return customer_phno;
    }

    public void setCustomer_phno(String customer_phno) {
        this.customer_phno = customer_phno;
    }

    public String getCustomer_details() {
        return customer_details;
    }

    public void setCustomer_details(String customer_details) {
        this.customer_details = customer_details;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getDiscPer() {
        return discPer;
    }

    public void setDiscPer(double discPer) {
        this.discPer = discPer;
    }

    public double getDiscAmount() {
        return discAmount;
    }

    public void setDiscAmount(double discAmount) {
        this.discAmount = discAmount;
    }

    public double getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(double taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getCgst_amount() {
        return cgst_amount;
    }

    public void setCgst_amount(double cgst_amount) {
        this.cgst_amount = cgst_amount;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getSgst_amount() {
        return sgst_amount;
    }

    public void setSgst_amount(double sgst_amount) {
        this.sgst_amount = sgst_amount;
    }

    public double getNet_total() {
        return net_total;
    }

    public void setNet_total(double net_total) {
        this.net_total = net_total;
    }

    public String getBill_status() {
        return bill_status;
    }

    public void setBill_status(String bill_status) {
        this.bill_status = bill_status;
    }

    public String getBilled_by() {
        return billed_by;
    }

    public void setBilled_by(String billed_by) {
        this.billed_by = billed_by;
    }

    public double getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(double deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public String getDispatchDetails() {
        return dispatchDetails;
    }

    public void setDispatchDetails(String dispatchDetails) {
        this.dispatchDetails = dispatchDetails;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public ManualBillingDto() {

        id = 0;
        date = "";
        invoice_no = 0;
        customer_name = "";
        customer_phno = "";
        customer_details = "";
        itemName = "";
        packing = "";
        hsnCode = "";
        quantity = 0;
        rate = 0;
        discPer = 0;
        discAmount = 0;
        taxableAmount = 0;
        cgst = 0;
        cgst_amount = 0;
        sgst = 0;
        sgst_amount = 0;
        net_total = 0;
        bill_status = "";
        billed_by = "";
        deductionAmount = 0;
        billAmount = 0;
        dispatchDetails = "";
        paymentDetails = "";
        paymentType = "";

    }

    @Override
    public String toString() {
        return "ManualBillingDto{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", invoice_no=" + invoice_no +
                ", customer_name='" + customer_name + '\'' +
                ", customer_phno='" + customer_phno + '\'' +
                ", customer_details='" + customer_details + '\'' +
                ", itemName='" + itemName + '\'' +
                ", packing='" + packing + '\'' +
                ", hsnCode='" + hsnCode + '\'' +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", discPer=" + discPer +
                ", discAmount=" + discAmount +
                ", taxableAmount=" + taxableAmount +
                ", cgst=" + cgst +
                ", cgst_amount=" + cgst_amount +
                ", sgst=" + sgst +
                ", sgst_amount=" + sgst_amount +
                ", net_total=" + net_total +
                ", bill_status='" + bill_status + '\'' +
                ", billed_by='" + billed_by + '\'' +
                ", deductionAmount=" + deductionAmount +
                ", billAmount=" + billAmount +
                ", dispatchDetails='" + dispatchDetails + '\'' +
                ", paymentDetails='" + paymentDetails + '\'' +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }
}
