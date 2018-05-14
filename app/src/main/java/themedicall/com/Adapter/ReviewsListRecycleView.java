package themedicall.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import themedicall.com.GetterSetter.ReviewsGetterSetter;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class ReviewsListRecycleView extends RecyclerView.Adapter<ReviewsListRecycleView.MyViewHolder>  {

    private List<ReviewsGetterSetter> reviewsList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView reviewRowVerifiedImg ;
        public TextView reviewRowId , reviewRowName , reviewRowReview , reviewRowMonthAgo ;

        public MyViewHolder(final View view) {
            super(view);


            reviewRowVerifiedImg = (ImageView) view.findViewById(R.id.reviewRowVerifiedImg);
            reviewRowId = (TextView) view.findViewById(R.id.reviewRowId);
            reviewRowName = (TextView) view.findViewById(R.id.reviewRowName);
            reviewRowReview = (TextView) view.findViewById(R.id.reviewRowReview);
            reviewRowMonthAgo = (TextView) view.findViewById(R.id.reviewRowMonthAgo);



//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(v.getContext() , HappyUserDetail.class);
//                    v.getContext().startActivity(intent);
//
//                    String id = happyUserId.getText().toString();
//                    Toast.makeText(v.getContext(), "id "+id, Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }

    public ReviewsListRecycleView(List<ReviewsGetterSetter> adList) {
        this.reviewsList = adList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReviewsGetterSetter ad = reviewsList.get(position);

        holder.reviewRowVerifiedImg.setImageResource(ad.getReviewsRowVerifiedImg());
        holder.reviewRowId.setText(ad.getReviewsRowId());
        holder.reviewRowName.setText(ad.getReviewsRowName());
        holder.reviewRowReview.setText(ad.getReviewsRowReview());
        holder.reviewRowMonthAgo.setText(ad.getReviewRowMonthAgo());


        holder.reviewRowId.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

}
