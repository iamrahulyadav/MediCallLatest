package themedicall.com.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import themedicall.com.GetterSetter.LabsGetterSetter;
import themedicall.com.GetterSetter.MedAlarmGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.R;

/**
 * Created by Muhammad Adeel on 3/29/2018.
 */

public class MedAlarmAdapter extends RecyclerView.Adapter<MedAlarmAdapter.MyViewHolder>  {

    private List<MedAlarmGetterSetter> medicineList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView medStatusDefault , medStatusTake , medStatusSkip;
        public TextView medNameRow , medTakeRow , medSkipRow ;

        public MyViewHolder(final View view) {
            super(view);


            medStatusDefault = (ImageView) view.findViewById(R.id.medStatusDefault);
            medStatusTake = (ImageView) view.findViewById(R.id.medStatusTake);
            medStatusSkip = (ImageView) view.findViewById(R.id.medStatusSkip);

            medNameRow = (TextView) view.findViewById(R.id.medNameRow);
            medTakeRow = (TextView) view.findViewById(R.id.medTakeRow);
            medSkipRow = (TextView) view.findViewById(R.id.medSkipRow);




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }
    }

    public MedAlarmAdapter(Context context ,List<MedAlarmGetterSetter> List) {
        this.medicineList = List;
        this.mContext = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.labs_custom_row, parent, false);
//        return new MyViewHolder(itemView);

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        switch (viewType) {
            case ITEM:
                // viewHolder = getViewHolder(parent, inflater);

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.med_alarm_medicine_custom_row, parent, false);
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
                    MedAlarmGetterSetter ad = medicineList.get(position);
                    holder.medNameRow.setText(ad.getMedicineName());
                    holder.medTakeRow.setText(ad.getMedicineTake());
                    holder.medSkipRow.setText(ad.getMedicineSkip());



                    break;

                case LOADING:
                    //do Nothing
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == medicineList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public MedAlarmGetterSetter getItem(int position) {
        return medicineList.get(position);
    }

    public void add(MedAlarmGetterSetter mc) {
        medicineList.add(mc);
        notifyItemInserted(medicineList.size() - 1);
    }

    public void addAll(List<MedAlarmGetterSetter> mcList) {
        for (MedAlarmGetterSetter mc : mcList) {
            add(mc);
        }
    }
}


