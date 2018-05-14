package themedicall.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import themedicall.com.GetterSetter.HospitalServiceGetterSetter;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class HospitalsServiceListRecycleView extends RecyclerView.Adapter<HospitalsServiceListRecycleView.MyViewHolder>  {

    private List<HospitalServiceGetterSetter> servicesList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView hospitalService;

        public MyViewHolder(final View view) {
            super(view);


            hospitalService = (TextView) view.findViewById(R.id.hospitalService);



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

    public HospitalsServiceListRecycleView(List<HospitalServiceGetterSetter> adList) {
        this.servicesList = adList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospitals_detail_services_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HospitalServiceGetterSetter ad = servicesList.get(position);

        holder.hospitalService.setText(ad.getHospitalService());

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

}
