package themedicall.com.GetterSetter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muhammad Adeel on 8/28/2017.
 */

public class HospitalServiceGetterSetter implements Parcelable{
    private String hospitalService;

    public HospitalServiceGetterSetter(String hospitalService) {
        this.hospitalService = hospitalService;
    }

    protected HospitalServiceGetterSetter(Parcel in) {
        hospitalService = in.readString();
    }

    public static final Creator<HospitalServiceGetterSetter> CREATOR = new Creator<HospitalServiceGetterSetter>() {
        @Override
        public HospitalServiceGetterSetter createFromParcel(Parcel in) {
            return new HospitalServiceGetterSetter(in);
        }

        @Override
        public HospitalServiceGetterSetter[] newArray(int size) {
            return new HospitalServiceGetterSetter[size];
        }
    };

    public String getHospitalService() {
        return hospitalService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hospitalService);
    }

}
