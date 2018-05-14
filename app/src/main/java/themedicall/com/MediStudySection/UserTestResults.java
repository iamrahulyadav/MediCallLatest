package themedicall.com.MediStudySection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Muhammad Adeel on 4/4/2018.
 */

public class UserTestResults implements Serializable {

    HashMap<Integer,Integer> numberOfCorrectAnswers = new HashMap<>(49);
    HashMap<Integer,Integer> numberOfWrongAnswers = new HashMap<>(49);

    public LinkedHashMap<Integer , String> selectedAnswerText = new LinkedHashMap<>(49);
    public LinkedHashMap<Integer , String> selectedSkipAnswerText = new LinkedHashMap<>(49);

    public LinkedHashMap<Integer , String> selectedViewAnswer = new LinkedHashMap<>(49);







    public void setNumberOfCorrectAnswers(HashMap<Integer,Integer> numberOfCorrectAnswers) {
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
    }

    public void setNumberOfWrongAnswers(HashMap<Integer,Integer> numberOfWrongAnswers) {
        this.numberOfWrongAnswers = numberOfWrongAnswers;
    }

    public HashMap<Integer, String> getSelectedAnswerText() {
        return selectedAnswerText;
    }

    public void setSelectedAnswerText(LinkedHashMap<Integer, String> selectedAnswerText) {
        this.selectedAnswerText = selectedAnswerText;
    }
}
