package themedicall.com.MediStudyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.MediStudyGetterSetter.MediStudyCoreGetterSetter;
import themedicall.com.R;

/**
 * Created by Muhammad Adeel on 4/12/2018.
 */

public class MediStudyCoreAdapter extends BaseAdapter {
    private Context context;
    private List<MediStudyCoreGetterSetter> coreList;
    private LayoutInflater inflater;


    public MediStudyCoreAdapter(Context context, List<MediStudyCoreGetterSetter> list) {
        this.context = context;
        this.coreList = list;
    }


    @Override
    public int getCount() {
        return coreList.size();
    }

    @Override
    public Object getItem(int i) {
        return coreList.get(i);
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

        MediStudyCoreGetterSetter bean = coreList.get(i);
        String name = bean.getName();
        String id = bean.getId();

        txtName.setText(name);
        txtId.setText(id);
        return view;
    }

}
