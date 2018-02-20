package com.example.bi.voicetolist5.Recycler;

import android.support.v7.widget.RecyclerView;

/**
 * Created by bi on 25-Aug-17.
 */

//maybe ue this to resolve the scroll issue in recyclerview
//https://stackoverflow.com/questions/34715732/how-to-scroll-up-down-of-bottom-bar-on-scrolling-of-recyclerview
public class HideShowScroll extends RecyclerView.OnScrollListener {



    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }
}
