package themedicall.com.MediPediaAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import themedicall.com.MediPediaGetterSetter.BrandFormsAndSimilarMedicineGetterSetter;
import themedicall.com.MediPediaGetterSetter.ListInfo;
import themedicall.com.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 3/2/2018.
 */

public class BrandFormsAndSimilarMedicinesAdapter extends RecyclerView.Adapter<BrandFormsAndSimilarMedicinesAdapter.DataHolder> {

    public static List<BrandFormsAndSimilarMedicineGetterSetter> infoList;
    private BrandFormsAndSimilarMedicineGetterSetter listener;
    Context context;
    View.OnClickListener onClickListener;
    OnitemClickListener onitemClickListener;

    public BrandFormsAndSimilarMedicinesAdapter(Context context1, ArrayList<BrandFormsAndSimilarMedicineGetterSetter> list) {
        context = context1;
        infoList = list;
    }

    public interface OnitemClickListener {
        void onItemClick(LinearLayout b, View v, ListInfo obj, int position);
    }

    public void setOnitemClickListener(OnitemClickListener onitemClickListener) {
        this.onitemClickListener = onitemClickListener;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.drugforms_similarmedicine_row, null);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(DataHolder dataHolder, int i) {
        final BrandFormsAndSimilarMedicineGetterSetter c = infoList.get(i);
        String cateName = c.getName().toString();
        Log.e("TAG", "the name of cate: " + cateName);

        dataHolder.name.setText(c.getName());
        dataHolder.potency.setText("" + c.getPotency());
        dataHolder.companyName.setText("" + c.getCompanyName());
        dataHolder.drugName.setText("" + c.getDrugName());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        TextView name, potency, companyName, drugName;

        public DataHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.brandRowName);
            //categoryName.setSelected(true);
            potency = (TextView) itemView.findViewById(R.id.brandRowPotency);
            //tvNum.setSelected(true);
            companyName = (TextView) itemView.findViewById(R.id.brandRowCompanyName);
            drugName = (TextView) itemView.findViewById(R.id.brandRowDrugName);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Toast.makeText(context,""+infoList.get(getAdapterPosition()).getName(),Toast.LENGTH_LONG).show();
//
//                    if (MediPedia.cBool == true) {
//                        Intent myIntent = new Intent(context, CompanyDetails.class);
//                        context.startActivity(myIntent);
//                    }
//                    //listener.onInfoSelected(infoListFiltered.get(getAdapterPosition()));
//                }
//            });

        }
    }


    public interface BrandFormsAndSimilarMedicinesAdapterListener {
        void onInfoSelected(BrandFormsAndSimilarMedicineGetterSetter listInfo);
    }
}
