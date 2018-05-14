package themedicall.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import themedicall.com.GetterSetter.HospitalSearchFilterGetterSetter;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 1/17/2018.
 */

public class HospitalFilterAdapterForPrecticeUpdateDoc extends BaseAdapter implements Filterable {
    private Context context;
    private List<HospitalSearchFilterGetterSetter> beanList;
    private LayoutInflater inflater;
    List<HospitalSearchFilterGetterSetter> mStringFilterList;
    ValueFilter valueFilter;


    public HospitalFilterAdapterForPrecticeUpdateDoc(Context context, List<HospitalSearchFilterGetterSetter> beanList) {
        this.context = context;
        this.beanList = beanList;
        mStringFilterList = beanList;
    }


    @Override
    public int getCount() {
        if(beanList != null){
            return beanList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return beanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.custom_hospital_filter_layout, null);
        }

        final TextView hos_id = (TextView) view.findViewById(R.id.hos_id_filter);
        TextView hos_name = (TextView) view.findViewById(R.id.hos_name_filter);
        TextView hos_address = (TextView) view.findViewById(R.id.hos_address_filter);

        try {

            HospitalSearchFilterGetterSetter bean = beanList.get(i);
            String hosId = bean.getId();
            String hosName = bean.getName();
            String hosAddress = bean.getAddress();

            hos_id.setText(hosId);
            hos_name.setText(hosName);
            hos_address.setText(hosAddress);


        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }



        return view;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<HospitalSearchFilterGetterSetter> filterList = new ArrayList<HospitalSearchFilterGetterSetter>();
                for (int i = 0; i < mStringFilterList.size(); i++) {

                    if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {

                        HospitalSearchFilterGetterSetter bean = new HospitalSearchFilterGetterSetter(mStringFilterList.get(i).getId() , mStringFilterList.get(i).getName() , mStringFilterList.get(i).getAddress());
                        filterList.add(bean);

                    }
                }




                results.count = filterList.size();
                results.values = filterList;


            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            beanList = (ArrayList<HospitalSearchFilterGetterSetter>) results.values;
            notifyDataSetChanged();
        }

    }
}
