package themedicall.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import themedicall.com.GetterSetter.HospitalMultipleDocGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.HospitalDetail;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class DoctorMultipleHospitalRecycleView extends RecyclerView.Adapter<DoctorMultipleHospitalRecycleView.MyViewHolder>  {

    private List<HospitalMultipleDocGetterSetter> docImageList;
    private Context mContext;
    private Double doctorRowDistance;


    public DoctorMultipleHospitalRecycleView(Context context, List<HospitalMultipleDocGetterSetter> adList, Double doctorRowDistance) {
        this.docImageList = adList;
        this.mContext = context;
        this.doctorRowDistance = doctorRowDistance;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView hospitalRowId ;
        public ImageView hospitalRowDocImg ;
        public TextView hospitalRowDocName ;

        public MyViewHolder(final View view) {
            super(view);


            hospitalRowId = (TextView) view.findViewById(R.id.hospitalRowId);
            hospitalRowDocImg = (ImageView) view.findViewById(R.id.hospitalRowDocImg);
            hospitalRowDocName = (TextView) view.findViewById(R.id.hospitalRowDocName);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hospital_id = hospitalRowId.getText().toString();

                    Intent intent = new Intent(v.getContext() , HospitalDetail.class);
                    intent.putExtra("hospital_id" , hospital_id);
                    intent.putExtra("hosKM" , String.valueOf(doctorRowDistance));
                    v.getContext().startActivity(intent);

                }
            });

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_multiple_hos_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HospitalMultipleDocGetterSetter ad = docImageList.get(position);

        holder.hospitalRowId.setText(ad.getHospitalRowId());
        Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL+"hospitals/"+docImageList.get(position).getHospitalRowDocImg()).transform(new CircleTransformPicasso()).into(holder.hospitalRowDocImg);

        if(ad.getHospitalRowDocName().length() > 26)
        {
            String smallString = ad.getHospitalRowDocName().substring(0 ,25)+" ...";
            holder.hospitalRowDocName.setText(smallString);
        }
        else
        {
            holder.hospitalRowDocName.setText(ad.getHospitalRowDocName());
        }

    }

    @Override
    public int getItemCount() {
        return docImageList.size();
    }

}
