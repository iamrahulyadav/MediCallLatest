package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 2/13/2018.
 */

public class BloodAppealsGetterSetter {

    private String id;
    private String bloodGroup;
    private String name;
    private String contactNo;
    private String detail;
    private String description;
    private String Status;
    private String TimeToAgo;
    private String googleAddress;
    private Double distance;

    public BloodAppealsGetterSetter(String id, String bloodGroup, String name, String contactNo, String detail, String description, String status, String timeToAgo, String googleAddress, Double distance) {
        this.id = id;
        this.bloodGroup = bloodGroup;
        this.name = name;
        this.contactNo = contactNo;
        this.detail = detail;
        this.description = description;
        Status = status;
        TimeToAgo = timeToAgo;
        this.googleAddress = googleAddress;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTimeToAgo() {
        return TimeToAgo;
    }

    public void setTimeToAgo(String timeToAgo) {
        TimeToAgo = timeToAgo;
    }

    public String getGoogleAddress() {
        return googleAddress;
    }

    public void setGoogleAddress(String googleAddress) {
        this.googleAddress = googleAddress;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}