package themedicall.com.MediStudyAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import themedicall.com.Globel.CustomTextView;
import themedicall.com.Globel.FileDownloader;
import themedicall.com.Globel.Glob;
import themedicall.com.MediStudyGetterSetter.MediStudyDownloadBookGetterSetter;
import themedicall.com.MediStudyGetterSetter.MediStudyFlashSpotSlidesGetterSetter;
import themedicall.com.R;

/**
 * Created by Muhammad Adeel on 2/13/2018.
 */

public class MediStudyFlashSpotSlidesAdapter extends RecyclerView.Adapter<MediStudyFlashSpotSlidesAdapter.MyViewHolder>  {

    private List<MediStudyFlashSpotSlidesGetterSetter> flashSpotSlidesList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private ProgressDialog progressDialog;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView flashSpotSlideImage , chainOne , chainTwo;
        public TextView flashSpotSlideId , flashSpotSlideImageUrl ,  flashSpotSlideDescription  ;
       // public WebView webview ;
        public MyViewHolder(final View view) {
            super(view);


            flashSpotSlideImage = (ImageView) view.findViewById(R.id.flashSpotSlideImage);
            chainOne = (ImageView) view.findViewById(R.id.chainOne);
            chainTwo = (ImageView) view.findViewById(R.id.chainTwo);
            flashSpotSlideId = (TextView) view.findViewById(R.id.flashSpotSlideId);
            flashSpotSlideImageUrl = (TextView) view.findViewById(R.id.flashSpotSlideImageUrl);
            flashSpotSlideDescription = (TextView) view.findViewById(R.id.flashSpotSlideDescription);
            //webview = (WebView) view.findViewById(R.id.flashSpotSlideDescription);





        }
    }


    public MediStudyFlashSpotSlidesAdapter(Context context , List<MediStudyFlashSpotSlidesGetterSetter> appealList) {
        this.mContext = context;
        this.flashSpotSlidesList = appealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.pharmacy_custom_row, parent, false);
//        return new MyViewHolder(itemView);

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        switch (viewType) {
            case ITEM:
                // viewHolder = getViewHolder(parent, inflater);

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medi_study_flash_spot_slide_custom_row, parent, false);
                viewHolder = new MyViewHolder(itemView);

                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.progress_item_at_end, parent, false);
                viewHolder = new MyViewHolder(v2);
                break;

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {


            switch (getItemViewType(position)) {

                case ITEM:
                    MediStudyFlashSpotSlidesGetterSetter ad = flashSpotSlidesList.get(position);
                    holder.flashSpotSlideId.setText(ad.getId());
                    holder.flashSpotSlideImageUrl.setText(ad.getImageUrl());

                    if(ad.getImageUrl().equals("") || ad.getImageUrl().equals("null"))
                    {
                        holder.flashSpotSlideImage.setVisibility(View.GONE);
                        holder.chainOne.setVisibility(View.GONE);
                        holder.chainTwo.setVisibility(View.GONE);
                    }
                    else
                    {
                        Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL + "slides/" + ad.getImageUrl()).into(holder.flashSpotSlideImage);
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        holder.flashSpotSlideDescription.setText(Html.fromHtml(ad.getDescription() ,Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        holder.flashSpotSlideDescription.setText(Html.fromHtml(ad.getDescription()));
                    }

//                    holder.webview.getSettings().setLoadWithOverviewMode(true);
//                    holder.webview.getSettings().setUseWideViewPort(true);
//                    holder.webview.getSettings().setMinimumFontSize(40);
//                    holder.webview.loadData(ad.getDescription() , "text/html", "UTF-8");

                    break;
                case LOADING:
                    //do Nothing
                    break;
            }
        }
    }




    @Override
    public int getItemCount() {
        return flashSpotSlidesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == flashSpotSlidesList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public MediStudyFlashSpotSlidesGetterSetter getItem(int position) {
        return flashSpotSlidesList.get(position);
    }

    public void add(MediStudyFlashSpotSlidesGetterSetter mc) {
        flashSpotSlidesList.add(mc);
        notifyItemInserted(flashSpotSlidesList.size() - 1);
    }

    public void addAll(List<MediStudyFlashSpotSlidesGetterSetter> mcList) {
        for (MediStudyFlashSpotSlidesGetterSetter mc : mcList) {
            add(mc);
        }
    }


}