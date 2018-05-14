package themedicall.com.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import themedicall.com.GetterSetter.BloodAppealsGetterSetter;
import themedicall.com.R;

import java.util.List;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.SEND_SMS;

/**
 * Created by Muhammad Adeel on 2/13/2018.
 */

public class BloodAppealAdapter extends RecyclerView.Adapter<BloodAppealAdapter.MyViewHolder>  {

    private List<BloodAppealsGetterSetter> bloodAppealList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public static final int REQUEST_PERMISSION_CODE = 300;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView bloodAppealMessage , bloodAppeaCall , bloodAppealShare ;
        public TextView bloodAppealId , bloodAppealBullet ,  bloodAppealName , bloodAppealGroup , bloodAppealDistance , bloodAppealDescription , bloodAppealPhone , bloodAppealDetail , bloodAppealStatus , bloodAppealHours , bloodAppealGoogleAddress;

        public MyViewHolder(final View view) {
            super(view);


            bloodAppealMessage = (ImageView) view.findViewById(R.id.bloodAppealMessage);
            bloodAppeaCall = (ImageView) view.findViewById(R.id.bloodAppeaCall);
            bloodAppealShare = (ImageView) view.findViewById(R.id.bloodAppealShare);
            bloodAppealId = (TextView) view.findViewById(R.id.bloodAppealId);
            bloodAppealBullet = (TextView) view.findViewById(R.id.bloodAppealBullet);
            bloodAppealName = (TextView) view.findViewById(R.id.bloodAppealRequestName);
            bloodAppealGroup = (TextView) view.findViewById(R.id.bloodAppealGroup);
            bloodAppealDistance = (TextView) view.findViewById(R.id.bloodAppealDistance);
            bloodAppealDescription = (TextView) view.findViewById(R.id.bloodAppealDescription);
            bloodAppealPhone = (TextView) view.findViewById(R.id.bloodAppealPhone);
            bloodAppealDetail = (TextView) view.findViewById(R.id.bloodAppealDetail);
            bloodAppealStatus = (TextView) view.findViewById(R.id.bloodAppealStatus);
            bloodAppealHours = (TextView) view.findViewById(R.id.bloodAppealHours);
            bloodAppealGoogleAddress = (TextView) view.findViewById(R.id.bloodAppealGoogleAddress);




            bloodAppealMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String phone = bloodAppealPhone.getText().toString();

                    sendMessage(view , phone);
                }
            });

            bloodAppeaCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String phone = bloodAppealPhone.getText().toString();

                    if(checkPermission())
                    {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+phone));
                        mContext.startActivity(callIntent);

                        return;
                    }
                    else
                    {
                        requestPermission();
                    }
                }
            });


        }
    }



    public void sendMessage(View v, final String phone)
    {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_blood_appeal_message_dialog);
        dialog.show();
        final String text = "I want to donate blood. kindly contact on my number it is absolutely free. \nHelped by Medicall.";

        EditText messageBloodAppealContact = (EditText) dialog.findViewById(R.id.messageBloodAppealContact);
        messageBloodAppealContact.setText(phone);
        messageBloodAppealContact.setClickable(false);
        messageBloodAppealContact.setFocusable(false);

        EditText messageBloodAppealText = (EditText) dialog.findViewById(R.id.messageBloodAppealText);
        messageBloodAppealText.setText(text);
        messageBloodAppealText.setClickable(false);
        messageBloodAppealText.setFocusable(false);

        TextView messageBloodAppealCancel = (TextView) dialog.findViewById(R.id.messageBloodAppealCancel);
        TextView messageBloodAppealSend = (TextView) dialog.findViewById(R.id.messageBloodAppealSend);

        messageBloodAppealCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        messageBloodAppealSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendSMSAfterPermission(phone , text , view , dialog);
                //dialog.dismiss();
            }

        });
    }

    public void sendSMSAfterPermission(String phoneNo, String msg, View view, Dialog dialog) {


        if (checkPermission()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(mContext, "SMS sent.", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            return;
        } else {
            requestPermission();
        }

    }

    public BloodAppealAdapter(Context context , List<BloodAppealsGetterSetter> appealList) {
        this.mContext = context;
        this.bloodAppealList = appealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.pharmacy_custom_row, parent, false);
//        return new MyViewHolder(itemView);

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        switch (viewType) {
            case ITEM:
                // viewHolder = getViewHolder(parent, inflater);

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_appeals_custom_row, parent, false);
                viewHolder = new MyViewHolder(itemView);

                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.progress_item_at_end, parent, false);
                viewHolder = new MyViewHolder(v2);
                break;

        }




        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {


            switch (getItemViewType(position)) {

                case ITEM:
                    BloodAppealsGetterSetter ad = bloodAppealList.get(position);
                    holder.bloodAppealId.setText(ad.getId());
                    holder.bloodAppealName.setText("By " + ad.getName());
                    holder.bloodAppealDistance.setText(ad.getDistance() +" KM");
                    holder.bloodAppealDescription.setText(ad.getDescription());
                    holder.bloodAppealPhone.setText(ad.getContactNo());
                    holder.bloodAppealDetail.setText(ad.getDetail());
                    holder.bloodAppealHours.setText("about " +ad.getTimeToAgo());
                    holder.bloodAppealGoogleAddress.setText(ad.getGoogleAddress());


                    if(ad.getStatus().contains("Arranged"))
                    {
                        holder.bloodAppealStatus.setText(ad.getStatus());
                        holder.bloodAppealBullet.setText("\u25CF");
                        holder.bloodAppealBullet.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                    }
                    else
                    {
                        holder.bloodAppealStatus.setText(ad.getStatus());
                        holder.bloodAppealBullet.setText("\u25CF");
                        holder.bloodAppealBullet.setTextColor(ContextCompat.getColor(mContext, R.color.timingBackground));
                    }


                    if(ad.getBloodGroup().contains("Not Provided"))
                    {
                        holder.bloodAppealGroup.setText("B+");
                    }
                    else
                    {
                        holder.bloodAppealGroup.setText(ad.getBloodGroup());

                    }

                    break;
                case LOADING:
                    //do Nothing
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return bloodAppealList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == bloodAppealList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public BloodAppealsGetterSetter getItem(int position) {
        return bloodAppealList.get(position);
    }

    public void add(BloodAppealsGetterSetter mc) {
        bloodAppealList.add(mc);
        notifyItemInserted(bloodAppealList.size() - 1);
    }

    public void addAll(List<BloodAppealsGetterSetter> mcList) {
        for (BloodAppealsGetterSetter mc : mcList) {
            add(mc);
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions((Activity) mContext, new String[]
                {
                        CALL_PHONE,
                        SEND_SMS
                }, REQUEST_PERMISSION_CODE);

    }

    public boolean checkPermission() {

        int phonePermissionResult = ContextCompat.checkSelfPermission(mContext , CALL_PHONE);
        int smsPermissionResult = ContextCompat.checkSelfPermission(mContext , SEND_SMS);

        return phonePermissionResult == PackageManager.PERMISSION_GRANTED &&
                smsPermissionResult == PackageManager.PERMISSION_GRANTED ;

    }






}