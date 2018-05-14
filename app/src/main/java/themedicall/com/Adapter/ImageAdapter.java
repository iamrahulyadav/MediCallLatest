package themedicall.com.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import themedicall.com.BasicInfoUpdateDocProfile;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.R;import themedicall.com.VolleyLibraryFiles.AppSingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User-10 on 07-Dec-17.
 */


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<CitiesGetterSetter> fetchList;
    private static LayoutInflater inflater=null;
    int mIndicator;
    int rotatevalue = 0;
    int currentPos = -1;
    int prePosition = -2;

    ProgressDialog progressDialog;
    JSONObject jsonObject;
    JSONArray jsonArray;

    String arrValue = "";

    HashMap<Integer, Matrix> mImageTransforms = new HashMap<Integer,Matrix>();
    Matrix mIdentityMatrix = new Matrix();
    CustomProgressDialog customProgressDialog;

    public ImageAdapter(Context c, ArrayList<CitiesGetterSetter> fetchList1, int indicator) {
        mContext = c;
        fetchList = fetchList1;
        mIndicator = indicator;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {

        return fetchList.size();

    }

    public Object getItem(int position) {
        return null;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        View vi=convertView;




        if(convertView==null)

            vi = inflater.inflate(R.layout.grid_image_list, null);

        final ImageView image = (ImageView)vi.findViewById(R.id.image);
        ImageView cross = (ImageView)vi.findViewById(R.id.cross);
        progressDialog = new ProgressDialog(mContext);
        customProgressDialog = new CustomProgressDialog(mContext, 1);

        // Bitmap b = BitmapFactory.decodeFile(fetchList.get(position));
        //Log.e("TAG", "IMAGE BITMAP: " + b);

//            image.setImageBitmap(BitmapFactory.decodeFile(fetchList.get(position)));

        final BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = false;
        // options.inSampleSize = -10;

        //options.inPurgeable = true;
        options.outHeight = 40;
        options.outWidth = 40;
        //options.inSampleSize = 4;



        if (mIndicator == 1){

            Picasso.with(mContext)
                    .load(fetchList.get(position).getName())
                    .placeholder(R.drawable.add_image)
                    //.error(R.drawable.)
                    //.override(400, 400)
                    .centerCrop()
                    .into(image);

        }
        else {



            try {
                Bitmap bm = BitmapFactory.decodeFile(fetchList.get(position).getName(), options);
                image.setImageBitmap(bm);
            }catch (OutOfMemoryError e){
                e.printStackTrace();
            }

        }
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(mContext);
                deleteAlert.setMessage("Do You Want to remove Photo");
                deleteAlert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                deleteAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        String imageId = fetchList.get(position).getId().toString();
                        Log.e("TAG", "The Image Id Onclick is: " + imageId);

                        if (!imageId.equals("0")) {
                            deletingImage(position, imageId);
                        }else {
                            fetchList.remove(position);
                        }
                        // fetchList.remove(position);
                        notifyDataSetChanged();
                        if(fetchList.size()<10) {
                            BasicInfoUpdateDocProfile.ivSelectMulitiPhoto.setVisibility(View.VISIBLE);
                        }

                    }
                });

                deleteAlert.show();


            }
        });
        if (mIndicator == 1){
            //do something if image is from server
        }
        else {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Roate Image");
                    alertDialog.setMessage("Rote Image to right, left or down Using Given Buttons");
                    alertDialog.setPositiveButton("To Left", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            performRotation(image, position, 270, options);
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.setNegativeButton("To Right", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            performRotation(image, position, 90, options);
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.setNeutralButton("To Down", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            performRotation(image, position, 180, options);
                            dialogInterface.dismiss();
                        }
                    });

                    //alertDialog.show();


                }
            });
        }

        return vi;

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    public Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

    private void rotate(ImageView imgview, float degree) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(2000);
        rotateAnim.setFillAfter(true);
        imgview.startAnimation(rotateAnim);
    }

/*    private String getOrientation(String uri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        String orientation = "landscape";
        try{
            String image = new File(uri).getAbsolutePath();
            BitmapFactory.decodeFile(image, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            if (imageHeight > imageWidth){
                orientation = "portrait";
            }
        }catch (Exception e){
            //Do nothing
        }
        return orientation;
    }*/

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 512, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void performRotation(ImageView imageView, int position, float angle, BitmapFactory.Options options){
        Bitmap bmp = BitmapFactory.decodeFile(fetchList.get(position).getName(), options);
        Log.e("TAg", "befor bitmap: " + bmp);
        Bitmap bb = RotateBitmap(bmp, angle);
        imageView.setImageBitmap(bb);
        Log.e("TAg", "after bitmap: " + bb);
        Uri ii = getImageUri(mContext, bb);
        Log.e("TAg", "urii: " + ii);
        String presentPath = getRealPathFromURI(ii, mContext);
        Log.e("TAg", "prev path: " + fetchList.get(position).toString());
        Log.e("TAg", "current path: " + presentPath);

        // fetchList.set(position, presentPath);

    }

    public void deletingImage(int position, String imageId){

        serverForDeletingImage(position,imageId);

    }
    public void serverForDeletingImage(final int position, final String imgageId)
    {
        Log.d("tag" , "image pos in start of service: "+position);

        // Tag used to cancel the request
        String cancel_req_tag = "register";


        progressDialog.setMessage("Please Wait ...");
        if (!progressDialog.isShowing()){
            //progressDialog.show();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.DELETING_IMAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());
                if (progressDialog.isShowing()){
                    // progressDialog.dismiss();
                }


                try {

                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAg", "the Delete: " + jObj);
                    boolean error = jObj.getBoolean("error");
                    Log.e("TAg", "the Delete error: " + error);

                    if (!error) {

                        String isDeleted = jObj.getString("error_message");

                        if (isDeleted.equals("Removed")) {
                            Log.e("TAg", "isRemoved : " + isDeleted);
                            fetchList.remove(position);
                            notifyDataSetChanged();
                            Log.d("tag" , "image pos in condition: "+position);

                        }


                        //progressDialog.dismiss();
                        Toast.makeText(mContext , "Image Deleted Successfully!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                if (progressDialog.isShowing()) {
                    // progressDialog.dismiss();
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("img_id", imgageId);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(mContext).addToRequestQueue(strReq, cancel_req_tag);
    }




}