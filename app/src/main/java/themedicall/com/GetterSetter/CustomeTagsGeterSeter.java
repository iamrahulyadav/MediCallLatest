package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 12/29/2017.
 */

public class CustomeTagsGeterSeter {


    private String id;
    private String name;
    private String specialityId;

    public CustomeTagsGeterSeter(String id, String name, String specialistyId) {
        this.id = id;
        this.name = name;
        this.specialityId = specialistyId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getSpecialityId(){
        return specialityId;
    }
}
