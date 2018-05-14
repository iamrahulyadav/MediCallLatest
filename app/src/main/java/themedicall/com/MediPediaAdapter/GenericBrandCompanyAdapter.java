package themedicall.com.MediPediaAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import themedicall.com.MediPediaGetterSetter.ListInfo;
import themedicall.com.MediStudySection.MediStudyExamMode;
import themedicall.com.MediaPediaSection.BrandPage;
import themedicall.com.MediaPediaSection.CompnyDetail;
import themedicall.com.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 3/2/2018.
 */

public class GenericBrandCompanyAdapter extends RecyclerView.Adapter<GenericBrandCompanyAdapter.DataHolder> implements Filterable {

    public static List<ListInfo> infoList;
    private List<ListInfo> infoListFiltered;
    private InfoAdapterListener listener;
    Context context;
    View.OnClickListener onClickListener;
    OnitemClickListener onitemClickListener;
    View itemView;
    String type;

    public GenericBrandCompanyAdapter(Context context, ArrayList<ListInfo> info, String type) {
        this.context = context;
        this.infoList = info;
        this.infoListFiltered = infoList;
        this.type = type;
    }

//    public GenericBrandCompanyAdapter(Context context, List<ListInfo> infoList, InfoAdapterListener listener) {
//        this.context = context;
//        this.listener = listener;
//        this.infoList = infoList;
//        this.infoListFiltered = infoList;
//    }
//
//    public GenericBrandCompanyAdapter(View.OnClickListener onClickListener, ArrayList<ListInfo> info) {
//        this.onClickListener = onClickListener;
//        this.infoList = info;
//    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    infoListFiltered = infoList;
                } else {
                    List<ListInfo> filteredList = new ArrayList<>();
                    for (ListInfo row : infoList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getId().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    infoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = infoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                infoListFiltered = (ArrayList<ListInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnitemClickListener {
        void onItemClick(LinearLayout b, View v, ListInfo obj, int position);
    }

    public void setOnitemClickListener(OnitemClickListener onitemClickListener) {
        this.onitemClickListener = onitemClickListener;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        //View view = LayoutInflater.from(context).inflate(R.layout.custome_layout_info, null);

        switch (i)
        {
            case 0:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.odd_speciality_cusom_row, viewGroup, false);

                break;
            case 1:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.even_speciality_custom_row, viewGroup, false);
                break;
        }
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.odd_speciality_cusom_row, parent, false);

        return new DataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GenericBrandCompanyAdapter.DataHolder dataHolder, final int i) {
        final ListInfo c = infoListFiltered.get(i);
        String cateName = c.getName().toString();
        Log.e("TAG", "the name of cate: " + cateName);

        dataHolder.categoryName.setText(c.getId());
        //dataHolder.tvNum.setText(""+c.getNum());
    }

    @Override
    public int getItemCount() {
        return infoListFiltered.size();
    }



    public class DataHolder extends RecyclerView.ViewHolder {

        TextView categoryName, tvNum;
        ImageView img ;

        public DataHolder(View itemView) {
            super(itemView);

            categoryName = (TextView) itemView.findViewById(R.id.select_speciality_name);
            categoryName.setTypeface(categoryName.getTypeface(), Typeface.BOLD);
            categoryName.setPadding(0 , 30, 0 ,30 );
            img = (ImageView) itemView.findViewById(R.id.select_speciality_image);
            img.setVisibility(View.GONE);
            categoryName.setSelected(true);
            //tvNum = (TextView) itemView.findViewById(R.id.tvNum);
            //tvNum.setSelected(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  Toast.makeText(context,""+infoListFiltered.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();

                    if (type.equals("Generic")){
                        Intent myIntent = new Intent(context, BrandPage.class);
                        context.startActivity(myIntent);
                    }

                    if (type.equals("Brand")) {
                        Intent myIntent = new Intent(context, BrandPage.class);
                        context.startActivity(myIntent);
                    }

                    if (type.equals("Company")) {
                        Intent myIntent = new Intent(context, CompnyDetail.class);
                        context.startActivity(myIntent);
                    }

                    if (type.equals("QuestionTest")) {
                        Intent myIntent = new Intent(context, MediStudyExamMode.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(myIntent);
                    }

                    //listener.onInfoSelected(infoListFiltered.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface InfoAdapterListener {
        void onInfoSelected(ListInfo listInfo);
    }
}
