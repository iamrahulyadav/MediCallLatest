package themedicall.com.GetterSetter;

import java.util.ArrayList;

/**
 * Created by pratap.kesaboyina on 30-11-2015.
 */
public class BlogHeadingGetterSetter {


    private String titleId;
    private String titleName;
    private ArrayList<BlogPostGetterSetter> allBlogInCategory;

    public BlogHeadingGetterSetter() {

    }

    public BlogHeadingGetterSetter(String titleId, String titleName, ArrayList<BlogPostGetterSetter> allBlogInCategory) {
        this.titleId = titleId;
        this.titleName = titleName;
        this.allBlogInCategory = allBlogInCategory;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public ArrayList<BlogPostGetterSetter> getAllBlogInCategory() {
        return allBlogInCategory;
    }

    public void setAllBlogInCategory(ArrayList<BlogPostGetterSetter> allBlogInCategory) {
        this.allBlogInCategory = allBlogInCategory;
    }
}
