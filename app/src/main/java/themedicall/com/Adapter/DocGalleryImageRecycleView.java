package themedicall.com.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import themedicall.com.GetterSetter.DocImageGalleryGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;
import themedicall.com.R;
import java.util.ArrayList;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class DocGalleryImageRecycleView extends RecyclerView.Adapter<DocGalleryImageRecycleView.MyViewHolder>  {
    private Context mContext;

    private ArrayList<DocImageGalleryGetterSetter> docImageList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView docGalleryImg ;

        public MyViewHolder(final View view) {
            super(view);

            docGalleryImg = (ImageView) view.findViewById(R.id.docGalleryImage);

        }
    }

    public DocGalleryImageRecycleView(Context context, ArrayList<DocImageGalleryGetterSetter> adList) {
        this.docImageList = adList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc_image_gallery_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DocImageGalleryGetterSetter ad = docImageList.get(position);

        Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL + "doctors/" + docImageList.get(position).getDocGalleryImg()).transform(new CircleTransformPicasso()).into(holder.docGalleryImg);
    }

    @Override
    public int getItemCount() {
        return docImageList.size();
    }

}
