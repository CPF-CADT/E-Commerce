public class Review {
    private String reviewId;
    private String productId;
    private String userId;
    private int rating;
    private String comment;


    public Review(String id, String product, String user, int stars, String feedback) {
        this.reviewId = id;
        this.productId = product;
        this.userId = user;
        this.rating = stars;
        this.comment = feedback;
    }

}
