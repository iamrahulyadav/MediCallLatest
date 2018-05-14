package themedicall.com.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import themedicall.com.GetterSetter.ClaimProfileGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.Interfaces.ClaimButtonInterface;
import themedicall.com.R;

import java.util.ArrayList;

/**
 * Created by User-10 on 08-Feb-18.
 */

public class ClaimDoctorProfileAdapter extends RecyclerView.Adapter<ClaimDoctorProfileAdapter.MyViewHolder>  {

    private ArrayList<ClaimProfileGetterSetter> DocList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean isIdFojnd = false;

    private int lastPosition = -1;

    ClaimButtonInterface claimButtonInterface;

    public ClaimDoctorProfileAdapter( Context context , ArrayList<ClaimProfileGetterSetter> adList , ClaimButtonInterface claimButtonInterface) {
        this.DocList = adList;
        this.mContext = context;
        this.claimButtonInterface = claimButtonInterface;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView card_view;
        public ImageView doctorRowProfileImg;
        public TextView doctorRowId , doctorRowName , doctorRowSpeciality , doctorRowDegree , doctor_hospital;
        public Button bt_claim;

        private ArrayList allhospitalList ;
        private ArrayList doctorQualifications ;
        private ArrayList doctorSpecialities;

        public MyViewHolder(final View view) {
            super(view);

            card_view = (CardView) view.findViewById(R.id.card_view);
            doctorRowProfileImg = (ImageView) view.findViewById(R.id.doctorRowProfileImg);
            doctorRowId = (TextView) view.findViewById(R.id.doctorRowId);
            doctorRowName = (TextView) view.findViewById(R.id.doctorRowName);
            doctorRowSpeciality = (TextView) view.findViewById(R.id.doctorRowSpeciality);
            doctorRowDegree = (TextView) view.findViewById(R.id.doctorRowDegree);
            doctor_hospital = (TextView) view.findViewById(R.id.doctor_hospital);
            doctorQualifications = new ArrayList();
            //allhospitalList = new ArrayList();
            doctorSpecialities = new ArrayList();
            bt_claim = (Button) view.findViewById(R.id.bt_claim);



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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {


            ClaimProfileGetterSetter ad = DocList.get(position);

            switch (getItemViewType(position)) {

                case ITEM:

                    holder.doctorRowId.setText(ad.getDoctorId());
                    Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL + "doctors/" + DocList.get(position).getDoctorRowProfileImg()).transform(new CircleTransformPicasso()).into(holder.doctorRowProfileImg);
                    //holder.doctorRowFees.setText("Fee RS : "+ad.getDoctorRowMinFees() + " - " + ad.getDoctorRowMaxFees());
                    holder.doctorQualifications = DocList.get(position).getDoctorQualifications();
                    //holder.allhospitalList = DocList.get(position).getHospitalList();
                    holder.doctorSpecialities = DocList.get(position).getDocterSpecialities();

                    if (ad.getDoctorName().contains("Dr.")) {
                        holder.doctorRowName.setText(ad.getDoctorName());

                    } else {
                        holder.doctorRowName.setText("Dr. " + ad.getDoctorName());

                    }


                    StringBuilder builderForSpecialities = new StringBuilder();
                    StringBuilder builderForDegree = new StringBuilder();
                    StringBuilder builderForHospitals = new StringBuilder();

                    String prefix1 = "";
                    for (Object str : holder.doctorQualifications) {
                        builderForDegree.append(prefix1);
                        prefix1 = " , ";
                        builderForDegree.append(str);
                    }

                    String prefix2 = "";
                    for (Object str : holder.doctorSpecialities) {
                        builderForSpecialities.append(prefix2);
                        prefix2 = " , ";
                        builderForSpecialities.append(str);
                    }

                    String prefix3 = "";
                    for (Object str : holder.doctorSpecialities) {
                        builderForHospitals.append(prefix3);
                        prefix3 = " , ";
                        builderForHospitals.append(str);
                    }


                    holder.doctorRowDegree.setText(builderForDegree.toString());
                    holder.doctorRowSpeciality.setText(builderForSpecialities.toString());
                    //holder.doctor_hospital.setText(builderForHospitals.toString());

                    holder.doctorRowId.setVisibility(View.GONE);

                    holder.bt_claim.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            claimButtonInterface.claimButtonClick(view, position, holder.doctorRowId.getText().toString());
                        }
                    });


                    break;
                case LOADING:
                    //do Nothing
                    break;
            }

        }
/*

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.down_from_top : R.anim.wave_scaling);
        holder.card_view.startAnimation(animation);
        lastPosition = position;
*/



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

    public void add(ClaimProfileGetterSetter mc) {
        DocList.add(mc);
        notifyItemInserted(DocList.size() - 1);
    }



    public void addLoadingFooter() {
        isLoadingAdded = true;
        notifyDataSetChanged();
        add(new ClaimProfileGetterSetter());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = DocList.size() - 1;
        ClaimProfileGetterSetter item = getItem(position);

        if (item != null) {
            DocList.remove(position);

        }
    }

    public ClaimProfileGetterSetter getItem(int position) {
        return DocList.get(position);
    }

    public void addAll(ArrayList<ClaimProfileGetterSetter> mcList) {
        for (ClaimProfileGetterSetter mc : mcList) {
            add(mc);
        }
    }
    //

    @NonNull
    private MyViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        MyViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.custom_claim_data_layout, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

}
