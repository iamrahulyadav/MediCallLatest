package themedicall.com.GetterSetter;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muhammad Adeel on 12/14/2017.
 */

public class HospitalTimingGetterSetter implements Parcelable {
    private String hospitalDayRow ;
    private String hospitalTimingRow ;

    public HospitalTimingGetterSetter(String hospitalDayRow, String hospitalTimingRow) {
        this.hospitalDayRow = hospitalDayRow;
        this.hospitalTimingRow = hospitalTimingRow;
    }

    protected HospitalTimingGetterSetter(Parcel in) {
        hospitalTimingRow = in.readString();
        hospitalDayRow = in.readString();

    }

    public String getHospitalDayRow() {
        return hospitalDayRow;
    }

    public String getHospitalTimingRow() {
        return hospitalTimingRow;
    }

    public static final Creator<HospitalTimingGetterSetter> CREATOR = new Creator<HospitalTimingGetterSetter>() {
        @Override
        public HospitalTimingGetterSetter createFromParcel(Parcel in) {
            return new HospitalTimingGetterSetter(in);
        }

        @Override
        public HospitalTimingGetterSetter[] newArray(int size) {
            return new HospitalTimingGetterSetter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hospitalDayRow);
        parcel.writeString(hospitalTimingRow);
    }
}
