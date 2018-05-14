package themedicall.com.MediPediaAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import themedicall.com.MediPediaGetterSetter.ListInfo;
import themedicall.com.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 3/2/2018.
 */

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.DataHolder> implements Filterable {

    public static List<ListInfo> infoList;
    private List<ListInfo> infoListFiltered;
    private CompanyAdapterListener listener;
    Context context;
    View.OnClickListener onClickListener;
    OnitemClickListener onitemClickListener;

    CompanyAdapter(Context context, ArrayList<ListInfo> info) {
        this.context = context;
        this.infoList = info;
        this.infoListFiltered = infoList;
    }

    public CompanyAdapter(Context context, List<ListInfo> infoList, CompanyAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.infoList = infoList;
        this.infoListFiltered = infoList;
    }

    public CompanyAdapter(View.OnClickListener onClickListener, ArrayList<ListInfo> info) {
        this.onClickListener = onClickListener;
        this.infoList = info;
    }

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
        View view = LayoutInflater.from(context).inflate(R.layout.custome_layout_info, null);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(final CompanyAdapter.DataHolder dataHolder, final int i) {
        final ListInfo c = infoListFiltered.get(i);
        String cateName = c.getName().toString();
        Log.e("TAG", "the name of cate: " + cateName);

        dataHolder.categoryName.setText(c.getName());
        dataHolder.tvNum.setText("" + c.getId());
    }

    @Override
    public int getItemCount() {
        return infoListFiltered.size();
    }


    public class DataHolder extends RecyclerView.ViewHolder {

        TextView categoryName, tvNum;

        public DataHolder(View itemView) {
            super(itemView);

            categoryName = (TextView) itemView.findViewById(R.id.tvName);
            //categoryName.setSelected(true);
            tvNum = (TextView) itemView.findViewById(R.id.tvNum);
            //tvNum.setSelected(true);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Toast.makeText(context,""+infoListFiltered.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();
//
//                    if (MainActivity.cBool == true) {
//                        Intent myIntent = new Intent(context, CompanyDetails.class);
//                        context.startActivity(myIntent);
//                    }
//                    //listener.onInfoSelected(infoListFiltered.get(getAdapterPosition()));
//                }
//            });

        }
    }

    public interface CompanyAdapterListener {
        void onInfoSelected(ListInfo listInfo);
    }
}