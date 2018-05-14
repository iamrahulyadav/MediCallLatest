package themedicall.com.GetterSetter;

import java.util.ArrayList;

/**
 * Created by User-10 on 15-Feb-18.
 */

public class ClaimProfileGetterSetter {

    private String doctorRowProfileImg;
    private String doctorName;
    private String doctorId;
    private ArrayList doctorQualifications;
    private ArrayList docterSpecialities;


    public ClaimProfileGetterSetter()
    {

    }

    public ClaimProfileGetterSetter(String doctorRowProfileImg, String  doctorName , String doctorId, ArrayList doctorQualifications , ArrayList doctorSpecialities ) {
        this.doctorRowProfileImg = doctorRowProfileImg;
        this.doctorName = doctorName;
        this.doctorId = doctorId;
        this.doctorQualifications = doctorQualifications;
        this.docterSpecialities = doctorSpecialities;



    }

    public String getDoctorRowProfileImg() {
        return doctorRowProfileImg;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public String getDoctorId() {
        return doctorId;
    }
    public ArrayList getDoctorQualifications() {
        return doctorQualifications;
    }
    public ArrayList getDocterSpecialities() {
        return docterSpecialities;
    }

    /*
    public static List<FindDoctorGetterSetter> creatData(int itemCount, List<FindDoctorGetterSetter> arrayList) {

        Log.e("TAG", "My Item Count: " + itemCount);

        List<FindDoctorGetterSetter> list = new ArrayList<>();

        for (int i = 0; i <itemCount; i++) {
            FindDoctorGetterSetter movie = new FindDoctorGetterSetter(arrayList.get(i).doctorRowProfileImg, arrayList.get(i).doctorRowExperience, arrayList.get(i).doctorRowId, arrayList.get(i).doctor_offer_any_discount , arrayList.get(i).doctorRowVerifiedStatus , arrayList.get(i).doctorRowName, arrayList.get(i).doctorRowStatus, arrayList.get(i).doctorRowMinFees, arrayList.get(i).doctorRowMaxFees, arrayList.get(i).doctorRowDiscountedImg, arrayList.get(i).doctorRowDistance, arrayList.get(i).doctorRowNoOfThumbsUp, arrayList.get(i).doctorRowNoOfViews, arrayList.get(i).doctorRowSpeciality, arrayList.get(i).doctorRowDegree, arrayList.get(i).hospitalList);
            list.add(movie);
        }
        return list;
    }*/

}