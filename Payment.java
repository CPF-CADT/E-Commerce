public class Payment {
    private String paymentId;
    private String orderId;
    private String paymentDate;
    private String paymentMethod;
    private String paymentStatus;

    
    public Payment(String id, String order, String date, String method, String status) {
        this.paymentId = id;
        this.orderId = order;
        this.paymentDate = date;
        this.paymentMethod = method;
        this.paymentStatus = status;
    }

    
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
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
