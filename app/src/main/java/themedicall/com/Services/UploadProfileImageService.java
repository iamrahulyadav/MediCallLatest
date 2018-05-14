package themedicall.com.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import themedicall.com.Globel.Glob;
import themedicall.com.UpdateDoctorProfile;

public class UploadProfileImageService extends Service {
    public UploadProfileImageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        String imagePath = intent.getExtras().getString("imagePath", "");
        String drID = intent.getExtras().getString("drId", "");

        Log.e("TAg", "the values in the profile image server imagepath is: " + imagePath);
        Log.e("TAg", "the values in the profile image server drId is: " + drID);

        uploadMultiplart(imagePath, drID);


        return super.onStartCommand(intent, flags, startId);
    }

    private void uploadMultiplart(final String imagePath, final String drId){

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getApplicationContext(), uploadId, Glob.UPLOADPROFILE_IMAGE )
                    .addFileToUpload(imagePath, "picture") //Adding file
                    .addParameter("key", Glob.Key) //Adding text parameter to the request
                    .addParameter("doctor_id", drId)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {



                            String responseFromServer = serverResponse.getBodyAsString();
                            Log.e("TAG", "the response from server for upload image: " + serverResponse.getBodyAsString());

                            if (responseFromServer!=null) {
                                try {

                                    JSONObject jObj = new JSONObject(responseFromServer);

                                    boolean error = jObj.getBoolean("error");

                                    if (!error) {

                                        String image_url = jObj.getString("image_url");
                                        Log.e("TAG", "the uploaded image response is: " + image_url);

                                        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("profile_img", image_url);
                                        editor.commit();

                                        Intent intent = new Intent();
                                        intent.setAction("imageLoaded");
                                        intent.putExtra("loaded", 1);//sending notification that data received in service
                                        sendBroadcast(intent);
                                        //sending boradcast for image upload complete

                                        //stoping sevice
                                        stopSelf();

                                    } else {

                                        String errorMsg = jObj.getString("error_message");
                                        //Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .startUpload(); //Starting the upload



        } catch (Exception exc) {

        }
    }
}
