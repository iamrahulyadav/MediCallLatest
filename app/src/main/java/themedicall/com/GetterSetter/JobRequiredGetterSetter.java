package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 9/28/2017.
 */

public class JobRequiredGetterSetter {

    private String jobRequiredRowId;
    private String jobRequiredRowTitle;
    private String jobRequiredRowLocation;
    private String jobRequiredRowGender;
    private String jobRequiredRowAge;

    public JobRequiredGetterSetter(String jobRequiredRowId, String jobRequiredRowTitle, String jobRequiredRowLocation, String jobRequiredRowGender, String jobRequiredRowAge) {
        this.jobRequiredRowId = jobRequiredRowId;
        this.jobRequiredRowTitle = jobRequiredRowTitle;
        this.jobRequiredRowLocation = jobRequiredRowLocation;
        this.jobRequiredRowGender = jobRequiredRowGender;
        this.jobRequiredRowAge = jobRequiredRowAge;
    }

    public String getJobRequiredRowId() {
        return jobRequiredRowId;
    }

    public String getJobRequiredRowTitle() {
        return jobRequiredRowTitle;
    }

    public String getJobRequiredRowLocation() {
        return jobRequiredRowLocation;
    }

    public String getJobRequiredRowGender() {
        return jobRequiredRowGender;
    }

    public String getJobRequiredRowAge() {
        return jobRequiredRowAge;
    }
}
