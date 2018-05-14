package themedicall.com.Adapter;

import android.Manifest;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import themedicall.com.GetterSetter.BloodReqiredGetterSetter;
import themedicall.com.R;


import java.util.List;

import static android.Manifest.permission.SEND_SMS;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class BloodRequiresRecycleView extends RecyclerView.Adapter<BloodRequiresRecycleView.MyViewHolder> {

    private List<BloodReqiredGetterSetter> bloodRequiredList;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 200;
    private static final int PERMISSION_REQUEST = 100;

    Context context ;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView BloodRequiredPhoneImg, BloodRequiredMessageImg;
        public TextView bloodRequiredId, bloodRequiredGroup, bloodRequiredName, bloodRequiredCity, bloodRequiredPhoneNumber, bloodRequiredDescription;
        private int REQUEST_PHONE_CALL = 23;
        String phone;


        public MyViewHolder(final View view) {
            super(view);

            BloodRequiredPhoneImg = (ImageView) view.findViewById(R.id.bloodRequiredPhoneImg);
            BloodRequiredMessageImg = (ImageView) view.findViewById(R.id.bloodRequiredMesageImg);
            bloodRequiredId = (TextView) view.findViewById(R.id.bloodRequiredId);
            bloodRequiredGroup = (TextView) view.findViewById(R.id.bloodRequiredGroup);
            bloodRequiredName = (TextView) view.findViewById(R.id.bloodRequiredName);
            bloodRequiredCity = (TextView) view.findViewById(R.id.bloodRequiredCity);
            bloodRequiredPhoneNumber = (TextView) view.findViewById(R.id.bloodRequiredPhoneNumber);
            bloodRequiredDescription = (TextView) view.findViewById(R.id.bloodRequiredDescription);


            BloodRequiredPhoneImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    phone = bloodRequiredPhoneNumber.getText().toString();
                    // Toast.makeText(v.getContext(), "id "+phone, Toast.LENGTH_SHORT).show();

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);

                        return;

                    }

                    v.getContext().startActivity(callIntent);


                }
            });

            BloodRequiredMessageImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phone = bloodRequiredPhoneNumber.getText().toString();
                    String bloodGroup = bloodRequiredGroup.getText().toString();
                    sendMessage(v , phone, bloodGroup);
                   // Toast.makeText(v.getContext(), "Message ", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public BloodRequiresRecycleView(Context context  , List<BloodReqiredGetterSetter> adList) {
        this.bloodRequiredList = adList;
        this.context = context ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        Log.e("TAg", "the view type : " + viewType);

        switch (viewType) {
            case ITEM:

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_required_custom_row, parent, false);
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


                    BloodReqiredGetterSetter ad = bloodRequiredList.get(position);
                    holder.bloodRequiredId.setText(ad.getBloodRequiredId());
                    holder.bloodRequiredGroup.setText(ad.getBloodRequiredGroup());
                    holder.bloodRequiredName.setText(ad.getBloodRequiredName());
                    holder.bloodRequiredCity.setText(ad.getBloodRequiredCity());
                    holder.bloodRequiredPhoneNumber.setText(ad.getBloodRequiredPhoneNumber());
                    holder.bloodRequiredDescription.setText(ad.getBloodRequiredDescription());

                    String instrucion = holder.bloodRequiredDescription.getText().toString();
                    if (instrucion.equals("null")) {
                        holder.bloodRequiredDescription.setVisibility(View.GONE);
                    } else {
                        holder.bloodRequiredDescription.setVisibility(View.VISIBLE);
                    }

                    holder.bloodRequiredId.setVisibility(View.GONE);


                    break;

                case LOADING:
                    //do Nothing
                    break;
            }
        }

    }


    @Override
    public int getItemCount() {
        return bloodRequiredList.size();
    }


    @Override
    public int getItemViewType(int position) {


        return (position == bloodRequiredList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public BloodReqiredGetterSetter getItem(int position) {
        return bloodRequiredList.get(position);
    }

    public void add(BloodReqiredGetterSetter mc) {
        bloodRequiredList.add(mc);
        notifyItemInserted(bloodRequiredList.size() - 1);
    }

    public void addAll(List<BloodReqiredGetterSetter> mcList) {
        for (BloodReqiredGetterSetter mc : mcList) {
            add(mc);
        }
    }

    public void sendMessage(View v, final String phone, final String bloodGroup)
    {
        String[] bloodUrgencyArray = { "Urgency" ,"Within 1 day" , "Within 2 days" , "Within 3 days" , "Within 4 days" , "Within 5 days" , "Within 6 days" , "Within 1 week"};
        ArrayAdapter<String> bloodUrgencyAdapter;

        final Dialog dialog = new Dialog(v.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_blood_donor_message_dialog);
        dialog.show();
        final String text = "Helped by Medicall. Don't pay for blood. ";
        final EditText messageContact = (EditText) dialog.findViewById(R.id.messageContact);
        EditText messageBloodGroup = (EditText) dialog.findViewById(R.id.messageBloodGroup);
        final EditText messageRequiredAt = (EditText) dialog.findViewById(R.id.messageRequiredAt);
        final Spinner messageUrgency = (Spinner) dialog.findViewById(R.id.messageUrgency);

        messageContact.setText(phone);
        messageContact.setClickable(false);
        messageContact.setFocusable(false);

        messageBloodGroup.setText(bloodGroup);
        messageBloodGroup.setClickable(false);
        messageBloodGroup.setFocusable(false);

        EditText messageText = (EditText) dialog.findViewById(R.id.messageText);
        messageText.setText(text);
        messageText.setClickable(false);
        messageText.setFocusable(false);


        TextView messageCancel = (TextView) dialog.findViewById(R.id.messageCancel);
        TextView messageSend = (TextView) dialog.findViewById(R.id.messageSend);


        bloodUrgencyAdapter = new ArrayAdapter<String>(context , R.layout.spinner_list , R.id.spinnerList , bloodUrgencyArray);
        messageUrgency.setAdapter(bloodUrgencyAdapter);


        messageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String spinnerText = messageUrgency.getSelectedItem().toString();
                String requiredAtText = messageRequiredAt.getText().toString();

                if(spinnerText.equals("Urgency"))
                {
                    Toast.makeText(context, "Please select urgency", Toast.LENGTH_SHORT).show();
                }
                else if(requiredAtText.equals(""))
                {
                    Toast.makeText(context, "Please write where you blood need", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String message = bloodGroup + " Required at " + requiredAtText +" "+spinnerText+"." + "\n" + text;

                    Log.e("tag" , "final message is : "+message);

                    sendSMSAfterPermission(phone , message , view , dialog);

                }

                //dialog.dismiss();
            }

        });
    }



    public void sendSMSAfterPermission(String phoneNo, String msg, View view, Dialog dialog) {


        if(checkPermission())
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null,msg, null, null);
            Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            return;
        }
        else
        {
            requestPermission();
        }



    }

    public boolean checkPermission() {

        int smsPermissionResult = ContextCompat.checkSelfPermission(context , SEND_SMS);

        return smsPermissionResult == PackageManager.PERMISSION_GRANTED ;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions((Activity) context, new String[]
                {
                        SEND_SMS
                }, PERMISSION_REQUEST);

    }
}
