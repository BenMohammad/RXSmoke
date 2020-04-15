package com.benmohammad.rxsmoke.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.benmohammad.rxsmoke.R;

public class TagView extends AppCompatTextView {

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAtr) {
        super(context, attrs, defStyleAtr);
    }

    public static TagView formView(ViewGroup viewGroup, String text) {
        @LayoutRes int layoutId = R.layout.layout_tag_view;
        TagView itemView = (TagView) LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutId, viewGroup, false);
        itemView.setText(text);
        return itemView;
    }
}
