package themedicall.com.GetterSetter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 8/28/2017.
 */

public class PharmacyGetterSetter {
    private String pharmacyRowId;
    private String pharmacyRowProfileImg;
    private Double pharmacyRowDistance;
    private String pharmacyRowName;
    private String pharmacyRowAddress;
    private String pharmacyRowPhoneNumber;
    private String pharmacyRowDiscountImg;

    public PharmacyGetterSetter(String pharmacyRowId, String pharmacyRowProfileImg, Double pharmacyRowDistance, String pharmacyRowName, String pharmacyRowAddress, String pharmacyRowPhoneNumber, String pharmacyRowDiscountImg) {
        this.pharmacyRowId = pharmacyRowId;
        this.pharmacyRowProfileImg = pharmacyRowProfileImg;
        this.pharmacyRowDistance = pharmacyRowDistance;
        this.pharmacyRowName = pharmacyRowName;
        this.pharmacyRowAddress = pharmacyRowAddress;
        this.pharmacyRowPhoneNumber = pharmacyRowPhoneNumber;
        this.pharmacyRowDiscountImg = pharmacyRowDiscountImg;
    }

    public String getPharmacyRowId() {
        return pharmacyRowId;
    }

    public String getPharmacyRowProfileImg() {
        return pharmacyRowProfileImg;
    }

    public Double getPharmacyRowDistance() {
        return pharmacyRowDistance;
    }

    public String getPharmacyRowName() {
        return pharmacyRowName;
    }

    public String getPharmacyRowAddress() {
        return pharmacyRowAddress;
    }

    public String getPharmacyRowPhoneNumber() {
        return pharmacyRowPhoneNumber;
    }

    public String getPharmacyRowDiscountImg() {
        return pharmacyRowDiscountImg;
    }

    public static List<PharmacyGetterSetter> creatData(int itemCount, List<PharmacyGetterSetter> arrayList) {

        Log.e("TAG", "My Item Count: " + itemCount);

        List<PharmacyGetterSetter> list = new ArrayList<>();

        for (int i = 0; i <itemCount; i++) {
            PharmacyGetterSetter movie = new PharmacyGetterSetter(arrayList.get(i).pharmacyRowId, arrayList.get(i).pharmacyRowProfileImg, arrayList.get(i).pharmacyRowDistance, arrayList.get(i).pharmacyRowName , arrayList.get(i).pharmacyRowAddress , arrayList.get(i).pharmacyRowPhoneNumber , arrayList.get(i).pharmacyRowDiscountImg);
            list.add(movie);
        }
        return list;

    }
}
