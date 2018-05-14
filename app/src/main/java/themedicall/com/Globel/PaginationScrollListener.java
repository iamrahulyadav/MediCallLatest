package themedicall.com.Globel;

/**
 * Created by User-10 on 30-Nov-17.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;
    int totalResults;

    public PaginationScrollListener(LinearLayoutManager layoutManager, int total) {
        this.layoutManager = layoutManager;
        this.totalResults = total;
    }



    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        Log.e("tag" , "total doctor in pagination scrool: "+totalResults );

        int visibleItemCount = layoutManager.getChildCount();
        Log.e("TAG", "visisble item count: " + visibleItemCount);
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        Log.e("TAG", "child item count: " + totalItemCount);
        Log.e("TAG", "child item count total: " + totalItemCount);
        if (totalResults-1 == totalItemCount){



        }
        else {

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= getTotalPageCount()) {
                loadMoreItems();
            }
        }
        }

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);


    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}