package themedicall.com.MediStudyAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import themedicall.com.GetterSetter.BloodAppealsGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomTextView;
import themedicall.com.Globel.FileDownloader;
import themedicall.com.Globel.Glob;
import themedicall.com.MediStudyGetterSetter.MediStudyDownloadBookGetterSetter;
import themedicall.com.MediStudySection.MediStudyBookDownload;
import themedicall.com.R;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.SEND_SMS;

/**
 * Created by Muhammad Adeel on 2/13/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MediStudyDownloadBookAdapter extends RecyclerView.Adapter<MediStudyDownloadBookAdapter.MyViewHolder>  {

    private List<MediStudyDownloadBookGetterSetter> bookList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private ProgressDialog progressDialog;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mediBookImage , mediBookDownloadImg , mediBookShareImg ;
        public TextView mediBookId , mediImageURL ,  mediBookURL , mediBookName , mediBookAuthorName , mediBookDescription;

        public MyViewHolder(final View view) {
            super(view);


            mediBookImage = (ImageView) view.findViewById(R.id.mediBookImage);
            mediBookDownloadImg = (ImageView) view.findViewById(R.id.mediBookDownloadImg);
            mediBookShareImg = (ImageView) view.findViewById(R.id.mediBookShareImg);
            mediBookId = (TextView) view.findViewById(R.id.mediBookId);
            mediImageURL = (TextView) view.findViewById(R.id.mediImageURL);
            mediBookURL = (TextView) view.findViewById(R.id.mediBookURL);
            mediBookName = (TextView) view.findViewById(R.id.mediBookName);
            mediBookAuthorName = (TextView) view.findViewById(R.id.mediBookAuthorName);
            mediBookDescription = (TextView) view.findViewById(R.id.mediBookDescription);





            mediBookDownloadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String bookUrl = mediBookURL.getText().toString();
                    final String bookName = mediBookName.getText().toString();
                    new DownloadFile().execute(bookUrl, bookName+".pdf");


                }
            });

            mediBookShareImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final String bookUrl = mediBookURL.getText().toString();
                    final String bookName = mediBookName.getText().toString();

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBodyText = "Download "+bookName+" book using this link \n" ;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"MediCall");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText + bookUrl);
                    view.getContext().startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));


                }
            });



        }
    }


    private class DownloadFile extends AsyncTask<String, Void, Void> {
        Drawable drawable = mContext.getDrawable(R.drawable.custom_seekbar); // Example for red gradient
        File pdfFile;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Downloading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgressDrawable(drawable);
            progressDialog.setCancelable(false);
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.dismiss();

            Toast.makeText(mContext , "Book download successfully", Toast.LENGTH_SHORT).show();

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "MediStudy Books");
            folder.mkdir();

            pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile , progressDialog);
            return null;
        }
    }

    public MediStudyDownloadBookAdapter(Context context , List<MediStudyDownloadBookGetterSetter> appealList) {
        this.mContext = context;
        this.bookList = appealList;
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

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medi_study_download_book_custom_row, parent, false);
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
                    MediStudyDownloadBookGetterSetter ad = bookList.get(position);
                    Picasso.with(mContext).load(Glob.FETCH_IMAGE_URL + "books/" + ad.getMediImageURL()).into(holder.mediBookImage);
                    holder.mediBookId.setText(ad.getMediBookId());
                    holder.mediImageURL.setText(ad.getMediImageURL());
                    holder.mediBookURL.setText(ad.getMediBookURL());
                    holder.mediBookName.setText(ad.getMediBookName() + " ("+ad.getSubjectName()+")");
                    holder.mediBookAuthorName.setText("By "+ad.getMediBookAuthorName());
                    holder.mediBookDescription.setText(ad.getMediBookDescription());
                    CustomTextView customTextView = new CustomTextView();
                    customTextView.makeTextViewResizable(holder.mediBookDescription , 2, "View More", true);



                    break;
                case LOADING:
                    //do Nothing
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == bookList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public MediStudyDownloadBookGetterSetter getItem(int position) {
        return bookList.get(position);
    }

    public void add(MediStudyDownloadBookGetterSetter mc) {
        bookList.add(mc);
        notifyItemInserted(bookList.size() - 1);
    }

    public void addAll(List<MediStudyDownloadBookGetterSetter> mcList) {
        for (MediStudyDownloadBookGetterSetter mc : mcList) {
            add(mc);
        }
    }


}