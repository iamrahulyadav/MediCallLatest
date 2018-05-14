package themedicall.com.GetterSetter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 8/28/2017.
 */

public class HospitalsGetterSetter {
    private String hospitalId;
    private String hospitalProfileImg;
    private String hospitalProfileName;
    private String hospitalProfileAddress;
    private String hospitalProfileNoOfDoc;
    private String hospitalProfileNoOfViews;
    private String hospitalProfileNoOfThumbsUp;
    private String hospitalProfileShareLink;
    private String hospitalProfileRating;
    private String hospitalProfileDiscount;
    private Double hospitalKm;
    ArrayList<HospitalLandLineListGetterSetter> landLineList;
    private ArrayList<HospitalMultipleDocGetterSetter> allDocList;

    public HospitalsGetterSetter() {

    }

    public HospitalsGetterSetter(String hospitalId, String hospitalProfileImg, String hospitalProfileName, String hospitalProfileAddress ,String hospitalProfileNoOfDoc , String hospitalProfileNoOfViews  , String hospitalProfileNoOfThumbsUp , String hospitalProfileShareLink , String hospitalProfileRating , String hospitalProfileDiscount  , Double hospitalKm , ArrayList<HospitalLandLineListGetterSetter> landLineList , ArrayList<HospitalMultipleDocGetterSetter> allDocList) {
        this.hospitalId = hospitalId;
        this.hospitalProfileImg = hospitalProfileImg;
        this.hospitalProfileName = hospitalProfileName;
        this.hospitalProfileAddress = hospitalProfileAddress;
        this.hospitalProfileNoOfDoc = hospitalProfileNoOfDoc;
        this.hospitalProfileNoOfViews = hospitalProfileNoOfViews;
        this.hospitalProfileNoOfThumbsUp = hospitalProfileNoOfThumbsUp;
        this.hospitalProfileShareLink = hospitalProfileShareLink;
        this.hospitalProfileRating = hospitalProfileRating;
        this.hospitalProfileDiscount = hospitalProfileDiscount;
        this.hospitalKm = hospitalKm;
        this.landLineList = landLineList;
        this.allDocList = allDocList;


    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getHospitalProfileImg() {
        return hospitalProfileImg;
    }

    public String getHospitalProfileName() {
        return hospitalProfileName;
    }

    public String getHospitalProfileAddress() {
        return hospitalProfileAddress;
    }

    public String getHospitalProfileNoOfDoc() {
        return hospitalProfileNoOfDoc;
    }

    public String getHospitalProfileNoOfViews() {
        return hospitalProfileNoOfViews;
    }

    public String getHospitalProfileNoOfThumbsUp() {
        return hospitalProfileNoOfThumbsUp;
    }

    public String getHospitalProfileShareLink() {
        return hospitalProfileShareLink;
    }

    public String getHospitalProfileRating() {
        return hospitalProfileRating;
    }

    public String getHospitalProfileDiscount() {
        return hospitalProfileDiscount;
    }

    public Double getHospitalKm() {
        return hospitalKm;
    }

    public ArrayList<HospitalLandLineListGetterSetter> getLandLineList() {
        return landLineList;
    }

    public ArrayList<HospitalMultipleDocGetterSetter> getAllDocList() {
        return allDocList;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setHospitalProfileImg(String hospitalProfileImg) {
        this.hospitalProfileImg = hospitalProfileImg;
    }

    public void setHospitalProfileName(String hospitalProfileName) {
        this.hospitalProfileName = hospitalProfileName;
    }

    public void setHospitalProfileAddress(String hospitalProfileAddress) {
        this.hospitalProfileAddress = hospitalProfileAddress;
    }

    public void setHospitalProfileNoOfDoc(String hospitalProfileNoOfDoc) {
        this.hospitalProfileNoOfDoc = hospitalProfileNoOfDoc;
    }

    public void setHospitalProfileNoOfViews(String hospitalProfileNoOfViews) {
        this.hospitalProfileNoOfViews = hospitalProfileNoOfViews;
    }

    public void setHospitalProfileNoOfThumbsUp(String hospitalProfileNoOfThumbsUp) {
        this.hospitalProfileNoOfThumbsUp = hospitalProfileNoOfThumbsUp;
    }

    public void setHospitalProfileShareLink(String hospitalProfileShareLink) {
        this.hospitalProfileShareLink = hospitalProfileShareLink;
    }

    public void setHospitalProfileRating(String hospitalProfileRating) {
        this.hospitalProfileRating = hospitalProfileRating;
    }

    public void setHospitalProfileDiscount(String hospitalProfileDiscount) {
        this.hospitalProfileDiscount = hospitalProfileDiscount;
    }

    public void setHospitalKm(Double hospitalKm) {
        this.hospitalKm = hospitalKm;
    }

    public void setLandLineList (ArrayList<HospitalLandLineListGetterSetter> landLineList) {
        this.landLineList = landLineList;
    }

    public void setAllDocList(ArrayList<HospitalMultipleDocGetterSetter> allDocList) {
        this.allDocList = allDocList;
    }

    public static List<HospitalsGetterSetter> creatData(int itemCount, List<HospitalsGetterSetter> arrayList) {

        Log.e("TAG", "My Item Count: " + itemCount);

        List<HospitalsGetterSetter> list = new ArrayList<>();

        for (int i = 0; i <itemCount; i++) {
            HospitalsGetterSetter movie = new HospitalsGetterSetter(arrayList.get(i).hospitalId, arrayList.get(i).hospitalProfileImg, arrayList.get(i).hospitalProfileName, arrayList.get(i).hospitalProfileAddress , arrayList.get(i).hospitalProfileNoOfDoc  , arrayList.get(i).hospitalProfileNoOfViews  , arrayList.get(i).hospitalProfileNoOfThumbsUp  , arrayList.get(i).hospitalProfileShareLink  , arrayList.get(i).hospitalProfileRating ,  arrayList.get(i).hospitalProfileDiscount ,  arrayList.get(i).hospitalKm ,arrayList.get(i).landLineList, arrayList.get(i).allDocList);
            list.add(movie);
        }
        return list;
    }


}
