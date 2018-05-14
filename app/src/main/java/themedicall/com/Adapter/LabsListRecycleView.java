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
import themedicall.com.GetterSetter.LabsGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class LabsListRecycleView extends RecyclerView.Adapter<LabsListRecycleView.MyViewHolder>  {

    private List<LabsGetterSetter> labsList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView labRowProfileImg , labRowDiscountedImg;
        public TextView labRowId , labRowDistance , labRowName , labRowAddress , labRowPhoneNumber;

        public MyViewHolder(final View view) {
            super(view);


            labRowProfileImg = (ImageView) view.findViewById(R.id.labRowProfileImg);
            labRowDiscountedImg = (ImageView) view.findViewById(R.id.labRowDiscountedImg);
            labRowId = (TextView) view.findViewById(R.id.labRowId);
            labRowDistance = (TextView) view.findViewById(R.id.labRowDistance);
            labRowName = (TextView) view.findViewById(R.id.labRowName);
            labRowAddress = (TextView) view.findViewById(R.id.labRowAddress);
            labRowPhoneNumber = (TextView) view.findViewById(R.id.labRowPhoneNumber);




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    labRowProfileImg.buildDrawingCache();
                    Bitmap labImgBitmap = labRowProfileImg.getDrawingCache();
                    String labId = labRowId.getText().toString();
                    String labKm = labRowDistance.getText().toString();
                    String labName = labRowName.getText().toString();
                    String labAddress = labRowAddress.getText().toString();
                    String labPhone = labRowPhoneNumber.getText().toString();


//                    Intent intent = new Intent(v.getContext() , LabDetail.class);
//                    intent.putExtra("labImgBitmap" , labImgBitmap);
//                    intent.putExtra("labId" , labId);
//                    intent.putExtra("labKm" , labKm);
//                    intent.putExtra("labName" , labName);
//                    intent.putExtra("labAddress" , labAddress);
//                    intent.putExtra("labPhone" , labPhone);
//                    v.getContext().startActivity(intent);


                }
            });

        }
    }

    public LabsListRecycleView(Context context ,List<LabsGetterSetter> adList) {
        this.labsList = adList;
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

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.labs_custom_row, parent, false);
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
                    LabsGetterSetter ad = labsList.get(position);
                    Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL+"labs/"+labsList.get(position).getLabRowProfileImg()).transform(new CircleTransformPicasso()).into(holder.labRowProfileImg);
                    holder.labRowId.setText(ad.getLabRowId());
                    holder.labRowDistance.setText(ad.getLabRowDistance() +" KM");
                    holder.labRowName.setText(ad.getLabRowName());
                    holder.labRowAddress.setText(ad.getLabRowAddress());
                    holder.labRowPhoneNumber.setText(ad.getLabRowPhoneNumber());
                    holder.labRowId.setVisibility(View.GONE);
                    holder.labRowPhoneNumber.setVisibility(View.GONE);


                    if(ad.getLabRowDiscounted().contains("Yes"))
                    {
                        holder.labRowDiscountedImg.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.labRowDiscountedImg.setVisibility(View.GONE);
                    }

                    break;

                case LOADING:
                    //do Nothing
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return labsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == labsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public LabsGetterSetter getItem(int position) {
        return labsList.get(position);
    }

    public void add(LabsGetterSetter mc) {
        labsList.add(mc);
        notifyItemInserted(labsList.size() - 1);
    }

    public void addAll(List<LabsGetterSetter> mcList) {
        for (LabsGetterSetter mc : mcList) {
            add(mc);
        }
    }


}
