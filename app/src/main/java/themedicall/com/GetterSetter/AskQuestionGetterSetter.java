package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 8/28/2017.
 */

public class AskQuestionGetterSetter {

    private String askQuesRowId;
    private String askQuesRowQuestion;
    private String askQuesRowName;
    private String askQuesRowAge;
    private String askQuesRowNoOfAns;
    private String askQuesRowDate;

    public AskQuestionGetterSetter(String askQuesRowId, String askQuesRowQuestion, String askQuesRowName, String askQuesRowAge, String askQuesRowNoOfAns, String askQuesRowDate) {
        this.askQuesRowId = askQuesRowId;
        this.askQuesRowQuestion = askQuesRowQuestion;
        this.askQuesRowName = askQuesRowName;
        this.askQuesRowAge = askQuesRowAge;
        this.askQuesRowNoOfAns = askQuesRowNoOfAns;
        this.askQuesRowDate = askQuesRowDate;
    }

    public String getAskQuesRowId() {
        return askQuesRowId;
    }

    public String getAskQuesRowQuestion() {
        return askQuesRowQuestion;
    }

    public String getAskQuesRowName() {
        return askQuesRowName;
    }

    public String getAskQuesRowAge() {
        return askQuesRowAge;
    }

    public String getAskQuesRowNoOfAns() {
        return askQuesRowNoOfAns;
    }

    public String getAskQuesRowDate() {
        return askQuesRowDate;
    }
}
