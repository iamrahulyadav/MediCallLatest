package themedicall.com.MediStudyGetterSetter;

/**
 * Created by Muhammad Adeel on 4/4/2018.
 */

public class MediStudyQuestionGetterSetter {

    private String questionId ;
    private String question ;
    private String explanation ;
    private String optionA ;
    private String optionB ;
    private String optionC ;
    private String optionD ;
    private String optionE ;
    private String optionF ;
    private String optionG ;
    private String optionH ;
    private String correctAnswer ;

    public MediStudyQuestionGetterSetter(String questionId, String question, String explanation, String optionA, String optionB, String optionC, String optionD, String optionE, String optionF, String optionG, String optionH, String correctAnswer) {
        this.questionId = questionId;
        this.question = question;
        this.explanation = explanation;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.optionE = optionE;
        this.optionF = optionF;
        this.optionG = optionG;
        this.optionH = optionH;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public String getOptionF() {
        return optionF;
    }

    public String getOptionG() {
        return optionG;
    }

    public String getOptionH() {
        return optionH;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
