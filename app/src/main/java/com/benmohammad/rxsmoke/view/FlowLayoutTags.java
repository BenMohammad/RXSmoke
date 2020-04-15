package com.benmohammad.rxsmoke.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayoutTags extends ViewGroup {

    private int mLineHeight;
    private int mVerticalSpacing;
    private int mHorizontalSpacing;

    public FlowLayoutTags(Context context) {
        super(context);
        init(context, null);
    }

    public FlowLayoutTags(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mVerticalSpacing =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        mHorizontalSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();

        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int lineHeight = 0;
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();

        int childHeightMeasureSpec;
        if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if(child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), childHeightMeasureSpec);
                final int childMeasureWidth = child.getMeasuredWidth() + child.getPaddingLeft() + child.getPaddingRight();
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + mVerticalSpacing);

                if(xPos + childMeasureWidth > width) {
                    xPos = getPaddingLeft();
                    yPos += lineHeight;
                }

                xPos += childMeasureWidth + mHorizontalSpacing;
            }
        }

        this.mLineHeight = lineHeight;

        if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = yPos + lineHeight;
        } else if((MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) && (yPos + lineHeight < height)) {
            height = yPos + lineHeight;
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - 1;
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();

        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if(child.getVisibility() != View.GONE) {
                final int childMeasuredWidth = child.getMeasuredWidth();
                final int childMeasuredHeight = child.getMeasuredHeight();
                if(xPos + childMeasuredWidth > width) {
                    xPos = getPaddingLeft();
                    yPos += mLineHeight;
                }

                child.layout(xPos, yPos, xPos + childMeasuredWidth, yPos + childMeasuredHeight);
                xPos += childMeasuredWidth + mHorizontalSpacing;
            }
        }
    }
}
