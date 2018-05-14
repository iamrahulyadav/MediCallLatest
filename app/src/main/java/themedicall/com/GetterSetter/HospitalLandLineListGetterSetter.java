package themedicall.com.GetterSetter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muhammad Adeel on 1/22/2018.
 */

public class HospitalLandLineListGetterSetter implements Parcelable{
    private String hos_name ;
    private String hos_land_line_number ;

    public HospitalLandLineListGetterSetter(String hos_name, String hos_land_line_number) {
        this.hos_name = hos_name;
        this.hos_land_line_number  = hos_land_line_number;
    }

    public HospitalLandLineListGetterSetter(String tempList) {
    }

    protected HospitalLandLineListGetterSetter(Parcel in) {
        hos_name = in.readString();
        hos_land_line_number = in.readString();
    }


    public String getHos_name() {
        return hos_name;
    }

    public void setHos_name(String hos_name) {
        this.hos_name = hos_name;
    }

    public String getHos_land_line_number() {
        return hos_land_line_number;
    }

    public void setHos_land_line_number(String hos_land_line_number) {
        this.hos_land_line_number = hos_land_line_number;
    }

    public static final Creator<HospitalLandLineListGetterSetter> CREATOR = new Creator<HospitalLandLineListGetterSetter>() {
        @Override
        public HospitalLandLineListGetterSetter createFromParcel(Parcel in) {
            return new HospitalLandLineListGetterSetter(in);
        }

        @Override
        public HospitalLandLineListGetterSetter[] newArray(int size) {
            return new HospitalLandLineListGetterSetter[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hos_name);
        parcel.writeString(hos_land_line_number);
    }
}
