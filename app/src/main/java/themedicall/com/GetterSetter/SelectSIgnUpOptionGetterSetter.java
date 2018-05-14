package themedicall.com.GetterSetter;

/**
 * Created by Muhammad Adeel on 8/30/2017.
 */

public class SelectSIgnUpOptionGetterSetter {
    private int SignUpOptionImg ;
    private String SignUpOptionId;
    private String SignUpOptionName;

    public SelectSIgnUpOptionGetterSetter(int signUpOptionImg, String signUpOptionId, String signUpOptionName) {
        SignUpOptionImg = signUpOptionImg;
        SignUpOptionId = signUpOptionId;
        SignUpOptionName = signUpOptionName;
    }

    public int getSignUpOptionImg() {
        return SignUpOptionImg;
    }

    public String getSignUpOptionId() {
        return SignUpOptionId;
    }

    public String getSignUpOptionName() {
        return SignUpOptionName;
    }
}
