package themedicall.com.GetterSetter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 8/28/2017.
 */

public class LabsGetterSetter {
    private String labRowId;
    private String labRowProfileImg;
    private Double labRowDistance;
    private String labRowName;
    private String labRowAddress;
    private String labRowPhoneNumber;
    private String labRowDiscounted;

    public LabsGetterSetter(String labRowId, String labRowProfileImg, Double labRowDistance, String labRowName, String labRowAddress, String labRowPhoneNumber, String labRowDiscounted) {
        this.labRowId = labRowId;
        this.labRowProfileImg = labRowProfileImg;
        this.labRowDistance = labRowDistance;
        this.labRowName = labRowName;
        this.labRowAddress = labRowAddress;
        this.labRowPhoneNumber = labRowPhoneNumber;
        this.labRowDiscounted = labRowDiscounted;
    }

    public String getLabRowId() {
        return labRowId;
    }

    public String getLabRowProfileImg() {
        return labRowProfileImg;
    }

    public Double getLabRowDistance() {
        return labRowDistance;
    }

    public String getLabRowName() {
        return labRowName;
    }

    public String getLabRowAddress() {
        return labRowAddress;
    }

    public String getLabRowPhoneNumber() {
        return labRowPhoneNumber;
    }

    public String getLabRowDiscounted() {
        return labRowDiscounted;
    }

    public static List<LabsGetterSetter> creatData(int itemCount, List<LabsGetterSetter> arrayList) {

        Log.e("TAG", "My Item Count: " + itemCount);

        List<LabsGetterSetter> list = new ArrayList<>();

        for (int i = 0; i <itemCount; i++) {
            LabsGetterSetter movie = new LabsGetterSetter(arrayList.get(i).labRowId, arrayList.get(i).labRowProfileImg, arrayList.get(i).labRowDistance, arrayList.get(i).labRowName , arrayList.get(i).labRowAddress , arrayList.get(i).labRowPhoneNumber , arrayList.get(i).labRowDiscounted);
            list.add(movie);
        }
        return list;

    }

}
