public class Review {
    private int reviewId;
    private int productId;
    private int userId;
    private int rating;
    private String comment;


    public Review(int id, int product, int user, int stars, String feedback) {
        this.reviewId = id;
        this.productId = product;
        this.userId = user;
        this.rating = stars;
        this.comment = feedback;
    }

   
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
