package themedicall.com.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import themedicall.com.GetterSetter.HospitalTimingGetterSetter;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 12/14/2017.
 */


public class HospitalTimingListRecyclerView extends RecyclerView.Adapter<HospitalTimingListRecyclerView.MyViewHolder>  {

    private List<HospitalTimingGetterSetter> dayTimeList;
    Context context;
    String currentDay ;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView hospitalDayRow , hospitalTimingRow;
        LinearLayout timingLayout ;

        public MyViewHolder(final View view) {
            super(view);


            hospitalDayRow = (TextView) view.findViewById(R.id.hospitalDayRow);
            hospitalTimingRow = (TextView) view.findViewById(R.id.hospitalTimingRow);
            timingLayout = (LinearLayout) view.findViewById(R.id.timingLayout);

        }
    }



    public HospitalTimingListRecyclerView(Context context , List<HospitalTimingGetterSetter> adList , String currentDay) {
        this.context = context ;
        this.dayTimeList = adList;
        this.currentDay = currentDay;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_timing_practice_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HospitalTimingGetterSetter ad = dayTimeList.get(position);

        holder.hospitalDayRow.setText(ad.getHospitalDayRow());
        holder.hospitalTimingRow.setText(ad.getHospitalTimingRow());

        Log.e("tag" , "tag current day from list " + ad.getHospitalDayRow());
        Log.e("tag" , "tag current day " + currentDay);

        if(ad.getHospitalDayRow().equals(currentDay))
        {
            holder.timingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.timingBackground));
        }
        else
        {
            Log.e("tag" , "day not match : ");
        }

    }

    @Override
    public int getItemCount() {
        return dayTimeList.size();
    }

}



