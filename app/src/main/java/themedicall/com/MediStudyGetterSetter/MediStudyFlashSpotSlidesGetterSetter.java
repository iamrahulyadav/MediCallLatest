package themedicall.com.MediStudyGetterSetter;

/**
 * Created by Muhammad Adeel on 4/12/2018.
 */

public class MediStudyFlashSpotSlidesGetterSetter {
    private String id;
    private String description;
    private String imageUrl;

    public MediStudyFlashSpotSlidesGetterSetter(String id, String description, String imageUrl) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
