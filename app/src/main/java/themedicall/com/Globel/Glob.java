package themedicall.com.Globel;

/**
 * Created by Muhammad Adeel on 10/13/2017.
 */

public class Glob {

    public static final String Key = "Vx0cbjkzfQpyTObY8vfqgN1us";
    public static final String URL = "http://themedicall.com/medi-app-api/";
    public static final String FETCH_IMAGE_URL = "http://themedicall.com/public/uploaded/img/";
    public static final String BLOOD_GROUP_LISTING_URL = URL+"get-blood-donors";
    public static final String ALL_DOCTOR_URL = URL+"get-doctors";
    public static final String NEARBY_DOCTOR_URL = URL+"get-nearby-doctors";
    public static final String DISCOUNTED_DOCTOR_URL = URL+"get-discounted-doctors";
    public static final String DOCTOR_PRACTICE_DETAIL_URL = URL+"get-doctor-practice-details";
    public static final String DOCTOR_PRACTICE_TIMING_URL = URL+"get-doctor-practice-timings";
    public static final String DOCTOR_HOSPITAL_DETAIL_URL = URL+"get-doctor-hospitals";
    public static final String DOCTOR_BIO_URL = URL+"get-doctor-bio-details";
    public static final String ALL_HOSPITALS_LISTING_URL  = URL+"get-hospitals";
    public static final String NEARBY_HOSPITALS_LISTING_URL= URL+"get-nearby-hospitals";
    public static final String DISCOUNTED_HOSPITALS_LISTING_URL= URL+"get-discounted-hospitals";
    public static final String EMERGENCY_CENTERS_HOSPITALS_LISTING_URL= URL+"get-24-by-7-hospitals";
    public static final String DISCOUNTED_DOCTOR_IN_DISCOUNTED_OFFER_LISTING_URL= URL+"get-tab-discounted-doctors";
    public static final String HOSPITALS_NAME_LISTING_URL = URL+"get-hospital-names";
    public static final String HOSPITALS_DOCTOR_LIST_URL = URL+"get-hospital-doctors";
    public static final String HOSPITALS_INFO_URL = URL+"get-hospital-details";
    public static final String All_LAB_URL = URL+"get-labs";
    public static final String RESET_PASSWORD = URL+"reset-password";
    public static final String DISCOUNTED_LAB_URL = URL+"get-discounted-labs";
    public static final String NEARBY_LAB_URL = URL+"get-nearby-labs";
    public static final String LAB_DETAIL_URL = URL+"get-lab-details";
    public static final String PHARMACY_DETAIL_URL = URL+"get-pharmacy-details";
    public static final String ALL_PHARMACY_URL = URL+"get-pharmacies";
    public static final String DISCOUNTED_PHARMACY_URL = URL+"get-discounted-pharmacies";
    public static final String NEAR_PHARMACY_URL = URL+"get-nearby-pharmacies";
    public static final String GET_HOS_NAME_FILTER = URL+"get-hospital-names";
    public static final String GET_DOCTOR_NAME_FILTER = URL+"get-doctors-names";
    public static final String SELECT_SPECIALITY_URL = URL+"get-specialities/"+Key;

    public static final String SIGNIN = URL + "login";
    public static final String APPEAL_BLOOD = URL + "appeal-blood";
    public static final String GET_APPEAL_BLOOD = URL + "get-blood-appeals";
    public static final String GET_SLIDES = URL + "get-slides";
    public static final String GET_EMERGENCY_CONTACT_LIST = URL + "get-emergency-contacts";

    public static final String HOME_CARE_REQUEST = URL + "home-care";
    public static final String MEDI_STUDY_SAVE_RESULT = URL + "save-result";
    public static final String MEDI_STUDY_REPORT_QUESTION = URL + "report-mcq";
    public static final String MEDI_STUDY_NOTES_QUESTION = URL + "notes-mcq";
    public static final String MEDI_STUDY_BOOKMARKED_QUESTION = URL + "bookmark-mcq";
    public static final String  GET_HOME_CARE_REQUEST = URL + "get-home-cares";


    public static final String GETDATAFORSPECIALISTY = URL + "get-data-for-specialities";
    public static final String GETALL_HOSPITALS = URL + "get-all-hospitals";
    //regisration docotor
    public static final String SIGNUP_DOCTOR = URL + "register-doctor";
    //uploading single image for doctor registration
    public static final String UPLOAD_DR_PMDC_PIC = URL + "upload-doctor-pmdc-picture";
    public static final String IMAGE_BACK_URL = "http://themedicall.com/public/uploaded/img/doctors/";
    public static final String DELETING_IMAGE = URL +"remove-doctor-gallery-img";

    //broadcast Receiver Action
    final public static String FRAGMENT_SWITCHING_ACTION = "frgSwitchAction";


    //registring blood donor
    public static final String SIGNUP_BLOOD_DONOR = URL + "register-blood-donor";
    public static final String SIGNUP_PATIENT = URL + "register-patient";
    public static final String PIN_VERIFICATION = URL + "verify-user";
    public static final String RESENT_VERIFICATION_CODE = URL + "resend-code";
    public static final String CHUGTAI_LAB_TESTS_LIST = URL + "get-lab-tests";
    public static final String CHUGTAI_LAB_QUICK_LOGIN = "http://api.cll.com.pk/api/v1/tmx/quickLogin";
    public static final String CHUGTAI_LAB_PATIENT_REPORT = "http://api.cll.com.pk/api/v1/tmx/patientReport";
    public static final String UPDATE_DOCTOR = URL+"update-doctor";

    public static final String GET_DOCTOR_FULL_DETAIL = URL + "get-doctor-full-details";
    public static final String UPLOADGALLARY_IMAGES = URL + "upload-doctor-gallery-img";
    public static final String UPLOADPROFILE_IMAGE = URL + "update-doctor-profile-image";

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 000000;
    public  static final int MY_PERMISSIONS_REQUEST_CAMERA = 000001;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 000002;

    final public static String ADDNEWHOSPITAL = URL+"add-hospital";
    final public static String MY_ACTION = "datarecived";
    public static final String AddingNewOtherSubSpeciality = URL+"add-sub-speciality";
    public static final String AddingNewOtherSubServices = URL+"add-service";
    public static final String AddingNewOtherQualification = URL+"add-qualification";
    public static final String BLOG_CATEGORIES = "http://themedicall.com/blog/wp-json/wp/v2/categories?fields=id,name";

    //calim url
    public static final String CLAI_PROFILE_MURL = URL+"claim-doctor-profile";
    //report url
    public static final String REPORT_DOCTOR = URL+"report-doctor-profile";
    public static final String ALLIED_HEALTH_SIGNUP = URL+"register-allied-health-professional";
    //alied health categories
    public static final String ALIED_HEALTH_CATEGOREIS = URL+"get-allied-health-categories";
    public static final String SIGNIN_WITH_SOCIAL = URL+"login-social";
    //user existance
    public static final String CHECK_USER_EXISTANCE = URL+"check-user-existence";
    public static final String IMAGE_URL_PATIENT = "http://themedicall.com/public/uploaded/img/patients/";
    public static final String IMAGE_URL_DONOR = "http://themedicall.com/public/uploaded/img/donors/";

    public static final String FORGOT_PASSWORD = URL+"forgot-password";
    public static final String MEDI_STUDY_GET_BOOKS = URL+"get-books";
    //MediPedia Urls
    public static final String url = "http://themedicall.com/medi-app-api/get-companies";


}
