package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 8/30/2017.
 */

public class EmergencyContactGetterSetter {
    private String emergencyContactId;
    private String emergencyContactPhoneNumber;
    private String emergencyContactImag;
    private String emergencyContactServiceName;

    public EmergencyContactGetterSetter(String emergencyContactId, String emergencyContactPhoneNumber ,String emergencyContactImag, String emergencyContactServiceName) {
        this.emergencyContactId = emergencyContactId;
        this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
        this.emergencyContactImag = emergencyContactImag;
        this.emergencyContactServiceName = emergencyContactServiceName;
    }

    public String getEmergencyContactId() {
        return emergencyContactId;
    }

    public String getEmergencyContactPhoneNumber() {
        return emergencyContactPhoneNumber;
    }

    public String getEmergencyContactImag() {
        return emergencyContactImag;
    }

    public String getEmergencyContactServiceName() {
        return emergencyContactServiceName;
    }
}
