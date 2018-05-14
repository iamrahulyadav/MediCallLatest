package themedicall.com.GetterSetter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 8/28/2017.
 */

public class FindDoctorGetterSetter {
    private String doctorRowProfileImg;
    private String doctorRowExperience;
    private String doctorRowId;
    private String doctor_offer_any_discount;
    private String doctorRowVerifiedStatus;
    private String doctorRowName;
    private String doctorRowStatus;
    private String doctorRowMinFees;
    private String doctorRowMaxFees;
    private String doctorRowDiscountedImg;
    private Double doctorRowDistance;
    private String doctorRowNoOfThumbsUp;
    private String doctorRowNoOfViews;
    private String doctorRowSpeciality;
    private String doctorRowShareLink;
    private Double doctorRowRatingtext;
    private ArrayList doctorRowDegree;
    ArrayList<HospitalLandLineListGetterSetter> landLineList;
    private ArrayList<HospitalMultipleDocGetterSetter> hospitalList;

    public FindDoctorGetterSetter()
    {

    }

    public FindDoctorGetterSetter(String doctorRowProfileImg, String  doctorRowExperience , String doctorRowId, String doctor_offer_any_discount , String doctorRowVerifiedStatus , String doctorRowName, String doctorRowStatus, String doctorRowMinFees, String doctorRowMaxFees, String doctorRowDiscountedImg, Double doctorRowDistance, String doctorRowNoOfThumbsUp, String doctorRowNoOfViews , String doctorRowSpeciality, String doctorRowShareLink , Double doctorRowRatingtext,  ArrayList doctorRowDegree , ArrayList<HospitalLandLineListGetterSetter> landLineList, ArrayList<HospitalMultipleDocGetterSetter> hospitalList) {
        this.doctorRowProfileImg = doctorRowProfileImg;
        this.doctorRowExperience = doctorRowExperience;
        this.doctorRowId = doctorRowId;
        this.doctor_offer_any_discount = doctor_offer_any_discount;
        this.doctorRowVerifiedStatus = doctorRowVerifiedStatus;
        this.doctorRowName = doctorRowName;
        this.doctorRowStatus = doctorRowStatus;
        this.doctorRowMinFees = doctorRowMinFees;
        this.doctorRowMaxFees = doctorRowMaxFees;
        this.doctorRowDiscountedImg = doctorRowDiscountedImg;
        this.doctorRowDistance = doctorRowDistance;
        this.doctorRowNoOfThumbsUp = doctorRowNoOfThumbsUp;
        this.doctorRowNoOfViews = doctorRowNoOfViews;
        this.doctorRowSpeciality = doctorRowSpeciality;
        this.doctorRowShareLink = doctorRowShareLink;
        this.doctorRowRatingtext = doctorRowRatingtext;
        this.doctorRowDegree = doctorRowDegree;
        this.landLineList = landLineList;
        this.hospitalList = hospitalList;
    }

    public String getDoctorRowProfileImg() {
        return doctorRowProfileImg;
    }

    public String getDoctorRowExperience() {
        return doctorRowExperience;
    }

    public String getDoctorRowId() {
        return doctorRowId;
    }

    public String getDoctor_offer_any_discount() {
        return doctor_offer_any_discount;
    }

    public String getDoctorRowVerifiedStatus() {
        return doctorRowVerifiedStatus;
    }

    public String getDoctorRowName() {
        return doctorRowName;
    }

    public String getDoctorRowStatus() {
        return doctorRowStatus;
    }

    public String getDoctorRowMinFees() {
        return doctorRowMinFees;
    }

    public String getDoctorRowMaxFees() {
        return doctorRowMaxFees;
    }

    public String getDoctorRowDiscountedImg() {
        return doctorRowDiscountedImg;
    }

    public Double getDoctorRowDistance() {
        return doctorRowDistance;
    }

    public String getDoctorRowNoOfThumbsUp() {
        return doctorRowNoOfThumbsUp;
    }


    public String getDoctorRowNoOfViews() {
        return doctorRowNoOfViews;
    }

    public String getDoctorRowSpeciality() {
        return doctorRowSpeciality;
    }

    public ArrayList getDoctorRowDegree() {
        return doctorRowDegree;
    }

    public ArrayList<HospitalLandLineListGetterSetter> getLandLineList() {
        return landLineList;
    }

