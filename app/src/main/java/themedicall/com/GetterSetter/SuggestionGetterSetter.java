package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 10/31/2017.
 */

public class SuggestionGetterSetter {

    private String id;
    private String name;

    public SuggestionGetterSetter(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public SuggestionGetterSetter() {

    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
