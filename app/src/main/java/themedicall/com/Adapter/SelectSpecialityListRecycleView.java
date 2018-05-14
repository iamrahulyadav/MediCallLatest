package themedicall.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import themedicall.com.FindDoctor;
import themedicall.com.GetterSetter.SelectSpecialityGetterSetter;
import themedicall.com.Globel.Glob;
import themedicall.com.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class SelectSpecialityListRecycleView extends RecyclerView.Adapter<SelectSpecialityListRecycleView.MyViewHolder>  {

    private List<SelectSpecialityGetterSetter> hospitalsList;
    private Context mContext;
    View itemView ;

    public SelectSpecialityListRecycleView(Context context, List<SelectSpecialityGetterSetter> adList) {
        this.hospitalsList = adList;
        this.mContext = context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView select_speciality_image ;
        public TextView select_speciality_id , select_speciality_name ;

        public MyViewHolder(final View view) {
            super(view);

//


            select_speciality_id = (TextView) view.findViewById(R.id.select_speciality_id);
            select_speciality_name = (TextView) view.findViewById(R.id.select_speciality_name);
            select_speciality_image = (ImageView) view.findViewById(R.id.select_speciality_image);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String getSpecialityId = select_speciality_id.getText().toString();
                    String getSpecialityName = select_speciality_name.getText().toString();
                    Intent intent = new Intent(v.getContext() , FindDoctor.class);
                    intent.putExtra("Speciality_id" , getSpecialityId);
                    intent.putExtra("Speciality_name" , getSpecialityName);
                    v.getContext().startActivity(intent);

                }
            });

        }
    }


    @Override
    public int getItemViewType(int position) {

        return position % 2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType)
        {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.odd_speciality_cusom_row, parent, false);

            break;
            case 1:
                 itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.even_speciality_custom_row, parent, false);
            break;
       }
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.odd_speciality_cusom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SelectSpecialityGetterSetter ad = hospitalsList.get(position);
        holder.select_speciality_id.setText(ad.getSpeciality_id());
        holder.select_speciality_name.setText(ad.getSpeciality_name());
        Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL+"specialities/"+hospitalsList.get(position).getSpeciality_image()).into(holder.select_speciality_image);
    }
    @Override
    public int getItemCount() {
        return hospitalsList.size();
    }



}
