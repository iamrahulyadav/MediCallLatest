package themedicall.com.GetterSetter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 8/28/2017.
 */

public class BloodReqiredGetterSetter {
    private String bloodRequiredId;
    private String bloodRequiredGroup;
    private String bloodRequiredName;
    private String bloodRequiredCity;
    private String bloodRequiredPhoneNumber;
    private String bloodRequiredDescription;

    public BloodReqiredGetterSetter(String bloodRequiredId, String bloodRequiredGroup, String bloodRequiredName, String bloodRequiredCity, String bloodRequiredPhoneNumber ,  String bloodRequiredDescription) {
        this.bloodRequiredId = bloodRequiredId;
        this.bloodRequiredGroup = bloodRequiredGroup;
        this.bloodRequiredName = bloodRequiredName;
        this.bloodRequiredCity = bloodRequiredCity;
        this.bloodRequiredPhoneNumber = bloodRequiredPhoneNumber;
        this.bloodRequiredDescription = bloodRequiredDescription;
    }

    public BloodReqiredGetterSetter() {

    }

    public String getBloodRequiredPhoneNumber() {
        return bloodRequiredPhoneNumber;
    }

    public String getBloodRequiredId() {
        return bloodRequiredId;
    }

    public String getBloodRequiredGroup() {
        return bloodRequiredGroup;
    }

    public String getBloodRequiredName() {
        return bloodRequiredName;
    }

    public String getBloodRequiredCity() {
        return bloodRequiredCity;
    }

    public String getBloodRequiredDescription() {
        return bloodRequiredDescription;
    }


    public static List<BloodReqiredGetterSetter> creatData(int itemCount, List<BloodReqiredGetterSetter> arrayList) {

        Log.e("TAG", "My Item Count: " + itemCount);

        List<BloodReqiredGetterSetter> list = new ArrayList<>();

        for (int i = 0; i <itemCount; i++) {
            BloodReqiredGetterSetter movie = new BloodReqiredGetterSetter(arrayList.get(i).getBloodRequiredId(), arrayList.get(i).getBloodRequiredGroup(), arrayList.get(i).getBloodRequiredName(), arrayList.get(i).getBloodRequiredCity(), arrayList.get(i).getBloodRequiredPhoneNumber(), arrayList.get(i).getBloodRequiredDescription());
            list.add(movie);
        }
        return list;
    }


}
