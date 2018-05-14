package themedicall.com.MediPediaAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import themedicall.com.MediPediaGetterSetter.ListInfo;
import themedicall.com.MediaPediaSection.BrandPage;
import themedicall.com.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 3/2/2018.
 */

public class FindBrandAdapter extends RecyclerView.Adapter<FindBrandAdapter.DataHolder> {

    public static List<ListInfo> infoList;
    private List<ListInfo> infoListFiltered;
    private InfoAdapterListener listener;
    Context context;
    View.OnClickListener onClickListener;
    OnitemClickListener onitemClickListener;
    View itemView;

    public FindBrandAdapter(Context context, ArrayList<ListInfo> info) {
        this.context = context;
        this.infoList = info;
        this.infoListFiltered = infoList;
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
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.find_brand_row, viewGroup, false);

        return new DataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int i) {
        final ListInfo c = infoListFiltered.get(i);
        String cateName = c.getName().toString();
        Log.e("TAG", "the name of cate: " + cateName);

        holder.categoryName.setText(c.getName());
    }

    @Override
    public int getItemCount() {
        return infoListFiltered.size();
    }


    public class DataHolder extends RecyclerView.ViewHolder {

        TextView categoryName, tvNum;

        public DataHolder(View itemView) {
            super(itemView);

            categoryName = (TextView) itemView.findViewById(R.id.brandRowName);
            categoryName.setSelected(true);
            //tvNum = (TextView) itemView.findViewById(R.id.tvNum);
            //tvNum.setSelected(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context,""+infoListFiltered.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context  , BrandPage.class);
                    context.startActivity(intent);
                }
            });

        }
    }

    public interface InfoAdapterListener {
        void onInfoSelected(ListInfo listInfo);
    }
}