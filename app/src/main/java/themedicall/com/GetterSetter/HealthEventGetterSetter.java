package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 9/27/2017.
 */

public class HealthEventGetterSetter {

    private String healthEventRowId;
    private int healthEventRowProfileImg;
    private String healthEventRowName;
    private String healthEventRowDate;
    private int healthEventRowCoverImg;
    private String healthEventRowStartDate;
    private String healthEventRowStartTime;
    private String healthEventRowEndDate;
    private String healthEventRowEndTime;
    private String healthEventRowTitle;
    private String healthEventRowAddress;
    private String healthEventRowEndFavourite;
    private String healthEventRowEndView;
    private String healthEventRowEndShare;
    private String healthEventRowEndSave;

    public HealthEventGetterSetter(String healthEventRowId, int healthEventRowProfileImg, String healthEventRowName, String healthEventRowDate, int healthEventRowCoverImg, String healthEventRowStartDate, String healthEventRowStartTime, String healthEventRowEndDate, String healthEventRowEndTime, String healthEventRowTitle, String healthEventRowAddress, String healthEventRowEndFavourite, String healthEventRowEndView, String healthEventRowEndShare, String healthEventRowEndSave) {
        this.healthEventRowId = healthEventRowId;
        this.healthEventRowProfileImg = healthEventRowProfileImg;
        this.healthEventRowName = healthEventRowName;
        this.healthEventRowDate = healthEventRowDate;
        this.healthEventRowCoverImg = healthEventRowCoverImg;
        this.healthEventRowStartDate = healthEventRowStartDate;
        this.healthEventRowStartTime = healthEventRowStartTime;
        this.healthEventRowEndDate = healthEventRowEndDate;
        this.healthEventRowEndTime = healthEventRowEndTime;
        this.healthEventRowTitle = healthEventRowTitle;
        this.healthEventRowAddress = healthEventRowAddress;
        this.healthEventRowEndFavourite = healthEventRowEndFavourite;
        this.healthEventRowEndView = healthEventRowEndView;
        this.healthEventRowEndShare = healthEventRowEndShare;
        this.healthEventRowEndSave = healthEventRowEndSave;
    }

    public String getHealthEventRowId() {
        return healthEventRowId;
    }

    public int getHealthEventRowProfileImg() {
        return healthEventRowProfileImg;
    }

    public String getHealthEventRowName() {
        return healthEventRowName;
    }

    public String getHealthEventRowDate() {
        return healthEventRowDate;
    }

    public int getHealthEventRowCoverImg() {
        return healthEventRowCoverImg;
    }

    public String getHealthEventRowStartDate() {
        return healthEventRowStartDate;
    }

    public String getHealthEventRowStartTime() {
        return healthEventRowStartTime;
    }

    public String getHealthEventRowEndDate() {
        return healthEventRowEndDate;
    }

    public String getHealthEventRowEndTime() {
        return healthEventRowEndTime;
    }

    public String getHealthEventRowTitle() {
        return healthEventRowTitle;
    }

    public String getHealthEventRowAddress() {
        return healthEventRowAddress;
    }

    public String getHealthEventRowEndFavourite() {
        return healthEventRowEndFavourite;
    }

    public String getHealthEventRowEndView() {
        return healthEventRowEndView;
    }

    public String getHealthEventRowEndShare() {
        return healthEventRowEndShare;
    }

    public String getHealthEventRowEndSave() {
        return healthEventRowEndSave;
    }
}
