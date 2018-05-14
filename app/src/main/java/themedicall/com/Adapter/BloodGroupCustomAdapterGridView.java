package themedicall.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import themedicall.com.R;


/**
 * Created by Muhammad Adeel on 8/21/2017.
 */

public class BloodGroupCustomAdapterGridView extends BaseAdapter {


    private Context mContext;
    private final int[] gridViewBloodGroupImageId;

    public BloodGroupCustomAdapterGridView(Context context, int[] gridViewImageId) {
        mContext = context;
        this.gridViewBloodGroupImageId = gridViewImageId;
    }

    @Override
    public int getCount() {
        return gridViewBloodGroupImageId.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position % 2;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layoutResource = 0; // determined by view type
        int viewType = getItemViewType(i);

        switch (viewType) {
            case 0:
                layoutResource = R.layout.odd_blood_group_gridview_row;
                break;

            case 1:
                layoutResource = R.layout.even_blood_group_gridview_row;
                break;
        }

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(layoutResource, null);

        } else {
            gridViewAndroid = (View) convertView;
        }

        ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.blood_group_gridview_image);
        imageViewAndroid.setImageResource(gridViewBloodGroupImageId[i]);
        return gridViewAndroid;
    }
}
