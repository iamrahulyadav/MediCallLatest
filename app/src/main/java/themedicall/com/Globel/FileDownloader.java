package themedicall.com.Globel;

import android.app.ProgressDialog;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Muhammad Adeel on 4/30/2018.
 */

public class FileDownloader {
    private static final int MEGABYTE = 1024 * 1024;

    public static void downloadFile(String fileUrl, File directory, ProgressDialog progressDialog) {
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            int fileLength = urlConnection.getContentLength();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);

            byte[] buffer = new byte[MEGABYTE];
            long total = 0;
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                total += bufferLength;
                int currentValue= (int) (total * 100 / fileLength);
                progressDialog.setProgress(currentValue);
                Log.e("tag", "custom progress bar is : "+currentValue);
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}