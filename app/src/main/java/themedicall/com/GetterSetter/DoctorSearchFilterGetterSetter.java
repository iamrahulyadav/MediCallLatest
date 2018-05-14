package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 1/17/2018.
 */

public class DoctorSearchFilterGetterSetter {
    private String id ;
    private String name ;
    private String speciality ;

    public DoctorSearchFilterGetterSetter(String id, String name, String speciality) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
