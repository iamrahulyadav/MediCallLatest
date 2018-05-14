package themedicall.com.Adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import themedicall.com.GetterSetter.BlogHeadingGetterSetter;
import themedicall.com.R;


import java.util.ArrayList;
import java.util.List;

public class BlogHeadingAdapter extends RecyclerView.Adapter<BlogHeadingAdapter.MyViewHolder> {


    private List<BlogHeadingGetterSetter> postList;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public BlogHeadingAdapter(Context context, List<BlogHeadingGetterSetter> adList) {
        this.postList = adList;
        this.mContext = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle , titleId;
        public RecyclerView recycler_view_list;
        ArrayList allPostList;



        public MyViewHolder(final View view) {
            super(view);



            titleId = (TextView) view.findViewById(R.id.titleId);
            itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            allPostList = new ArrayList();

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        Log.e("TAg", "the view type : " + viewType);

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);


                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.progress_item_at_end, parent, false);
                viewHolder = new MyViewHolder(v2);
                break;

        }


        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {


            BlogHeadingGetterSetter ad = postList.get(position);

            switch (getItemViewType(position)) {

                case ITEM:


                    holder.titleId.setText(ad.getTitleId());
                    holder.itemTitle.setText(ad.getTitleName());
                    holder.allPostList = postList.get(position).getAllBlogInCategory();

                    String categoryTitle = holder.itemTitle.getText().toString();

                    BlogPostAdapter blogSectionListDataAdapter = new BlogPostAdapter(mContext, holder.allPostList , categoryTitle);

                    holder.recycler_view_list.setHasFixedSize(true);
                    holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    holder.recycler_view_list.setAdapter(blogSectionListDataAdapter);
                    holder.recycler_view_list.setNestedScrollingEnabled(false);


                    break;

                case LOADING:
                    //do Nothing
                    break;
            }

        }


    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public int getItemViewType(int position) {


        return (position == postList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    //
//
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void add(BlogHeadingGetterSetter mc) {
        postList.add(mc);
        notifyItemInserted(postList.size() - 1);
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        notifyDataSetChanged();
        add(new BlogHeadingGetterSetter());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = postList.size() - 1;
        BlogHeadingGetterSetter item = getItem(position);

        if (item != null) {
            postList.remove(position);

        }
    }

    public BlogHeadingGetterSetter getItem(int position) {
        return postList.get(position);
    }

    public void addAll(List<BlogHeadingGetterSetter> mcList) {
        for (BlogHeadingGetterSetter mc : mcList) {
            add(mc);
        }
    }
    //

    @NonNull
    private MyViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        MyViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.custom_blog_heading_list, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

}