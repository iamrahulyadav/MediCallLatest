package themedicall.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import themedicall.com.DoctorDetail;
import themedicall.com.GetterSetter.DoctorSearchFilterGetterSetter;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Muhammad Adeel on 1/17/2018.
 */

public class DoctorsFilterAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<DoctorSearchFilterGetterSetter> beanList;
    private LayoutInflater inflater;
    List<DoctorSearchFilterGetterSetter> mStringFilterList;
    ValueFilter valueFilter;
    DoctorSearchFilterGetterSetter bean ;
    public DoctorsFilterAdapter(Context context, List<DoctorSearchFilterGetterSetter> beanList) {
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
            view = inflater.inflate(R.layout.custom_doctors_filter_layout, null);
        }

        final TextView doc_id = (TextView) view.findViewById(R.id.doc_id_filter);
        TextView doc_name = (TextView) view.findViewById(R.id.doc_name_filter);
        TextView doc_speciality = (TextView) view.findViewById(R.id.doc_speciality_filter);

        if(beanList!= null && beanList.size() !=0) {
            bean = beanList.get(i);
        }
        else
        {
            Log.e("tag" , "list is empty");
        }
        String hosId = bean.getId();
        String hosName = bean.getName();
        String hosAddress = bean.getSpeciality();

        doc_id.setText(hosId);
        doc_name.setText(hosName);
        doc_speciality.setText(hosAddress);

        view.setOnClickListener(new View.OnClickListener() {
            String id = doc_id.getText().toString();
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , DoctorDetail.class);
                intent.putExtra("docId" , id);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
                //Toast.makeText(view.getContext() , "doc id "+id, Toast.LENGTH_SHORT).show();
            }
        });

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
                ArrayList<DoctorSearchFilterGetterSetter> filterList = new ArrayList<DoctorSearchFilterGetterSetter>();
                for (int i = 0; i < mStringFilterList.size(); i++) {

                    if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {

                        DoctorSearchFilterGetterSetter bean = new DoctorSearchFilterGetterSetter(mStringFilterList.get(i).getId() , mStringFilterList.get(i).getName() , mStringFilterList.get(i).getSpeciality());
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

            try {

                beanList = (ArrayList<DoctorSearchFilterGetterSetter>) results.values;
                notifyDataSetChanged();

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

    }
}
