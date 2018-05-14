package themedicall.com.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import themedicall.com.GetterSetter.EmergencyContactGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class EmergencyContactRecycleView extends RecyclerView.Adapter<EmergencyContactRecycleView.MyViewHolder>  {
    private int REQUEST_PHONE_CALL = 23;
    private Context mContext;
    private List<EmergencyContactGetterSetter> emergencyContactList;




    public EmergencyContactRecycleView(Context context, List<EmergencyContactGetterSetter> adList) {
        this.emergencyContactList = adList;
        this.mContext = context ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView emergencyContactImage , emergency_contacts_call_button;
        public TextView emergencyContactId , emergencyContactServiceName , emergencyContactPhoneNumber;




        public MyViewHolder(final View view) {
            super(view);
            emergencyContactImage = (ImageView) view.findViewById(R.id.emergency_contacts_image);
            emergency_contacts_call_button = (ImageView) view.findViewById(R.id.emergency_contacts_call_button);
            emergencyContactId = (TextView) view.findViewById(R.id.emergency_contacts_id);
            emergencyContactPhoneNumber = (TextView) view.findViewById(R.id.emergency_contacts_phoneNumber);
            emergencyContactServiceName = (TextView) view.findViewById(R.id.emergency_contacts_name);


            emergency_contacts_call_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = emergencyContactPhoneNumber.getText().toString();
                    //Toast.makeText(v.getContext(), "Phone Number "+phone, Toast.LENGTH_SHORT).show();
                    callToEmergencyNum(phone);
                }
            });
        }
    }



    public void callToEmergencyNum(String phone)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone));
        if (ContextCompat.checkSelfPermission(mContext , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

            return;

        }

        mContext.startActivity(callIntent);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emergency_contact_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EmergencyContactGetterSetter ad = emergencyContactList.get(position);
        Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL + "emergency_contacts/" + emergencyContactList.get(position).getEmergencyContactImag()).transform(new CircleTransformPicasso()).into(holder.emergencyContactImage);
        holder.emergencyContactId.setText(ad.getEmergencyContactId());
        holder.emergencyContactPhoneNumber.setText(ad.getEmergencyContactPhoneNumber());
        holder.emergencyContactServiceName.setText(ad.getEmergencyContactServiceName());


        holder.emergencyContactId.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return emergencyContactList.size();
    }

}
