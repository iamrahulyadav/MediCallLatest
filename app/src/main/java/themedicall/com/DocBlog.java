package themedicall.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import themedicall.com.Adapter.BlogHeadingAdapter;
import themedicall.com.GetterSetter.BlogHeadingGetterSetter;
import themedicall.com.GetterSetter.BlogPostGetterSetter;


import java.util.ArrayList;


public class DocBlog extends Fragment {

    ArrayList<BlogHeadingGetterSetter> allSampleData;
    public DocBlog() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doc_blog, container, false);

        allSampleData = new ArrayList<BlogHeadingGetterSetter>();

        RecyclerView doc_blog_recycler_view = (RecyclerView) view.findViewById(R.id.doc_blog_recycler_view);

        doc_blog_recycler_view.setHasFixedSize(true);

        BlogHeadingAdapter adapter = new BlogHeadingAdapter(getContext() , allSampleData);

        doc_blog_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        doc_blog_recycler_view.setAdapter(adapter);

        createDummyData();


        return view;
    }

    public void createDummyData() {
        for (int i = 1; i <= 5; i++) {

            BlogHeadingGetterSetter dm = new BlogHeadingGetterSetter();

            dm.setTitleName("Section " + i);

            ArrayList<BlogPostGetterSetter> singleItem = new ArrayList<BlogPostGetterSetter>();
            for (int j = 0; j <= 5; j++) {
                singleItem.add(new BlogPostGetterSetter("Item " + j, "URL " + j , "fdf" + j));
            }

            dm.setAllBlogInCategory(singleItem);

            allSampleData.add(dm);

        }
    }
}
