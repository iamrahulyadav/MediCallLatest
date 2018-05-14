package themedicall.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 1/4/2018.
 */

public class CustomCityNewAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<CitiesGetterSetter> beanList;
    private LayoutInflater inflater;
    List<CitiesGetterSetter> mStringFilterList;
    ValueFilter valueFilter;


    public CustomCityNewAdapter(Context context, List<CitiesGetterSetter> beanList) {
        this.context = context;
        this.beanList = beanList;
        mStringFilterList = beanList;
    }


    @Override
    public int getCount() {
        return beanList.size();
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
            view = inflater.inflate(R.layout.custom_list_item, null);
        }

        TextView txtName = (TextView) view.findViewById(R.id.city_title);
        TextView txtId = (TextView) view.findViewById(R.id.city_id);

        CitiesGetterSetter bean = beanList.get(i);
        String name = bean.getName();
        String id = bean.getId();

        txtName.setText(name);
        txtId.setText(id);
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
                ArrayList<CitiesGetterSetter> filterList = new ArrayList<CitiesGetterSetter>();
                for (int i = 0; i < mStringFilterList.size(); i++) {

                    if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {

                        CitiesGetterSetter bean = new CitiesGetterSetter(mStringFilterList.get(i).getId() , mStringFilterList.get(i).getName());
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
            beanList = (ArrayList<CitiesGetterSetter>) results.values;
            notifyDataSetChanged();
        }

    }
}
