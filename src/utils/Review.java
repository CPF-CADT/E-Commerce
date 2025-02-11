package utils;
public class Review {
    static  int reviewId;
    public  int productId;
    public  int userId;
    public  int rating;
    public  String comment;


    public Review(int reviewIdid, int product, int user, int stars, String feedback) {
        reviewId = reviewId + 1;
        this.productId = product;
        this.userId = user;
        this.rating = stars;
        this.comment = feedback;
    }

}
