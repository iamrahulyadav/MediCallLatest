package themedicall.com.MediStudyGetterSetter;

/**
 * Created by Muhammad Adeel on 4/25/2018.
 */

public class MediStudyDownloadBookGetterSetter {
    private String mediBookId ;
    private String mediImageURL ;
    private String mediBookURL ;
    private String mediBookName ;
    private String mediBookAuthorName ;
    private String mediBookDescription ;
    private String mediBookRating ;
    private String subjectName ;

    public MediStudyDownloadBookGetterSetter(String mediBookId, String mediImageURL, String mediBookURL, String mediBookName, String mediBookAuthorName, String mediBookDescription, String mediBookRating, String subjectName) {
        this.mediBookId = mediBookId;
        this.mediImageURL = mediImageURL;
        this.mediBookURL = mediBookURL;
        this.mediBookName = mediBookName;
        this.mediBookAuthorName = mediBookAuthorName;
        this.mediBookDescription = mediBookDescription;
        this.mediBookRating = mediBookRating;
        this.subjectName = subjectName;
    }

    public String getMediBookId() {
        return mediBookId;
    }

    public String getMediImageURL() {
        return mediImageURL;
    }

    public String getMediBookURL() {
        return mediBookURL;
    }

    public String getMediBookName() {
        return mediBookName;
    }

    public String getMediBookAuthorName() {
        return mediBookAuthorName;
    }

    public String getMediBookDescription() {
        return mediBookDescription;
    }

    public String getMediBookRating() {
        return mediBookRating;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
