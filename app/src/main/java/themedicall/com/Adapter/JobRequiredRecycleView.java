package themedicall.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import themedicall.com.GetterSetter.JobRequiredGetterSetter;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class JobRequiredRecycleView extends RecyclerView.Adapter<JobRequiredRecycleView.MyViewHolder>  {

    private List<JobRequiredGetterSetter> jobRequiredList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView jobRequiredRowId , jobRequiredRowTitle , jobRequiredRowLocation , jobRequiredRowGender , jobRequiredRowAge;


        public MyViewHolder(final View view) {
            super(view);

            jobRequiredRowId = (TextView) view.findViewById(R.id.jobRequiredRowId);
            jobRequiredRowTitle = (TextView) view.findViewById(R.id.jobRequiredRowTitle);
            jobRequiredRowLocation = (TextView) view.findViewById(R.id.jobRequiredRowLocation);
            jobRequiredRowGender = (TextView) view.findViewById(R.id.jobRequiredRowGender);
            jobRequiredRowAge = (TextView) view.findViewById(R.id.jobRequiredRowAge);


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(v.getContext() , HappyUserDetail.class);
//                    v.getContext().startActivity(intent);
//
//                    String id = happyUserId.getText().toString();
//                    Toast.makeText(v.getContext(), "id "+id, Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }

    public JobRequiredRecycleView(List<JobRequiredGetterSetter> adList) {
        this.jobRequiredList = adList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_available_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JobRequiredGetterSetter ad = jobRequiredList.get(position);
        holder.jobRequiredRowId.setText(ad.getJobRequiredRowId());
        holder.jobRequiredRowTitle.setText(ad.getJobRequiredRowTitle());
        holder.jobRequiredRowLocation.setText(ad.getJobRequiredRowLocation());
        holder.jobRequiredRowGender.setText(ad.getJobRequiredRowGender());
        holder.jobRequiredRowAge.setText(ad.getJobRequiredRowAge() + " Years");


        holder.jobRequiredRowId.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return jobRequiredList.size();
    }

}
