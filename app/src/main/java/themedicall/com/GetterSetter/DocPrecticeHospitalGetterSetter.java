package themedicall.com.GetterSetter;

import java.util.ArrayList;

/**
 * Created by Muhammad Adeel on 11/30/2017.
 */

public class DocPrecticeHospitalGetterSetter {

    private String doc_practice_hospital_id;
    private String doc_practice_doctor_id;
    private String doc_practice_hospital_name;
    private String doc_practice_hospital_img;
    private String hospital_addr;
    private String hospital_lat;
    private String hospital_lng;
    private int hospitalDetailCount;
    private ArrayList<HospitalLandLineListGetterSetter> landLineList;
    private ArrayList<HospitalServiceGetterSetter> precticeDetailServiceList;
    private ArrayList<HospitalTimingGetterSetter> timingList;

    public DocPrecticeHospitalGetterSetter(String doc_practice_hospital_id, String doc_practice_doctor_id, String doc_practice_hospital_name, String doc_practice_hospital_img, String hospital_addr, String hospital_lat, String hospital_lng ,int hospitalDetailCount ,  ArrayList<HospitalLandLineListGetterSetter> landLineList  ,  ArrayList<HospitalServiceGetterSetter> precticeDetailServiceList    ,ArrayList<HospitalTimingGetterSetter> timingList ) {
        this.doc_practice_hospital_id = doc_practice_hospital_id;
        this.doc_practice_doctor_id = doc_practice_doctor_id;
        this.doc_practice_hospital_name = doc_practice_hospital_name;
        this.doc_practice_hospital_img = doc_practice_hospital_img;
        this.hospital_addr = hospital_addr;
        this.hospital_lat = hospital_lat;
        this.hospital_lng = hospital_lng;
        this.landLineList = landLineList ;
        this.hospitalDetailCount = hospitalDetailCount;
        this.precticeDetailServiceList = precticeDetailServiceList;
        this.timingList = timingList;
    }

    public String getDoc_practice_hospital_id() {
        return doc_practice_hospital_id;
    }

    public String getDoc_practice_doctor_id() {
        return doc_practice_doctor_id;
    }

    public String getDoc_practice_hospital_name() {
        return doc_practice_hospital_name;
    }

    public String getDoc_practice_hospital_img() {
        return doc_practice_hospital_img;
    }

    public String getHospital_addr() {
        return hospital_addr;
    }

    public String getHospital_lat() {
        return hospital_lat;
    }

    public String getHospital_lng() {
        return hospital_lng;
    }

    public int getHospitalDetailCount() {
        return hospitalDetailCount;
    }

    public ArrayList<HospitalLandLineListGetterSetter> getLandLineList() {
        return landLineList;
    }

    public ArrayList<HospitalServiceGetterSetter> getPrecticeDetailServiceList() {
        return precticeDetailServiceList;
    }


    public ArrayList<HospitalTimingGetterSetter> getTimingList(){
        return timingList;
    }


    public void setDoc_practice_hospital_id(String doc_practice_hospital_id) {
        this.doc_practice_hospital_id = doc_practice_hospital_id;
    }

    public void setDoc_practice_doctor_id(String doc_practice_doctor_id) {
        this.doc_practice_doctor_id = doc_practice_doctor_id;
    }

    public void setDoc_practice_hospital_name(String doc_practice_hospital_name) {
        this.doc_practice_hospital_name = doc_practice_hospital_name;
    }

    public void setDoc_practice_hospital_img(String doc_practice_hospital_img) {
        this.doc_practice_hospital_img = doc_practice_hospital_img;
    }

    public void setHospital_addr(String hospital_addr) {
        this.hospital_addr = hospital_addr;
    }

    public void setHospital_lat(String hospital_lat) {
        this.hospital_lat = hospital_lat;
    }

    public void setHospital_lng(String hospital_lng) {
        this.hospital_lng = hospital_lng;
    }

    public void setPrecticeDetailServiceList(ArrayList<HospitalServiceGetterSetter> precticeDetailServiceList) {
        this.precticeDetailServiceList = precticeDetailServiceList;
    }

    public void setHospitalDetailCount(int hospitalDetailCount) {
        this.hospitalDetailCount = hospitalDetailCount;
    }

    public void setLandLineList(ArrayList<HospitalLandLineListGetterSetter> landLineList) {
        this.landLineList = landLineList;
    }

    public void setTimingList(ArrayList<HospitalTimingGetterSetter> timingList) {
        this.timingList = timingList;
    }

    public void setHospitalTiming(ArrayList timingList) {
        this.timingList = timingList;
    }
}
