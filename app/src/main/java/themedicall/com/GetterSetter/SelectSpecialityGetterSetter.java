package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 11/15/2017.
 */

public class SelectSpecialityGetterSetter {

    private String speciality_id;
    private String speciality_name;
    private String speciality_image;

    public SelectSpecialityGetterSetter(String speciality_id, String speciality_name, String speciality_image) {
        this.speciality_id = speciality_id;
        this.speciality_name = speciality_name;
        this.speciality_image = speciality_image;
    }

    public String getSpeciality_id() {
        return speciality_id;
    }

    public String getSpeciality_name() {
        return speciality_name;
    }

    public String getSpeciality_image() {
        return speciality_image;
    }
}
