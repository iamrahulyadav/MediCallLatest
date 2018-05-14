package themedicall.com.GetterSetter;

import java.io.Serializable;

/**
 * Created by Muhammad Adeel on 10/31/2017.
 */

public class CitiesGetterSetter implements Serializable {

    private String id;
    private String name;

    public CitiesGetterSetter(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
