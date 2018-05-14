package themedicall.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import themedicall.com.DoctorDetail;
import themedicall.com.GetterSetter.HospitalDoctorGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 11/30/2017.
 */

public class HospitalDocListRecyclerView extends RecyclerView.Adapter<HospitalDocListRecyclerView.MyViewHolder>{

    private List<HospitalDoctorGetterSetter> HosDocList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public HospitalDocListRecyclerView( Context context , List<HospitalDoctorGetterSetter> adList  ) {
        this.HosDocList = adList;
        this.mContext = context;


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ArrayList qualificationList , specialityList;
        public ImageView doctorRowProfileImg , doctorRowPhoneImg , doctorRowDiscountedImg  ,doctorRowThumbsDownImg ,  doctorRowShareProfile , verifiedDoc;
        public TextView doctorRowId , doctorRowName , doctorRowSpeciality , doctorRowDegree , doctorRowFees , doctorRowDistance
                , doctorRowClaimProfile , doctorRowNoOfThumbsUp , doctorRowNoOfThumbsDown  , doctorRowExperience , doctorRowDiscounted , doctorRowShareLink , doctorRowRatingtext , doctorRowNoOfViews;
        public RecyclerView recyclerView;
        RatingBar doctorRowRating ;
        LinearLayout docBottomLayout ,  shareLayout ;



        public MyViewHolder(final View view) {
            super(view);


            qualificationList = new ArrayList();
            specialityList = new ArrayList();

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
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_hospitals_list);
            docBottomLayout = (LinearLayout) view.findViewById(R.id.docBottomLayout);
            shareLayout = (LinearLayout) view.findViewById(R.id.shareLayout);
            doctorRowRating = (RatingBar) view.findViewById(R.id.doctorRowRating);


            shareLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String doc_profile_url = doctorRowShareLink.getText().toString();
                    //Toast.makeText(view.getContext(), "share url : "+doc_profile_url, Toast.LENGTH_SHORT).show();

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBodyText = "View Doctor Profile click on this link \n" ;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"MediCall");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, doc_profile_url);
                    view.getContext().startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

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

        }
    }




    @Override
    public HospitalDocListRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_doctor_custom_row, parent, false);
//            return new MyViewHolder(itemView);


        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        switch (viewType) {
            case ITEM:
               // viewHolder = getViewHolder(parent, inflater);

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_doctor_custom_row, parent, false);
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

            HospitalDoctorGetterSetter ad = HosDocList.get(position);
            holder.doctorRowId.setText(ad.getDoctorRowId());
            //holder.doctorRowName.setText("Dr "+ad.getDoctorRowName() + "(" + ad.getDoctorRowStatus() + ")");
            Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL + "doctors/" + HosDocList.get(position).getDoctorRowProfileImg()).transform(new CircleTransformPicasso()).into(holder.doctorRowProfileImg);
            //holder.doctorRowFees.setText("Fee RS : "+ad.getDoctorRowMinFees() + " - " + ad.getDoctorRowMaxFees());
            holder.doctorRowDistance.setText(ad.getDoctorRowDistance() + " KM");
            holder.doctorRowNoOfThumbsUp.setText(ad.getDoctorRowNoOfThumbsUp());
            holder.doctorRowExperience.setText(ad.getDoctorRowExperience());
            holder.qualificationList = HosDocList.get(position).getDoctorRowQualification();
            holder.specialityList = HosDocList.get(position).getDoctorRowSpeciality();
            holder.doctorRowDiscounted.setText(ad.getDoctorRowDiscountedImg());
            holder.doctorRowShareLink.setText(ad.getDoctorRowShareLink());
            holder.doctorRowNoOfViews.setText(ad.getDoctorRowNoOfViews());

            Log.e("tag degree", "degree " + holder.qualificationList.toString());

            if (ad.getDoctorRowRatingtext() == null) {
                holder.doctorRowRating.setRating(0);
            } else {
                holder.doctorRowRating.setRating(Float.parseFloat(String.valueOf(ad.getDoctorRowRatingtext())));
            }


            if (ad.getDoctorRowName().contains("Dr.")) {
                holder.doctorRowName.setText(ad.getDoctorRowName());

            } else {
                holder.doctorRowName.setText("Dr. " + ad.getDoctorRowName());

            }


            if (ad.getDoctorRowOfferDiscount().contains("Yes")) {
                holder.doctorRowDiscountedImg.setVisibility(View.VISIBLE);
            } else {
                holder.doctorRowDiscountedImg.setVisibility(View.GONE);
            }


            if (ad.getDoctorRowVerifiedStatus().contains("1")) {
                holder.verifiedDoc.setVisibility(View.VISIBLE);
            } else {
                holder.verifiedDoc.setVisibility(View.GONE);
            }


            StringBuilder qualificationBuilder = new StringBuilder();

            String qualificationPrefix = "";
            for (Object str : holder.qualificationList) {
                qualificationBuilder.append(qualificationPrefix);
                qualificationPrefix = " , ";
                qualificationBuilder.append(str);
            }

            holder.doctorRowDegree.setText(qualificationBuilder.toString());


            StringBuilder specialityBuilder = new StringBuilder();

            String specialityPrefix = "";
            for (Object str : holder.specialityList) {
                specialityBuilder.append(specialityPrefix);
                specialityPrefix = " , ";
                specialityBuilder.append(str);
            }

            holder.doctorRowSpeciality.setText(specialityBuilder.toString());


            if (ad.getDoctorRowMaxFees().equals(ad.getDoctorRowMinFees())) {
                holder.doctorRowFees.setText("Fee RS : " + ad.getDoctorRowMinFees());
            } else {
                holder.doctorRowFees.setText("Fee RS : " + ad.getDoctorRowMinFees() + " - " + ad.getDoctorRowMaxFees());
            }


            if (ad.getDoctorRowExperience().contains("00")) {
                holder.doctorRowExperience.setVisibility(View.GONE);
            } else {
                holder.doctorRowExperience.setText(ad.getDoctorRowExperience());
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
        return HosDocList.size();
    }

    @Override
    public int getItemViewType(int position) {


        return (position == HosDocList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public HospitalDoctorGetterSetter getItem(int position) {
        return HosDocList.get(position);
    }

    public void add(HospitalDoctorGetterSetter mc) {
        HosDocList.add(mc);
        notifyItemInserted(HosDocList.size() - 1);
    }

    public void addAll(List<HospitalDoctorGetterSetter> mcList) {
        for (HospitalDoctorGetterSetter mc : mcList) {
            add(mc);
        }
    }

}
