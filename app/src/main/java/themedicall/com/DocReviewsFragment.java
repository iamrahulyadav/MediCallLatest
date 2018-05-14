package themedicall.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import themedicall.com.Adapter.ReviewsListRecycleView;
import themedicall.com.GetterSetter.ReviewsGetterSetter;

import java.util.ArrayList;
import java.util.List;


public class DocReviewsFragment extends Fragment {

    RecyclerView recyclerView_reviews;
    List<ReviewsGetterSetter> reviewsList;

    public DocReviewsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_doc_reviews, container, false);


        recyclerView_reviews = (RecyclerView) view.findViewById(R.id.recycler_view_reviews);
        recyclerView_reviews.setHasFixedSize(true);
        recyclerView_reviews.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
        reviewsList = new ArrayList<>();

        prepareReviewsList();

        return view;
    }


    public void prepareReviewsList()
    {
        reviewsList.add(new ReviewsGetterSetter("1" , "Muhammad Adeel" , R.drawable.doctor , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "2 month ago"));
        reviewsList.add(new ReviewsGetterSetter("1" , "Muhammad Adeel" , R.drawable.doctor , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "2 month ago"));
        reviewsList.add(new ReviewsGetterSetter("1" , "Muhammad Adeel" , R.drawable.doctor , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "2 month ago"));
        reviewsList.add(new ReviewsGetterSetter("1" , "Muhammad Adeel" , R.drawable.doctor , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "2 month ago"));
        reviewsList.add(new ReviewsGetterSetter("1" , "Muhammad Adeel" , R.drawable.doctor , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "2 month ago"));
        reviewsList.add(new ReviewsGetterSetter("1" , "Muhammad Adeel" , R.drawable.doctor , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "2 month ago"));
        reviewsList.add(new ReviewsGetterSetter("1" , "Muhammad Adeel" , R.drawable.doctor , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" +
                "" , "2 month ago"));

        ReviewsListRecycleView reviewsListRecycleView = new ReviewsListRecycleView(reviewsList);
        recyclerView_reviews.setAdapter(reviewsListRecycleView);
        reviewsListRecycleView.notifyDataSetChanged();



    }

}
