package themedicall.com.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import themedicall.com.GetterSetter.HomeCareRequiredGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 2/13/2018.
 */

public class HomeCareRequiredAdapter extends RecyclerView.Adapter<HomeCareRequiredAdapter.MyViewHolder>  {

    private List<HomeCareRequiredGetterSetter> careRequiredList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public static final int REQUEST_PERMISSION_CODE = 300;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView careRequiredImage ;
        public TextView careRequiredStatus , careRequiredHours , careRequiredBullet , careRequiredRequestName , careRequiredDescription , careRequiredId , careRequiredDistance  , careRequiredArea ,  careRequiredApply   ;

        public MyViewHolder(final View view) {
            super(view);


            careRequiredImage = (ImageView) view.findViewById(R.id.careRequiredImage);
            careRequiredStatus = (TextView) view.findViewById(R.id.careRequiredStatus);
            careRequiredHours = (TextView) view.findViewById(R.id.careRequiredHours);
            careRequiredBullet = (TextView) view.findViewById(R.id.careRequiredBullet);
            careRequiredRequestName = (TextView) view.findViewById(R.id.careRequiredRequestName);
            careRequiredDescription = (TextView) view.findViewById(R.id.careRequiredDescription);
            careRequiredId = (TextView) view.findViewById(R.id.careRequiredId);
            careRequiredDistance = (TextView) view.findViewById(R.id.careRequiredDistance);
            careRequiredArea = (TextView) view.findViewById(R.id.careRequiredArea);
            careRequiredApply = (TextView) view.findViewById(R.id.careRequiredApply);


        }
    }

    public HomeCareRequiredAdapter(Context context , List<HomeCareRequiredGetterSetter> appealList) {
        this.mContext = context;
        this.careRequiredList = appealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.pharmacy_custom_row, parent, false);
//        return new MyViewHolder(itemView);

        //MyViewHolder viewHolder = null;
        //LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

//        switch (viewType) {
//            case ITEM:
//                // viewHolder = getViewHolder(parent, inflater);

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_care_required_custom_row, parent, false);
                MyViewHolder viewHolder = new MyViewHolder(itemView);

//                break;
//            case LOADING:
//                View v2 = inflater.inflate(R.layout.progress_item_at_end, parent, false);
//                viewHolder = new MyViewHolder(v2);
//                break;
//
//        }
//
//


        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      //  if (holder instanceof MyViewHolder) {


//            switch (getItemViewType(position)) {
//
//                case ITEM:
                    HomeCareRequiredGetterSetter ad = careRequiredList.get(position);
                    Picasso.with(mContext).load(ad.getCareRequiredImage()).transform(new CircleTransformPicasso()).into(holder.careRequiredImage);
                    holder.careRequiredHours.setText("About " +ad.getCareRequiredHours());
                    holder.careRequiredRequestName.setText("by " +ad.getCareRequiredRequestName());
                    holder.careRequiredDescription.setText(ad.getCareRequiredDescription());
                    holder.careRequiredId.setText(ad.getCareRequiredId());
                    holder.careRequiredDistance.setText(ad.getCareRequiredDistance() + "KM");
                    holder.careRequiredArea.setText(ad.getCareRequiredArea());

                    if(ad.getCareRequiredStatus().contains("0"))
                    {
                        holder.careRequiredStatus.setText("In Progress");
                        holder.careRequiredBullet.setText("\u25CF");
                        holder.careRequiredBullet.setTextColor(ContextCompat.getColor(mContext, R.color.timingBackground));
                    }
                    else
                    {
                        holder.careRequiredStatus.setText("Arranged");
                        holder.careRequiredBullet.setText("\u25CF");
                        holder.careRequiredBullet.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                    }

//                    break;
//                case LOADING:
//                    //do Nothing
//                    break;
//            }
        //}
    }


    @Override
    public int getItemCount() {
        return careRequiredList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return (position == careRequiredList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
//    }
//
//    public HomeCareRequiredGetterSetter getItem(int position) {
//        return careRequiredList.get(position);
//    }
//
//    public void add(HomeCareRequiredGetterSetter mc) {
//        careRequiredList.add(mc);
//        notifyItemInserted(careRequiredList.size() - 1);
//    }
//
//    public void addAll(List<HomeCareRequiredGetterSetter> mcList) {
//        for (HomeCareRequiredGetterSetter mc : mcList) {
//            add(mc);
//        }
//    }

}