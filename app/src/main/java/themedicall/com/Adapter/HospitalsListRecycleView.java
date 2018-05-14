package themedicall.com.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import themedicall.com.GetterSetter.HospitalsGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.HospitalDetail;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class HospitalsListRecycleView extends RecyclerView.Adapter<HospitalsListRecycleView.MyViewHolder>  {

    HospitalsLandLineListRecycleView hospitalsLandLineListRecycleView;
    private List<HospitalsGetterSetter> hospitalsList;
    private Context mContext;
    ArrayAdapter<String> cityAdapter;
    private int REQUEST_PHONE_CALL = 23;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean isIdFojnd = false;


    public HospitalsListRecycleView(Context context, List<HospitalsGetterSetter> adList) {
        this.hospitalsList = adList;
        this.mContext = context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView hospitalProfileImg , doctorCallBtn , hospitalDocLine , doctorRowDiscountedImg;
        public TextView hospitalId , hospitalProfileName , hospitalProfileAddress , hospitalProfileNoOfDoc , hospitalKm , hospitalRowProfileLink , hospitalRowNoOfViews , doctorRowDiscounted;
        ArrayList allDocList ;
        ArrayList landLine;
        public RecyclerView recyclerView;
        LinearLayout hospitalShareLayout ;

        public MyViewHolder(final View view) {
            super(view);

//


            doctorCallBtn = (ImageView) view.findViewById(R.id.doctorCallBtn);
            doctorRowDiscountedImg = (ImageView) view.findViewById(R.id.doctorRowDiscountedImg);
            hospitalProfileImg = (ImageView) view.findViewById(R.id.hospitalRowProfileImg);
            hospitalDocLine = (ImageView) view.findViewById(R.id.hospitalDocLine);
            hospitalId = (TextView) view.findViewById(R.id.hospitalId);
            hospitalProfileName = (TextView) view.findViewById(R.id.hospitalRowName);
            hospitalProfileAddress = (TextView) view.findViewById(R.id.hospitalRowAddress);
            hospitalProfileNoOfDoc = (TextView) view.findViewById(R.id.hospitalRowNoOfDoc);
            hospitalRowProfileLink = (TextView) view.findViewById(R.id.hospitalRowProfileLink);
            hospitalRowNoOfViews = (TextView) view.findViewById(R.id.hospitalRowNoOfViews);
            doctorRowDiscounted = (TextView) view.findViewById(R.id.doctorRowDiscounted);
            hospitalShareLayout = (LinearLayout) view.findViewById(R.id.hospitalShareLayout);
            hospitalKm = (TextView) view.findViewById(R.id.hospitalKm);
            landLine = new ArrayList();
            allDocList = new ArrayList();
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_hospitals_doc_list);


            doctorCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(landLine.size() == 0)
                    {
                        Toast.makeText(mContext, "No Contact detail found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        LandLineListDialog(landLine);
                    }
                }
            });


            hospitalShareLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String hospitalProfileLink = hospitalRowProfileLink.getText().toString();
                    //Toast.makeText(view.getContext(), "share able link : "+hospitalProfileLink, Toast.LENGTH_SHORT).show();

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBodyText = "View Hospital Profile click on this link \n" ;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"MediCall");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText + hospitalProfileLink);
                    view.getContext().startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

                }
            });



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(mContext, "land Line number "+landLine.toString(), Toast.LENGTH_SHORT).show();


                    hospitalProfileImg.buildDrawingCache();
                    String hospital_id = hospitalId.getText().toString();
                    Bitmap hosImgBitmap = hospitalProfileImg.getDrawingCache();
                    String hosName = hospitalProfileName.getText().toString();
                    String hosAddress = hospitalProfileAddress.getText().toString();
                    String hosNoOfDoc = hospitalProfileNoOfDoc.getText().toString();
                    String hosKm = hospitalKm.getText().toString();
                   // Toast.makeText(mContext, "land line array "+ landLine.toString(), Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(v.getContext() , HospitalDetail.class);
                    intent.putExtra("hospital_id" , hospital_id);
                    intent.putExtra("hosImgBitmap" , hosImgBitmap);
                    intent.putExtra("hosName" , hosName);
                    intent.putExtra("hosAddress" , hosAddress);
                    intent.putExtra("hosNoOfDoc" , hosNoOfDoc);
                    intent.putExtra("hosAdapterKM" , hosKm);
                    intent.putParcelableArrayListExtra("landLineArray" , landLine);
                    v.getContext().startActivity(intent);

                }
            });

        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());



        Log.e("TAg", "the view type : " + viewType);

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);


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

        HospitalsGetterSetter ad = hospitalsList.get(position);

        switch (getItemViewType(position)) {

            case ITEM:

                Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL + "hospitals/" + hospitalsList.get(position).getHospitalProfileImg()).transform(new CircleTransformPicasso()).into(holder.hospitalProfileImg);
                holder.hospitalId.setText(ad.getHospitalId());
                holder.hospitalProfileName.setText(ad.getHospitalProfileName());
                holder.hospitalProfileAddress.setText(ad.getHospitalProfileAddress());
                holder.hospitalProfileNoOfDoc.setText(ad.getHospitalProfileNoOfDoc());
                holder.hospitalKm.setText(ad.getHospitalKm() + " KM");
                holder.landLine = hospitalsList.get(position).getLandLineList();
                holder.allDocList = hospitalsList.get(position).getAllDocList();
                holder.hospitalRowNoOfViews.setText(ad.getHospitalProfileNoOfViews());
                holder.hospitalRowProfileLink.setText(ad.getHospitalProfileShareLink());
                holder.doctorRowDiscounted.setText(ad.getHospitalProfileDiscount());


                if(ad.getHospitalProfileDiscount().contains("Yes"))
                {
                    holder.doctorRowDiscountedImg.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.doctorRowDiscountedImg.setVisibility(View.GONE);
                }


                if(holder.allDocList.size() == 0)
                {
                    holder.hospitalDocLine.setVisibility(View.GONE);
                }


                HospitalMultipleDocRecycleView hospitalMultipleDocRecycleView = new HospitalMultipleDocRecycleView(mContext, holder.allDocList  , ad.getHospitalKm());
                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(hospitalMultipleDocRecycleView);
                holder.recyclerView.setNestedScrollingEnabled(false);


                if (holder.allDocList.isEmpty()) {
                    holder.recyclerView.setVisibility(View.GONE);
                } else {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                }


                holder.hospitalId.setVisibility(View.GONE);

                break;

            case LOADING:
                //do Nothing
                break;
        }
    }

    @Override
    public int getItemCount() {
        return hospitalsList.size();
    }

    @Override
    public int getItemViewType(int position) {


        return (position == hospitalsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void LandLineListDialog(ArrayList landLine)
    {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.custom_citylist_search);
        dialog.setTitle("Phone Numbers");
        ListView cityList = (ListView) dialog.findViewById(R.id.cityList);
        SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        search_view.setVisibility(View.GONE);
        Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
        bt_dilaog_done.setVisibility(View.GONE);
        dialog.show();


        hospitalsLandLineListRecycleView = new HospitalsLandLineListRecycleView(mContext , landLine);
        cityList.setAdapter(hospitalsLandLineListRecycleView);
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView = (TextView) view.findViewById(R.id.landLineList);
                String phoneNumber = textView.getText().toString();
                //Toast.makeText(mContext, "phone number "+phoneNumber, Toast.LENGTH_SHORT).show();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));
                if (ContextCompat.checkSelfPermission(mContext , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

                    return;

                }

                mContext.startActivity(callIntent);

                dialog.dismiss();

            }
        });


    }



    //
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void add(HospitalsGetterSetter mc) {
        hospitalsList.add(mc);
        notifyItemInserted(hospitalsList.size() - 1);
    }



    public void addLoadingFooter() {
        isLoadingAdded = true;
        notifyDataSetChanged();
        add(new HospitalsGetterSetter());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = hospitalsList.size() - 1;
        HospitalsGetterSetter item = getItem(position);

        if (item != null) {
            hospitalsList.remove(position);

        }
    }

    public HospitalsGetterSetter getItem(int position) {
        return hospitalsList.get(position);
    }



    public void addAll(List<HospitalsGetterSetter> mcList) {
        for (HospitalsGetterSetter mc : mcList) {
            add(mc);
        }
    }


    @NonNull
    private MyViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        MyViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.hospitals_custom_row, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }




}
