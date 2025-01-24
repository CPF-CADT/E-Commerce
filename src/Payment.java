
public class Payment {
    private int paymentId;
    private int orderId;
    private int paymentDate;
    private String paymentMethod;
    private String paymentStatus;

    
    public Payment(int id, int order, int date, String method, String status) {
        this.paymentId = id;
        this.orderId = order;
        this.paymentDate = date;
        this.paymentMethod = method;
        this.paymentStatus = status;
    }

    
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(int paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
