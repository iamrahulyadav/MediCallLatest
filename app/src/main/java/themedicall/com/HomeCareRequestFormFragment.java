package themedicall.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HomeCareRequestFormFragment extends Fragment {

    EditText name , phone , homeCareLocation ,  address , service;
    String nameStr , phoneStr , homeCareLocationStr , addressStr , serviceStr;
    Button submitBtn;
    Spinner field ;
    ListView cityListView;
    SearchView search_view;
    CustomCityNewAdapter customCityNewAdapter;
    int request = 1 ;
    String city;
    String Area;
    String Address;
    double lat;
    double lng ;
    RadioGroup homeCareRadioGroup ;
    RadioButton radioButton ;
    int radioSelectedId ;
    String radioButtonText ;
    int signInRequestCode = 200 ;
    CustomProgressDialog dialog;
    private final String TAG = "Appeal Blood";
    public HomeCareRequestFormFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_care_request_form, container, false);

        initiate(view);
        clickListener();
        return view ;
    }

    public void initiate(final View view)
    {

        dialog=new CustomProgressDialog(getActivity(), 1);
        name = (EditText) view.findViewById(R.id.home_care_name);
        phone = (EditText) view.findViewById(R.id.home_care_phone);
        address = (EditText) view.findViewById(R.id.home_care_address);
        service = (EditText) view.findViewById(R.id.home_care_service);

        homeCareLocation = (EditText) view.findViewById(R.id.homeCareLocation);
        homeCareRadioGroup = (RadioGroup) view.findViewById(R.id.homeCareRadioGroup);

        homeCareRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                radioSelectedId = homeCareRadioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) view.findViewById(radioSelectedId);
                    radioButtonText = (String) radioButton.getText();
            }
        });




        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null) {

            String userId = sharedPreferences.getString("userid", null);
            if (userId != null) {
                String userFullName = sharedPreferences.getString("userfullname", null);
                String profile_img = sharedPreferences.getString("profile_img", null);

                final String PROFILE_IMAGE_URL = Glob.IMAGE_BACK_URL + profile_img;
                Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);

                name.setText(userFullName);
                name.setFocusable(false);
                name.setClickable(false);

            }
            else
            {
                name.setText("");
                name.setFocusable(true);
                name.setClickable(true);
            }
        }

        submitBtn = (Button) view.findViewById(R.id.home_care_submitBtn);


    }



    public void clickListener()
    {

        homeCareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewHospitalActivity = new Intent(getActivity(), MapForAppealBlood.class);
                startActivityForResult(createNewHospitalActivity , request);
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameStr = name.getText().toString();
                phoneStr = phone.getText().toString();
                homeCareLocationStr = homeCareLocation.getText().toString();
                addressStr = address.getText().toString();
                serviceStr = service.getText().toString();


                if(nameStr.equals(""))
                {
                    name.setError("Please enter name");
                }
                else if(phoneStr.length() < 11)
                {
                    phone.setError("Please enter complete phone no");
                }
                else if(homeCareLocationStr.equals(""))
                {
                    Toast.makeText(getActivity(), "Please select location", Toast.LENGTH_SHORT).show();
                }
                else if(addressStr.equals(""))
                {
                    address.setError("Please enter address");
                }
                else if(homeCareRadioGroup.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(getActivity(), "Please checked any care required", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Log.e("tag" , "home care name : "+nameStr);
                    Log.e("tag" , "home care phone : "+phoneStr);
                    Log.e("tag" , "home care google address: "+homeCareLocationStr);
                    Log.e("tag" , "home care address : "+addressStr);
                    Log.e("tag" , "home care required : "+radioButtonText);
                    Log.e("tag" , "home care service required : "+serviceStr);

                    checkLogin();

                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == request)
        {
            if(data == null)
            {
                Log.e("tag" , "data is null in onActivity Result for map");
            }
            else
            {
                city = data.getStringExtra("City");
                Area = data.getStringExtra("Area");
                Address = data.getStringExtra("Address");
                lat = data.getDoubleExtra("lat"  , 0.0);
                lng = data.getDoubleExtra("lng" , 0.0);

                homeCareLocation.setText(Address);

                Log.e("tag" , "onActivityResult City : " + city);
                Log.e("tag" , "onActivityResult Area : " + Area);
                Log.e("tag" , "onActivityResult Address : " + Address);
                Log.e("tag" , "onActivityResult lat : " + lat);
                Log.e("tag" , "onActivityResult lng : " + lng);
            }

        }
    }

    public void checkLogin()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){

            String userId = sharedPreferences.getString("userid", null);
            if (userId!=null){
                String userFullName = sharedPreferences.getString("userfullname", null);
                String profile_img = sharedPreferences.getString("profile_img", null);

                final String PROFILE_IMAGE_URL = Glob.IMAGE_BACK_URL+profile_img;
                Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);

                homeCare(userId);


            }
            else {

                Intent intent = new Intent(getActivity() , SignIn.class);
                startActivityForResult(intent , signInRequestCode);
            }
        }
    }

    public void homeCare(final String userId)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "home care request";

        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.HOME_CARE_REQUEST , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "home care request Response : " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getActivity() , "Request for home care submit Successfully!", Toast.LENGTH_SHORT).show();



                    } else {

                        String errorMsg = jObj.getString("error_message");

                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "home care request Error: " + error.getMessage());
                Toast.makeText(getActivity(), R.string.internetConnection, Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Log.e("tag" , "user id " +userId);


                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
                params.put("user_id", userId);
                params.put("name", nameStr);
                params.put("contact", phoneStr);
                params.put("google_address", homeCareLocationStr);
                params.put("home_address", addressStr);
                params.put("care_required", radioButtonText);
                params.put("service_required", serviceStr);
                params.put("city", city);
                params.put("area", Area);
                params.put("lat", String.valueOf(lat));
                params.put("lng", String.valueOf(lng));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service


}
