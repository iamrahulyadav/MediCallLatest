package themedicall.com.GetterSetter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pratap.kesaboyina on 01-12-2015.
 */
public class BlogPostGetterSetter implements Parcelable{


    private String id;
    private String title;
    private String url;

    public BlogPostGetterSetter(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public BlogPostGetterSetter() {

    }


    protected BlogPostGetterSetter(Parcel in) {
        id = in.readString();
        title = in.readString();
        url = in.readString();
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public static final Creator<BlogPostGetterSetter> CREATOR = new Creator<BlogPostGetterSetter>() {
        @Override
        public BlogPostGetterSetter createFromParcel(Parcel in) {
            return new BlogPostGetterSetter(in);
        }

        @Override
        public BlogPostGetterSetter[] newArray(int size) {
            return new BlogPostGetterSetter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(url);
    }
}
