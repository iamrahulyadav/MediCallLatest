package themedicall.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import themedicall.com.GetterSetter.SuggestionGetterSetter;
import themedicall.com.R;
import java.util.ArrayList;

/**
 * Created by Hafiz Adeel on 3/7/2017.
 */

public class SuggestionListCustomAdapter extends BaseAdapter implements Filterable {

    private ArrayList<SuggestionGetterSetter> mListItems;
    private ArrayList<SuggestionGetterSetter> arrayListSuggestion = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    Context context;
    private Filter filter = new CustomFilter();

    public SuggestionListCustomAdapter(Context context, ArrayList<SuggestionGetterSetter> mListItems) {
        super();
        this.context = context;
        this.mListItems = mListItems;

        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return mListItems.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return null;
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(final int position, View view, ViewGroup viewGroup) {


        // create a ViewHolder reference
        final ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();

            view = mLayoutInflater.inflate(R.layout.custom_list_item, viewGroup, false);

            // get all views you need to handle from the cell and save them in the view holder
            holder.Id = (TextView) view.findViewById(R.id.city_id);
            holder.Name = (TextView) view.findViewById(R.id.city_title);


            // save the view holder on the cell view to get it back latter
            view.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) view.getTag();
        }

        //get the string item from the position "position" from array list to put it on the TextView
        SuggestionGetterSetter stringItem = mListItems.get(position);
        if (stringItem != null) {
            //set the item name on the TextView
            holder.Name.setText(stringItem.getName());
            holder.Id.setText(stringItem.getId());

        } else {
            // make sure that when you have an if statement that alters the UI, you always have an else that sets a default value back, otherwise you will find that the recycled items will have the same UI changes
            holder.Id.setText("Unknown");
            holder.Name.setText("Unknown");


        }

        //this method must return the view corresponding to the data at the specified position.
        return view;

    }


    /**
     * Used to avoid calling "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is large
     */
    private class ViewHolder {

        protected TextView Name;
        protected TextView Id;


    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * Our Custom Filter Class.
     */
    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            arrayListSuggestion.clear();

            if (mListItems != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < mListItems.size(); i++) {
                    if (mListItems.get(i).getName().toLowerCase().contains(constraint)) { // Compare item in original list if it contains constraints.
                        arrayListSuggestion.add(mListItems.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }
            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = arrayListSuggestion;
            results.count = arrayListSuggestion.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }



}
