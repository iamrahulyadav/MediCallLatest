package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 2/9/2018.
 */

public class ChugtaiLabGetterSetter {
    private String lab_test_id ;
    private String id ;
    private String lab_test_name ;
    private String lab_test_rate ;

    public ChugtaiLabGetterSetter(String lab_test_id, String id, String lab_test_name, String lab_test_rate) {
        this.lab_test_id = lab_test_id;
        this.id = id;
        this.lab_test_name = lab_test_name;
        this.lab_test_rate = lab_test_rate;
    }

    public String getLab_test_id() {
        return lab_test_id;
    }

    public void setLab_test_id(String lab_test_id) {
        this.lab_test_id = lab_test_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLab_test_name() {
        return lab_test_name;
    }

    public void setLab_test_name(String lab_test_name) {
        this.lab_test_name = lab_test_name;
    }

    public String getLab_test_rate() {
        return lab_test_rate;
    }

    public void setLab_test_rate(String lab_test_rate) {
        this.lab_test_rate = lab_test_rate;
    }
}
