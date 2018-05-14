package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 10/4/2017.
 */

public class ReviewsGetterSetter {
    private String reviewsRowId;
    private String reviewsRowName;
    private int reviewsRowVerifiedImg;
    private String reviewsRowReview;
    private String reviewRowMonthAgo;


    public ReviewsGetterSetter(String reviewsRowId, String reviewsRowName, int reviewsRowVerifiedImg, String reviewsRowReview, String reviewRowMonthAgo) {
        this.reviewsRowId = reviewsRowId;
        this.reviewsRowName = reviewsRowName;
        this.reviewsRowVerifiedImg = reviewsRowVerifiedImg;
        this.reviewsRowReview = reviewsRowReview;
        this.reviewRowMonthAgo = reviewRowMonthAgo;
    }

    public String getReviewsRowId() {
        return reviewsRowId;
    }

    public String getReviewsRowName() {
        return reviewsRowName;
    }

    public int getReviewsRowVerifiedImg() {
        return reviewsRowVerifiedImg;
    }

    public String getReviewsRowReview() {
        return reviewsRowReview;
    }

    public String getReviewRowMonthAgo() {
        return reviewRowMonthAgo;
    }
}
