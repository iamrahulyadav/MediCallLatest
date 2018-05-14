package themedicall.com.MediPediaGetterSetter;

/**
 * Created by Muhammad Adeel on 3/2/2018.
 */

public class ListInfo {
    private String name;
    private String id;

    public ListInfo(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
