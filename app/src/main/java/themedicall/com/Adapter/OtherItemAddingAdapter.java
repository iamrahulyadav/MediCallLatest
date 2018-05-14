package themedicall.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.Interfaces.AddingNewCategoryInterface;
import themedicall.com.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shoaib Anwar on 18-Jan-18.
 */

public class OtherItemAddingAdapter extends BaseAdapter {

    private List<CitiesGetterSetter> mListItems;
    private List<CitiesGetterSetter> arraySpeciality;
    private LayoutInflater mLayoutInflater;
    Context context;
    private int selectedPosition = -1;

    AddingNewCategoryInterface mAddingNewCategoryInterface;


    public OtherItemAddingAdapter(Context context, List<CitiesGetterSetter> mListItems, AddingNewCategoryInterface addingNewCategoryInterface){
        super();
        this.context = context;
        this.mListItems = mListItems;
        this.arraySpeciality = new ArrayList<CitiesGetterSetter>();
        this.arraySpeciality.addAll(mListItems);
        this.mAddingNewCategoryInterface = addingNewCategoryInterface;

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
        return i;
    }

    @Override

    public View getView(final int position, View view, ViewGroup viewGroup) {


        // create a ViewHolder reference
        final ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();

            view = mLayoutInflater.inflate(R.layout.custome_layout_adding_new_item, viewGroup, false);

            // get all views you need to handle from the cell and save them in the view holder
            holder.Id = (TextView) view.findViewById(R.id.tv_speciality_id);
            holder.radioName = (RadioButton) view.findViewById(R.id.rb_spciality);
            holder.rg_dialog = (RadioGroup) view.findViewById(R.id.rg_dialog);

            // save the view holder on the cell view to get it back latter
            view.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }



        //get the string item from the position "position" from array list to put it on the TextView
        CitiesGetterSetter stringItem = mListItems.get(position);
        if (stringItem != null) {
            //set the item name on the TextView
            holder.radioName.setText(stringItem.getName());
            holder.Id.setText(stringItem.getId());
            final String id = stringItem.getId();
            final String title = stringItem.getName();

            //check the radio button if both position and selectedPosition matches
            holder.radioName.setChecked(position == selectedPosition);

            holder.radioName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemCheckChanged(position, id, title);
                }
            });


        } else {
            // make sure that when you have an if statement that alters the UI, you always have an else that sets a default value back, otherwise you will find that the recycled items will have the same UI changes
            holder.Id.setText("Unknown");
            holder.radioName.setText("Unknown");


        }


        //this method must return the view corresponding to the data at the specified position.
        return view;

    }

    private class ViewHolder {

        protected RadioButton radioName;
        protected TextView Id;
        protected RadioGroup rg_dialog;

    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(int position, String id, String title) {
        selectedPosition = position;
        mAddingNewCategoryInterface.setValues(id, title);
        notifyDataSetChanged();
    }



}
