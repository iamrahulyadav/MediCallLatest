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
import themedicall.com.DoctorDetail;
import themedicall.com.GetterSetter.HospitalMultipleDocGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class HospitalMultipleDocRecycleView extends RecyclerView.Adapter<HospitalMultipleDocRecycleView.MyViewHolder>  {

    private List<HospitalMultipleDocGetterSetter> docImageList;
    private Context mContext;
    private Double hospitalKm;


    public  HospitalMultipleDocRecycleView(Context context, List<HospitalMultipleDocGetterSetter> adList, Double hospitalKm) {
        this.docImageList = adList;
        this.mContext = context;
        this.hospitalKm = hospitalKm;

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

                    String docId = hospitalRowId.getText().toString();
                   // Toast.makeText(v.getContext() , "km "+hospitalKm , Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(v.getContext() , DoctorDetail.class);
                    intent.putExtra("docId" , docId);
                    intent.putExtra("docKM" , String.valueOf(hospitalKm));
                    v.getContext().startActivity(intent);

                }
            });

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_multiple_doctor_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HospitalMultipleDocGetterSetter ad = docImageList.get(position);

        holder.hospitalRowId.setText(ad.getHospitalRowId());
        Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL+"doctors/"+docImageList.get(position).getHospitalRowDocImg()).transform(new CircleTransformPicasso()).into(holder.hospitalRowDocImg);
        holder.hospitalRowDocName.setText(ad.getHospitalRowDocName());


        if(ad.getHospitalRowDocName().contains("Dr."))
        {
            holder.hospitalRowDocName.setText(ad.getHospitalRowDocName());

        }
        else
        {
            holder.hospitalRowDocName.setText("Dr. " + ad.getHospitalRowDocName());

        }
    }

    @Override
    public int getItemCount() {
        return docImageList.size();
    }

}
