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
import themedicall.com.GetterSetter.PharmacyGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class PharmacyListRecycleView extends RecyclerView.Adapter<PharmacyListRecycleView.MyViewHolder>  {

    private List<PharmacyGetterSetter> pharmacyList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView pharmacyRowProfileImg , pharmacyRowDiscountedImg;
        public TextView pharmacyRowId , pharmacyRowDistance , pharmacyRowName , pharmacyRowAddress , pharmacyRowPhoneNumber;

        public MyViewHolder(final View view) {
            super(view);


            pharmacyRowProfileImg = (ImageView) view.findViewById(R.id.pharmacyRowProfileImg);
            pharmacyRowDiscountedImg = (ImageView) view.findViewById(R.id.pharmacyRowDiscountedImg);
            pharmacyRowId = (TextView) view.findViewById(R.id.pharmacyRowId);
            pharmacyRowDistance = (TextView) view.findViewById(R.id.pharmacyRowDistance);
            pharmacyRowName = (TextView) view.findViewById(R.id.pharmacyRowName);
            pharmacyRowAddress = (TextView) view.findViewById(R.id.pharmacyRowAddress);
            pharmacyRowPhoneNumber = (TextView) view.findViewById(R.id.pharmacyRowPhoneNumber);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pharmacyRowProfileImg.buildDrawingCache();
                    Bitmap pharmacyImgBitmap = pharmacyRowProfileImg.getDrawingCache();
                    String pharmacyId = pharmacyRowId.getText().toString();
                    String pharmacyKm = pharmacyRowDistance.getText().toString();
                    String pharmacyName = pharmacyRowName.getText().toString();
                    String pharmacyAddress = pharmacyRowAddress.getText().toString();
                    String pharmacyPhone = pharmacyRowPhoneNumber.getText().toString();


//                    Intent intent = new Intent(v.getContext() , PharmacyDetail.class);
//                    intent.putExtra("pharmacyImgBitmap" , pharmacyImgBitmap);
//                    intent.putExtra("pharmacyId" , pharmacyId);
//                    intent.putExtra("pharmacyKm" , pharmacyKm);
//                    intent.putExtra("pharmacyName" , pharmacyName);
//                    intent.putExtra("pharmacyAddress" , pharmacyAddress);
//                    intent.putExtra("pharmacyPhone" , pharmacyPhone);
//                    v.getContext().startActivity(intent);

                }
            });

        }
    }

    public PharmacyListRecycleView(Context context , List<PharmacyGetterSetter> adList) {
        this.mContext = context;
        this.pharmacyList = adList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.pharmacy_custom_row, parent, false);
//        return new MyViewHolder(itemView);

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        switch (viewType) {
            case ITEM:
                // viewHolder = getViewHolder(parent, inflater);

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_custom_row, parent, false);
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
                    PharmacyGetterSetter ad = pharmacyList.get(position);
                    Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL+"pharmacies/"+pharmacyList.get(position).getPharmacyRowProfileImg()).transform(new CircleTransformPicasso()).into(holder.pharmacyRowProfileImg);
                    holder.pharmacyRowId.setText(ad.getPharmacyRowId());
                    holder.pharmacyRowDistance.setText(ad.getPharmacyRowDistance() +" KM");
                    holder.pharmacyRowName.setText(ad.getPharmacyRowName());
                    holder.pharmacyRowAddress.setText(ad.getPharmacyRowAddress());
                    holder.pharmacyRowPhoneNumber.setText(ad.getPharmacyRowPhoneNumber());
                    holder.pharmacyRowId.setVisibility(View.GONE);
                    holder.pharmacyRowPhoneNumber.setVisibility(View.GONE);

                    if(ad.getPharmacyRowDiscountImg().contains("Yes"))
                    {
                        holder.pharmacyRowDiscountedImg.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.pharmacyRowDiscountedImg.setVisibility(View.GONE);
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
        return pharmacyList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == pharmacyList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public PharmacyGetterSetter getItem(int position) {
        return pharmacyList.get(position);
    }

    public void add(PharmacyGetterSetter mc) {
        pharmacyList.add(mc);
        notifyItemInserted(pharmacyList.size() - 1);
    }

    public void addAll(List<PharmacyGetterSetter> mcList) {
        for (PharmacyGetterSetter mc : mcList) {
            add(mc);
        }
    }

}