    public ArrayList<HospitalMultipleDocGetterSetter> getHospitalList() {
        return hospitalList;
    }

    public void setDoctorRowProfileImg(String doctorRowProfileImg) {
        this.doctorRowProfileImg = doctorRowProfileImg;
    }

    public void setDoctorRowExperience(String doctorRowExperience) {
        this.doctorRowExperience = doctorRowExperience;
    }

    public void setDoctorRowId(String doctorRowId) {
        this.doctorRowId = doctorRowId;
    }

    public void setDoctor_offer_any_discount(String doctor_offer_any_discount) {
        this.doctor_offer_any_discount = doctor_offer_any_discount;
    }

    public void setDoctorRowVerifiedStatus(String doctorRowVerifiedStatus) {
        this.doctorRowVerifiedStatus = doctorRowVerifiedStatus;
    }

    public void setDoctorRowName(String doctorRowName) {
        this.doctorRowName = doctorRowName;
    }

    public void setDoctorRowStatus(String doctorRowStatus) {
        this.doctorRowStatus = doctorRowStatus;
    }

    public void setDoctorRowMinFees(String doctorRowMinFees) {
        this.doctorRowMinFees = doctorRowMinFees;
    }

    public void setDoctorRowMaxFees(String doctorRowMaxFees) {
        this.doctorRowMaxFees = doctorRowMaxFees;
    }

    public void setDoctorRowDiscountedImg(String doctorRowDiscountedImg) {
        this.doctorRowDiscountedImg = doctorRowDiscountedImg;
    }

    public void setDoctorRowDistance(Double doctorRowDistance) {
        this.doctorRowDistance = doctorRowDistance;
    }

    public void setDoctorRowNoOfThumbsUp(String doctorRowNoOfThumbsUp) {
        this.doctorRowNoOfThumbsUp = doctorRowNoOfThumbsUp;
    }


    public void setDoctorRowSpeciality(String doctorRowSpeciality) {
        this.doctorRowSpeciality = doctorRowSpeciality;
    }

    public void setDoctorRowDegree(ArrayList doctorRowDegree) {
        this.doctorRowDegree = doctorRowDegree;
    }

    public void setDoctorRowNoOfViews(String doctorRowNoOfViews) {
        this.doctorRowNoOfViews = doctorRowNoOfViews;
    }

    public void setHospitalList(ArrayList<HospitalMultipleDocGetterSetter> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public String getDoctorRowShareLink() {
        return doctorRowShareLink;
    }

    public void setDoctorRowShareLink(String doctorRowShareLink) {
        this.doctorRowShareLink = doctorRowShareLink;
    }

    public Double getDoctorRowRatingtext() {
        return doctorRowRatingtext;
    }

    public void setDoctorRowRatingtext(Double doctorRowRatingtext) {
        this.doctorRowRatingtext = doctorRowRatingtext;
    }

    public void setLandLineList(ArrayList<HospitalLandLineListGetterSetter> landLineList) {
        this.landLineList = landLineList;
    }

    public static List<FindDoctorGetterSetter> creatData(int itemCount, List<FindDoctorGetterSetter> arrayList) {

        Log.e("TAG", "My Item Count: " + itemCount);

        List<FindDoctorGetterSetter> list = new ArrayList<>();

        for (int i = 0; i <itemCount; i++) {
            FindDoctorGetterSetter movie = new FindDoctorGetterSetter(arrayList.get(i).doctorRowProfileImg, arrayList.get(i).doctorRowExperience, arrayList.get(i).doctorRowId, arrayList.get(i).doctor_offer_any_discount , arrayList.get(i).doctorRowVerifiedStatus , arrayList.get(i).doctorRowName, arrayList.get(i).doctorRowStatus, arrayList.get(i).doctorRowMinFees, arrayList.get(i).doctorRowMaxFees, arrayList.get(i).doctorRowDiscountedImg, arrayList.get(i).doctorRowDistance, arrayList.get(i).doctorRowNoOfThumbsUp, arrayList.get(i).doctorRowNoOfViews, arrayList.get(i).doctorRowSpeciality, arrayList.get(i).doctorRowShareLink  , arrayList.get(i).doctorRowRatingtext  , arrayList.get(i).doctorRowDegree , arrayList.get(i).landLineList , arrayList.get(i).hospitalList);
            list.add(movie);
        }
        return list;
    }



}
