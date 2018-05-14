package themedicall.com.Adapter;

/**
 * Created by Muhammad Adeel on 12/22/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import themedicall.com.R;
import java.util.List;

/**
 * Created by User-10 on 19-Dec-17.
 */

public class DaysSelectAdapter extends BaseAdapter {

    private List<String> mListItems;
    private LayoutInflater mLayoutInflater;
    Context context;

    public DaysSelectAdapter(Context context, List<String> arrayList){
        this.context = context;
        mListItems = arrayList;

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
        final DaysSelectAdapter.ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new DaysSelectAdapter.ViewHolder();

            view = mLayoutInflater.inflate(R.layout.custom_status_list, viewGroup, false);

            // get all views you need to handle from the cell and save them in the view holder
            holder.Id = (TextView) view.findViewById(R.id.city_id);
            holder.Name = (TextView) view.findViewById(R.id.city_title);
            holder.imageView = (ImageView) view.findViewById(R.id.blood_icon);
            holder.imageView.setImageResource(R.drawable.dob);


            // save the view holder on the cell view to get it back latter
            view.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (DaysSelectAdapter.ViewHolder)view.getTag();
        }

        //get the string item from the position "position" from array list to put it on the TextView
        String stringItem = mListItems.get(position);
        if (stringItem != null) {
            //set the item name on the TextView
            holder.Name.setText(stringItem);
            //holder.Id.setText(stringItem.getId());

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
        protected ImageView imageView;


    }


}
