package themedicall.com.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import themedicall.com.GetterSetter.ChugtaiLabGetterSetter;
import themedicall.com.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Adeel on 1/17/2018.
 */

public class ChugtaiLabFilterAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<ChugtaiLabGetterSetter> beanList;
    private LayoutInflater inflater;
    List<ChugtaiLabGetterSetter> mStringFilterList;
    ValueFilter valueFilter;
    ChugtaiLabGetterSetter bean ;

    public ChugtaiLabFilterAdapter(Context context, List<ChugtaiLabGetterSetter> beanList) {
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
            view = inflater.inflate(R.layout.custom_chugtai_lab_filter_layout, null);
        }

        TextView lab_test_id = (TextView) view.findViewById(R.id.lab_test_id);
        TextView id = (TextView) view.findViewById(R.id.id);
        TextView lab_test_name = (TextView) view.findViewById(R.id.lab_test_name);
        TextView lab_test_rate = (TextView) view.findViewById(R.id.lab_test_rate);

        if(beanList!= null && beanList.size() !=0) {
            bean = beanList.get(i);
        }
        else
        {
            Log.e("tag" , "list is empty");
        }
        String labTestId = bean.getLab_test_id();
        String labId = bean.getId();
        String labTestName = bean.getLab_test_name();
        String labTestRate = bean.getLab_test_rate();

        lab_test_id.setText(labTestId);
        id.setText(labId);
        lab_test_name.setText(labTestName);
        lab_test_rate.setText(labTestRate);

//        view.setOnClickListener(new View.OnClickListener() {
//            String id = hos_id.getText().toString();
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext() , HospitalDetail.class);
//                intent.putExtra("hospital_id" , id);
//                view.getContext().startActivity(intent);
//                //Toast.makeText(view.getContext() , "hos id "+id, Toast.LENGTH_SHORT).show();
//            }
//        });

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
                ArrayList<ChugtaiLabGetterSetter> filterList = new ArrayList<ChugtaiLabGetterSetter>();
                for (int i = 0; i < mStringFilterList.size(); i++) {

                    if ((mStringFilterList.get(i).getLab_test_name().toUpperCase()).contains(constraint.toString().toUpperCase())) {

                        ChugtaiLabGetterSetter bean = new ChugtaiLabGetterSetter(mStringFilterList.get(i).getLab_test_id() , mStringFilterList.get(i).getId() , mStringFilterList.get(i).getLab_test_name(), mStringFilterList.get(i).getLab_test_rate());
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
            beanList = (ArrayList<ChugtaiLabGetterSetter>) results.values;
            notifyDataSetChanged();
        }

    }
}
