package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 8/30/2017.
 */

public class HospitalMultipleDocGetterSetter {

    private String hospitalRowId ;
    private String hospitalRowDocImg ;
    private String hospitalRowDocName ;
    private Double hospitalRowDocLat ;
    private Double hospitalRowDocLang ;


    public HospitalMultipleDocGetterSetter(){

    }

    public HospitalMultipleDocGetterSetter(String hospitalRowId, String hospitalRowDocImg, String hospitalRowDocName, Double hospitalRowDocLat, Double hospitalRowDocLang) {
        this.hospitalRowId = hospitalRowId;
        this.hospitalRowDocImg = hospitalRowDocImg;
        this.hospitalRowDocName = hospitalRowDocName;
        this.hospitalRowDocLat = hospitalRowDocLat;
        this.hospitalRowDocLang = hospitalRowDocLang;
    }

    public String getHospitalRowId() {
        return hospitalRowId;
    }

    public String getHospitalRowDocImg() {
        return hospitalRowDocImg;
    }

    public String getHospitalRowDocName() {
        return hospitalRowDocName;
    }

    public Double getHospitalRowDocLat() {
        return hospitalRowDocLat;
    }

    public Double getHospitalRowDocLang() {
        return hospitalRowDocLang;
    }

    public void setHospitalRowId(String hospitalRowId) {
        this.hospitalRowId = hospitalRowId;
    }

    public void setHospitalRowDocImg(String hospitalRowDocImg) {
        this.hospitalRowDocImg = hospitalRowDocImg;
    }

    public void setHospitalRowDocName(String hospitalRowDocName) {
        this.hospitalRowDocName = hospitalRowDocName;
    }

    public void setHospitalRowDocLat(Double hospitalRowDocLat) {
        this.hospitalRowDocLat = hospitalRowDocLat;
    }

    public void setHospitalRowDocLang(Double hospitalRowDocLang) {
        this.hospitalRowDocLang = hospitalRowDocLang;
    }
}
