package com.example.bi.voicetolist5.Recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by bi on 21-Aug-17.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;
    private boolean showFirstDivider = true; //setting this to true add divider that was missing on top
    private boolean showLastDivider = false; //false



    public DividerItemDecoration(Context context, AttributeSet attributes) {
        final TypedArray a = context.obtainStyledAttributes(attributes, new int[android.R.attr.listDivider]);
        divider = a.getDrawable(0);
        a.recycle();
    }


    public DividerItemDecoration(Context context, AttributeSet attributes, boolean showFirstDivider, boolean showLastDivider) {
        this(context, attributes);
        this.showFirstDivider = showFirstDivider;
        this.showLastDivider = showLastDivider;
    }


    public DividerItemDecoration(Drawable divider) {
        this.divider = divider;
    }


    public DividerItemDecoration(Drawable divider, boolean showFirstDivider, boolean showLastDivider) {
        this(divider);
        this.showFirstDivider = showFirstDivider;
        this.showLastDivider = showLastDivider;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (divider == null){
            return;
        }
        if (parent.getChildPosition(view) <1){
            return;
        }
        if (getOrientation (parent) == LinearLayout.VERTICAL){
            outRect.top  = divider.getIntrinsicHeight();
        } else {
            outRect.top = divider.getIntrinsicWidth();
        }
    }



    private int getOrientation (RecyclerView parent){
        if (parent.getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return  layoutManager.getOrientation();
        } else {
            throw new IllegalArgumentException("Divider can only be used with a linearlayout manger");
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (divider == null){
            super.onDraw(c, parent, state);
            return;
        }

        // //initialization needed to advoid compiler warning
        int left =0 , right = 0, top = 0, bottom = 0, size;
        int orientation = getOrientation(parent);
        int childCount = parent.getChildCount();

        if (orientation == LinearLayoutManager.VERTICAL){
            size = divider.getIntrinsicHeight();
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else {
            size = divider.getIntrinsicWidth();
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getBottom();
        }


        for (int i = showFirstDivider ? 0 : 1; i <childCount; i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (orientation == LinearLayoutManager.VERTICAL){
                top = child.getBottom() + layoutParams.bottomMargin;
                bottom = top + size;
            } else {
                left = child.getRight() + layoutParams.rightMargin;
                right = left+size;
            }
            divider.setBounds(left, top,right, bottom);
            divider.draw(c);
        }
    }

}
