package themedicall.com.MediStudyGetterSetter;

import java.util.ArrayList;

/**
 * Created by Muhammad Adeel on 4/17/2018.
 */

public class MediStudyYearMonthGetterSetter {
    private String year;
    private ArrayList<String> monthArray;

    public MediStudyYearMonthGetterSetter(String year, ArrayList<String> monthArray) {
        this.year = year;
        this.monthArray = monthArray;
    }

    public String getYear() {
        return year;
    }

    public ArrayList<String> getMonthArray() {
        return monthArray;
    }
}
