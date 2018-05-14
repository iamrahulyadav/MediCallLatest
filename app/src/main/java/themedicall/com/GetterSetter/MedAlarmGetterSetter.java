package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 3/29/2018.
 */

public class MedAlarmGetterSetter {

    private String medicineName;
    private String medicineTake;
    private String medicineSkip;

    public MedAlarmGetterSetter(String medicineName, String medicineTake, String medicineSkip) {
        this.medicineName = medicineName;
        this.medicineTake = medicineTake;
        this.medicineSkip = medicineSkip;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineTake() {
        return medicineTake;
    }

    public void setMedicineTake(String medicineTake) {
        this.medicineTake = medicineTake;
    }

    public String getMedicineSkip() {
        return medicineSkip;
    }

    public void setMedicineSkip(String medicineSkip) {
        this.medicineSkip = medicineSkip;
    }
}
