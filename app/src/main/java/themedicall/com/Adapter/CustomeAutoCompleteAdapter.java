package themedicall.com.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by Shoaib anwar on 21-Dec-17.
 */

public class CustomeAutoCompleteAdapter extends ArrayAdapter<CitiesGetterSetter> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<CitiesGetterSetter> items, tempItems, suggestions;

    public CustomeAutoCompleteAdapter(Context context, int resource, int textViewResourceId, ArrayList<CitiesGetterSetter> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<CitiesGetterSetter>(items); // this makes the difference.
        suggestions = new ArrayList<CitiesGetterSetter>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_item, parent, false);

            holder.Id = (TextView) view.findViewById(R.id.city_id);
            holder.Name = (TextView) view.findViewById(R.id.city_title);
            holder.bloodImage = (ImageView) view.findViewById(R.id.blood_icon);
            holder.bloodImage.setVisibility(View.GONE);
            view.setTag(holder);





        }
        else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }

        CitiesGetterSetter people = items.get(position);
        if (people != null) {
            if (holder.Name != null)
                holder.Name.setText(people.getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((CitiesGetterSetter) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CitiesGetterSetter people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }

                SharedPreferences sharedPreferences = context.getSharedPreferences("usercrad", 0);
                if (sharedPreferences!=null){

                    String userId = sharedPreferences.getString("userid", null);
                    if (userId!=null){
                        String verifyStatus = sharedPreferences.getString("verified_status", null);
                        if (verifyStatus.equals("1")) {
                            CitiesGetterSetter CTGS = new CitiesGetterSetter("-1", "Other");
                            suggestions.add(CTGS);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try{
                ArrayList<CitiesGetterSetter> filterList = (ArrayList<CitiesGetterSetter>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (CitiesGetterSetter people : filterList) {
                        add(people);
                        notifyDataSetChanged();
                    }
                }
            }catch (ConcurrentModificationException e){
                e.printStackTrace();
            }
        }
    };

    private class ViewHolder {

        protected TextView Name;
        protected TextView Id;
        protected ImageView bloodImage;


    }
}



