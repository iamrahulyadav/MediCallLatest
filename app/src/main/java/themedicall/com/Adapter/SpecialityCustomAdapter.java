package themedicall.com.Adapter;

/**
 * Created by Muhammad Adeel on 12/22/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import themedicall.com.GetterSetter.SelectSpecialityGetterSetter;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by User-10 on 21-Dec-17.
 */

public class SpecialityCustomAdapter extends BaseAdapter {

    private ArrayList<SelectSpecialityGetterSetter> mListItems;
    private ArrayList<String> mSelectedIds;
    private LayoutInflater mLayoutInflater;
    boolean[] itemChecked;
    Context context;

    private ArrayList<SelectSpecialityGetterSetter> arrayListCities;
    private ArrayList<HashMap<String, String>> mSelectedSpecialities;

    public SpecialityCustomAdapter(Context context, ArrayList<SelectSpecialityGetterSetter> arrayList, ArrayList<String> selectedIdList, ArrayList<HashMap<String, String>> selectedSpeciality){
        this.context = context;
        mListItems = arrayList;
        mSelectedIds = selectedIdList;
        this.arrayListCities = new ArrayList<SelectSpecialityGetterSetter>();
        this.arrayListCities.addAll(mListItems);
        itemChecked = new boolean[mListItems.size()];
        this.mSelectedSpecialities = selectedSpeciality;

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
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override

    public View getView(final int position, View view, ViewGroup viewGroup) {


        // create a ViewHolder reference
        final ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();

            view = mLayoutInflater.inflate(R.layout.specialisty_custom_layout_for_dialog, viewGroup, false);

            // get all views you need to handle from the cell and save them in the view holder
            holder.Id = (TextView) view.findViewById(R.id.city_id);
            holder.Name = (TextView) view.findViewById(R.id.city_title);
            holder.cb_specialist = (CheckBox) view.findViewById(R.id.cb_specialist);

            // holder.bloodImage = (ImageView) view.findViewById(R.id.blood_icon);
            //holder.bloodImage.setVisibility(View.GONE)
            // save the view holder on the cell view to get it back latter
            view.setTag(holder);


        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }

        //get the string item from the position "position" from array list to put it on the TextView
        if (mSelectedIds.size()>0){

        }
        SelectSpecialityGetterSetter stringItem = mListItems.get(position);
        if (stringItem != null) {
            //set the item name on the TextView
            holder.Name.setText(stringItem.getSpeciality_name());
            holder.Id.setText(stringItem.getSpeciality_id());

            if (mSelectedSpecialities.size()>0) {
                for (int i = 0; i < mSelectedSpecialities.size(); i++) {
                    String selectedId = mSelectedSpecialities.get(i).get("sp_id");
                    if (stringItem.getSpeciality_id().equals(selectedId)) {
                        holder.cb_specialist.setChecked(true);
                    }

                }


            }

     /*       if (itemChecked[position])
                holder.cb_specialist.setChecked(true);
            else
                holder.cb_specialist.setChecked(false);*/

        } else {
            // make sure that when you have an if statement that alters the UI, you always have an else that sets a default value back, otherwise you will find that the recycled items will have the same UI changes
            holder.Id.setText("Unknown");
            holder.Name.setText("Unknown");
        }


        //*******


        //********

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
        protected CheckBox cb_specialist;
        //protected ImageView bloodImage;


    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mListItems.clear();
        if (charText.length() == 0) {
            mListItems.addAll(arrayListCities);
        } else {
            for (SelectSpecialityGetterSetter wp : arrayListCities) {
                if (wp.getSpeciality_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mListItems.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
