package themedicall.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import themedicall.com.GetterSetter.HealthEventGetterSetter;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class HealthEventListRecycleView extends RecyclerView.Adapter<HealthEventListRecycleView.MyViewHolder>  {

    private List<HealthEventGetterSetter> healthEventList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView healthEventRowProfileImg , healthEventRowCoverImg;
        public TextView healthEventRowId , healthEventRowName , healthEventRowDate , healthEventRowStartDate , healthEventRowStartTime , healthEventRowEndDate
                , healthEventRowEndTime , healthEventRowTitle , healthEventRowAddress, healthEventRowFavourite , healthEventRowView , healthEventRowShare , healthEventRowSave;

        public MyViewHolder(final View view) {
            super(view);

            healthEventRowProfileImg = (ImageView) view.findViewById(R.id.healthEventRowProfileImage);
            healthEventRowCoverImg = (ImageView) view.findViewById(R.id.healthEventRowCoverImage);
            healthEventRowId = (TextView) view.findViewById(R.id.healthEventRowId);
            healthEventRowName = (TextView) view.findViewById(R.id.healthEventRowName);
            healthEventRowDate = (TextView) view.findViewById(R.id.healthEventRowDate);
            healthEventRowStartDate = (TextView) view.findViewById(R.id.healthEventRowStartDate);
            healthEventRowStartTime = (TextView) view.findViewById(R.id.healthEventRowStartTime);
            healthEventRowEndDate = (TextView) view.findViewById(R.id.healthEventRowEndDate);
            healthEventRowEndTime = (TextView) view.findViewById(R.id.healthEventRowEndTime);
            healthEventRowTitle = (TextView) view.findViewById(R.id.healthEventRowTitle);
            healthEventRowAddress = (TextView) view.findViewById(R.id.healthEventRowAddress);
            healthEventRowFavourite = (TextView) view.findViewById(R.id.healthEventRowFavourite);
            healthEventRowView = (TextView) view.findViewById(R.id.healthEventRowView);
            healthEventRowShare = (TextView) view.findViewById(R.id.healthEventRowShare);
            healthEventRowSave = (TextView) view.findViewById(R.id.healthEventRowSave);


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(v.getContext() , HealthEventsDetail.class);
//                    v.getContext().startActivity(intent);
//
//                }
//            });

        }
    }

    public HealthEventListRecycleView(List<HealthEventGetterSetter> adList) {
        this.healthEventList = adList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_events_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HealthEventGetterSetter ad = healthEventList.get(position);
        holder.healthEventRowProfileImg.setImageResource(ad.getHealthEventRowProfileImg());
        holder.healthEventRowCoverImg.setImageResource(ad.getHealthEventRowCoverImg());
        holder.healthEventRowId.setText(ad.getHealthEventRowId());
        holder.healthEventRowName.setText(ad.getHealthEventRowName());
        holder.healthEventRowDate.setText(ad.getHealthEventRowDate());
        holder.healthEventRowStartDate.setText(ad.getHealthEventRowStartDate());
        holder.healthEventRowStartTime.setText(ad.getHealthEventRowStartTime());
        holder.healthEventRowEndDate.setText(ad.getHealthEventRowEndDate());
        holder.healthEventRowEndTime.setText(ad.getHealthEventRowEndTime());
        holder.healthEventRowTitle.setText(ad.getHealthEventRowTitle());
        holder.healthEventRowAddress.setText(ad.getHealthEventRowAddress());
        holder.healthEventRowFavourite.setText(ad.getHealthEventRowEndFavourite());
        holder.healthEventRowView.setText(ad.getHealthEventRowEndView());
        holder.healthEventRowShare.setText(ad.getHealthEventRowEndShare());
        holder.healthEventRowSave.setText(ad.getHealthEventRowEndSave());



        holder.healthEventRowId.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return healthEventList.size();
    }

}
