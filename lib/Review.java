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

   
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
