package themedicall.com.GetterSetter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 11/30/2017.
 */

public class HospitalDoctorGetterSetter {

    private String doctorRowProfileImg;
    private String doctorRowExperience;
    private String doctorRowId;
    private String doctorRowOfferDiscount;
    private String doctorRowVerifiedStatus;
    private String doctorRowName;
    private String doctorRowStatus;
    private String doctorRowMinFees;
    private String doctorRowMaxFees;
    private String doctorRowDiscountedImg;
    private String doctorRowDistance;
    private String doctorRowNoOfThumbsUp;
    private String doctorRowNoOfViews;
    private ArrayList doctorRowSpeciality;
    private ArrayList doctorRowQualification;
    private String doctorRowShareLink;
    private Double doctorRowRatingtext;

    public HospitalDoctorGetterSetter()
    {

    }

    public HospitalDoctorGetterSetter(String doctorRowProfileImg, String doctorRowExperience, String doctorRowId, String doctorRowOfferDiscount , String doctorRowVerifiedStatus , String doctorRowName, String doctorRowStatus, String doctorRowMinFees, String doctorRowMaxFees, String doctorRowDiscountedImg, String doctorRowDistance, String doctorRowNoOfThumbsUp, String doctorRowNoOfViews, ArrayList doctorRowSpeciality, ArrayList doctorRowQualification , String doctorRowShareLink , Double doctorRowRatingtext) {
        this.doctorRowProfileImg = doctorRowProfileImg;
        this.doctorRowExperience = doctorRowExperience;
        this.doctorRowId = doctorRowId;
        this.doctorRowOfferDiscount = doctorRowOfferDiscount;
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
        this.doctorRowQualification = doctorRowQualification;
        this.doctorRowShareLink = doctorRowShareLink;
        this.doctorRowRatingtext = doctorRowRatingtext;
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

    public String getDoctorRowOfferDiscount() {
        return doctorRowOfferDiscount;
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

    public String getDoctorRowDistance() {
        return doctorRowDistance;
    }

    public String getDoctorRowNoOfThumbsUp() {
        return doctorRowNoOfThumbsUp;
    }

    public String getDoctorRowNoOfViews() {
        return doctorRowNoOfViews;
    }

    public ArrayList getDoctorRowSpeciality() {
        return doctorRowSpeciality;
    }

    public ArrayList getDoctorRowQualification() {
        return doctorRowQualification;
    }

    public String getDoctorRowShareLink() {
        return doctorRowShareLink;
    }

    public Double getDoctorRowRatingtext() {
        return doctorRowRatingtext;
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

    public void setDoctorRowOfferDiscount(String doctorRowOfferDiscount) {
        this.doctorRowOfferDiscount = doctorRowOfferDiscount;
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

    public void setDoctorRowDistance(String doctorRowDistance) {
        this.doctorRowDistance = doctorRowDistance;
    }

    public void setDoctorRowNoOfThumbsUp(String doctorRowNoOfThumbsUp) {
        this.doctorRowNoOfThumbsUp = doctorRowNoOfThumbsUp;
    }

    public void setDoctorRowNoOfViews(String doctorRowNoOfViews) {
        this.doctorRowNoOfViews = doctorRowNoOfViews;
    }

    public void setDoctorRowSpeciality(ArrayList doctorRowSpeciality) {
        this.doctorRowSpeciality = doctorRowSpeciality;
    }

    public void setDoctorRowQualification(ArrayList doctorRowQualification) {
        this.doctorRowQualification = doctorRowQualification;
    }

    public void setDoctorRowShareLink(String doctorRowShareLink) {
        this.doctorRowShareLink = doctorRowShareLink;
    }

    public void setDoctorRowRatingtext(Double doctorRowRatingtext) {
        this.doctorRowRatingtext = doctorRowRatingtext;
    }


    public static List<HospitalDoctorGetterSetter> creatData(int itemCount, List<HospitalDoctorGetterSetter> arrayList) {

    Log.e("TAG", "My Item Count: " + itemCount);

    List<HospitalDoctorGetterSetter> list = new ArrayList<>();

    for (int i = 0; i <itemCount; i++) {
        HospitalDoctorGetterSetter movie = new HospitalDoctorGetterSetter(arrayList.get(i).doctorRowProfileImg, arrayList.get(i).doctorRowExperience, arrayList.get(i).doctorRowId, arrayList.get(i).doctorRowOfferDiscount , arrayList.get(i).doctorRowVerifiedStatus , arrayList.get(i).doctorRowName, arrayList.get(i).doctorRowStatus, arrayList.get(i).doctorRowMinFees, arrayList.get(i).doctorRowMaxFees, arrayList.get(i).doctorRowDiscountedImg, arrayList.get(i).doctorRowDistance, arrayList.get(i).doctorRowNoOfThumbsUp, arrayList.get(i).doctorRowNoOfViews, arrayList.get(i).doctorRowSpeciality, arrayList.get(i).doctorRowQualification  , arrayList.get(i).doctorRowShareLink  , arrayList.get(i).doctorRowRatingtext);
        list.add(movie);
    }
    return list;

    }


}
