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
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import themedicall.com.DoctorDetail;
import themedicall.com.GetterSetter.FindDoctorGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class FindDoctorListRecycleView extends RecyclerView.Adapter<FindDoctorListRecycleView.MyViewHolder>  {

    ArrayAdapter<String> cityAdapter;
    HospitalsLandLineListRecycleView hospitalsLandLineListRecycleView;
    private int REQUEST_PHONE_CALL = 23;
    private List<FindDoctorGetterSetter> DocList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean isIdFojnd = false;

    public FindDoctorListRecycleView( Context context , List<FindDoctorGetterSetter> adList  ) {
        this.DocList = adList;
        this.mContext = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView doctorRowProfileImg , doctorRowPhoneImg , doctorRowDiscountedImg  ,doctorRowThumbsDownImg ,  doctorRowShareProfile , verifiedDoc , doctorHospitalLine;
        public TextView doctorRowId , doctorRowName , doctorRowSpeciality , doctorRowDegree , doctorRowFees , doctorRowDistance
                , doctorRowClaimProfile , doctorRowNoOfThumbsUp , doctorRowNoOfThumbsDown  , doctorRowExperience , doctorRowDiscounted , doctorRowShareLink , doctorRowRatingtext , doctorRowNoOfViews;
        public RecyclerView recyclerView;
        RatingBar doctorRowRating ;
        ArrayList allhospitalList ;
        LinearLayout docBottomLayout ,  shareLayout ;
        private ArrayList degreeList ;
        ArrayList landLine;


        public MyViewHolder(final View view) {
            super(view);


            doctorHospitalLine = (ImageView) view.findViewById(R.id.doctorHospitalLine);
            doctorRowProfileImg = (ImageView) view.findViewById(R.id.doctorRowProfileImg);
            doctorRowPhoneImg = (ImageView) view.findViewById(R.id.doctorRowPhone);
            doctorRowDiscountedImg = (ImageView) view.findViewById(R.id.doctorRowDiscountedImg);
            verifiedDoc = (ImageView) view.findViewById(R.id.verifiedDoc);
            doctorRowId = (TextView) view.findViewById(R.id.doctorRowId);
            doctorRowDiscounted = (TextView) view.findViewById(R.id.doctorRowDiscounted);
            doctorRowExperience = (TextView) view.findViewById(R.id.doctorRowExperience);
            doctorRowName = (TextView) view.findViewById(R.id.doctorRowName);
            doctorRowSpeciality = (TextView) view.findViewById(R.id.doctorRowSpeciality);
            doctorRowDegree = (TextView) view.findViewById(R.id.doctorRowDegree);
            doctorRowFees = (TextView) view.findViewById(R.id.doctorRowFees);
            doctorRowDistance = (TextView) view.findViewById(R.id.doctorRowDistance);
            doctorRowClaimProfile = (TextView) view.findViewById(R.id.doctorRowClaimProfile);
            doctorRowNoOfThumbsUp = (TextView) view.findViewById(R.id.doctorRowNoOfThumbUp);
            doctorRowShareLink = (TextView) view.findViewById(R.id.doctorRowShareLink);
            doctorRowRatingtext = (TextView) view.findViewById(R.id.doctorRowRatingtext);
            doctorRowNoOfViews = (TextView) view.findViewById(R.id.doctorRowNoOfViews);
            degreeList = new ArrayList();
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_hospitals_list);
            docBottomLayout = (LinearLayout) view.findViewById(R.id.docBottomLayout);
            shareLayout = (LinearLayout) view.findViewById(R.id.shareLayout);
            doctorRowRating = (RatingBar) view.findViewById(R.id.doctorRowRating);
            landLine = new ArrayList();

            doctorRowPhoneImg.setOnClickListener(new View.OnClickListener() {
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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    doctorRowProfileImg.buildDrawingCache();
                    Bitmap profileImgBitmap = doctorRowProfileImg.getDrawingCache();
                    String docId = doctorRowId.getText().toString();
                    String docExperience = doctorRowExperience.getText().toString();
                    String docName = doctorRowName.getText().toString();
                    String docSpeciality = doctorRowSpeciality.getText().toString();
                    String docDegree = doctorRowDegree.getText().toString();
                    String docFees = doctorRowFees.getText().toString();
                    String doctorThumbsUp = doctorRowNoOfThumbsUp.getText().toString();
                    String doctorDistance = doctorRowDistance.getText().toString();
                    String discountDr = doctorRowDiscounted.getText().toString();


                    Log.e("tag" , "doc distance in adapter: "+doctorDistance);


                    Intent intent = new Intent(v.getContext() , DoctorDetail.class);

                    intent.putExtra("docProfile" , profileImgBitmap);
                    intent.putExtra("docId" , docId);
                    intent.putExtra("docExperience" , docExperience);
                    intent.putExtra("docName" , docName);
                    intent.putExtra("docSpeciality" , docSpeciality);
                    intent.putExtra("docDegree" , docDegree);
                    intent.putExtra("docFees" , docFees);
                    intent.putExtra("doctorThumbsUp" , doctorThumbsUp);
                    intent.putExtra("docAdapterKM" , doctorDistance);
                    intent.putExtra("discountDr" , discountDr);
                    v.getContext().startActivity(intent);

                }
            });

            shareLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String doc_profile_url = doctorRowShareLink.getText().toString();
                    //Toast.makeText(view.getContext(), "share url : "+doc_profile_url, Toast.LENGTH_SHORT).show();

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBodyText = "View Doctor Profile click on this link \n" ;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"MediCall");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText + doc_profile_url);
                    view.getContext().startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {


            FindDoctorGetterSetter ad = DocList.get(position);

            switch (getItemViewType(position)) {

                case ITEM:

                    holder.doctorRowId.setText(ad.getDoctorRowId());
                    //holder.doctorRowName.setText("Dr "+ad.getDoctorRowName() + "(" + ad.getDoctorRowStatus() + ")");
                    Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL + "doctors/" + DocList.get(position).getDoctorRowProfileImg()).transform(new CircleTransformPicasso()).into(holder.doctorRowProfileImg);



                    //holder.doctorRowFees.setText("Fee RS : "+ad.getDoctorRowMinFees() + " - " + ad.getDoctorRowMaxFees());
                    holder.doctorRowDistance.setText(ad.getDoctorRowDistance() + " KM");
                    holder.doctorRowNoOfThumbsUp.setText(ad.getDoctorRowNoOfThumbsUp());
                    holder.doctorRowExperience.setText(ad.getDoctorRowExperience());
                    holder.doctorRowSpeciality.setText(ad.getDoctorRowSpeciality());
                    holder.doctorRowDiscounted.setText(ad.getDoctor_offer_any_discount());
                    holder.doctorRowShareLink.setText(ad.getDoctorRowShareLink());
                    holder.degreeList = DocList.get(position).getDoctorRowDegree();
                    holder.landLine = DocList.get(position).getLandLineList();
                    holder.allhospitalList = DocList.get(position).getHospitalList();




                    //Log.e("tag land", "land line in adapter " + holder.landLine.toString());

                    if(ad.getDoctorRowRatingtext() == null)
                    {
                        holder.doctorRowRating.setRating(0);
                    }
                    else
                    {
                        holder.doctorRowRating.setRating(Float.parseFloat(String.valueOf(ad.getDoctorRowRatingtext())));
                    }

                    Log.e("tag degree", "degree " + holder.degreeList.toString());

//                    if(ad.getDoctorRowNoOfViews().equals(null) || ad.getDoctorRowNoOfViews().equals(""))
//                    {
//                        holder.doctorRowNoOfViews.setText("0");
//                    }
//                    else
//                    {
                        holder.doctorRowNoOfViews.setText(ad.getDoctorRowNoOfViews());
                    //}



                    if (ad.getDoctorRowName().contains("Dr.")) {
                        holder.doctorRowName.setText(ad.getDoctorRowName());

                    } else {
                        holder.doctorRowName.setText("Dr. " + ad.getDoctorRowName());

                    }

                    if(ad.getDoctor_offer_any_discount().contains("Yes"))
                    {
                        holder.doctorRowDiscountedImg.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.doctorRowDiscountedImg.setVisibility(View.GONE);
                    }


                    if(ad.getDoctorRowVerifiedStatus().contains("1"))
                    {
                        holder.verifiedDoc.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.verifiedDoc.setVisibility(View.GONE);
                    }


                    StringBuilder builder = new StringBuilder();

                    String prefix = "";
                    for (Object str : holder.degreeList) {
                        builder.append(prefix);
                        prefix = " , ";
                        builder.append(str);
                    }


                    Log.e("tag" , "degree list in adapter : "+builder.toString());

                    if(builder.toString().equals(""))
                    {
                        holder.doctorRowDegree.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.doctorRowDegree.setText(builder.toString());
                    }



                    if (ad.getDoctorRowMaxFees().equals(ad.getDoctorRowMinFees())) {
                        holder.doctorRowFees.setText("Fee : " + ad.getDoctorRowMinFees());
                    } else {
                        holder.doctorRowFees.setText("Fee : " + ad.getDoctorRowMinFees() + " - " + ad.getDoctorRowMaxFees());
                    }


                    if (ad.getDoctorRowExperience().contains("00")) {
                        holder.doctorRowExperience.setVisibility(View.GONE);
                    } else {
                        holder.doctorRowExperience.setText(ad.getDoctorRowExperience());
                    }


                    if(holder.allhospitalList.size() == 0)
                    {
                        holder.recyclerView.setVisibility(View.GONE);
                        holder.doctorHospitalLine.setVisibility(View.GONE);
                    }
                    else
                    {
                        DoctorMultipleHospitalRecycleView doctorMultipleHospitalRecycleView = new DoctorMultipleHospitalRecycleView(mContext, holder.allhospitalList , ad.getDoctorRowDistance());

                        holder.recyclerView.setHasFixedSize(true);
                        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        holder.recyclerView.setAdapter(doctorMultipleHospitalRecycleView);
                        holder.recyclerView.setNestedScrollingEnabled(false);
                    }



                    holder.doctorRowId.setVisibility(View.GONE);

                    break;

                case LOADING:
                    //do Nothing
                    break;
            }

        }




    }


    @Override
    public int getItemCount() {
        return DocList.size();
    }

    @Override
    public int getItemViewType(int position) {


        return (position == DocList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    //
//
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void add(FindDoctorGetterSetter mc) {
        DocList.add(mc);
        notifyItemInserted(DocList.size() - 1);
    }



    public void addLoadingFooter() {
        isLoadingAdded = true;
        notifyDataSetChanged();
        add(new FindDoctorGetterSetter());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = DocList.size() - 1;
        FindDoctorGetterSetter item = getItem(position);

        if (item != null) {
            DocList.remove(position);

        }
    }

    public FindDoctorGetterSetter getItem(int position) {
        return DocList.get(position);
    }

    public void addAll(List<FindDoctorGetterSetter> mcList) {
        for (FindDoctorGetterSetter mc : mcList) {
            add(mc);
        }
    }
    //

    @NonNull
    private MyViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        MyViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.find_doctor_custom_row, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
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


}
