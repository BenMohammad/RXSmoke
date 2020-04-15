package com.benmohammad.rxsmoke.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.marlonlom.utilities.timeago.TimeAgo;

public class Utils {

    public static final String NEW_API = "NewApi";

    public static final void loadRoundImage(final Context context, String url, final ImageView iv) {
        Glide.with(context).asBitmap()
                .load(url)
                .into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static String timeStampRelativeToCurrentTime(long epoch) {
        return TimeAgo.using(epoch);
    }

    public static void setTintedVectorAsset(Context context, @DrawableRes int drawableVectorRes, ImageView imageView) {
        imageView.setImageDrawable(getTintedVectorAsset(context, drawableVectorRes));
    }

    public static void setTintedVectorAsset(Context context, @DrawableRes int drawableVectorRes, ImageView imageView, @ColorRes int colorRes) {
        imageView.setImageDrawable(getTintedVectorAsset(context, drawableVectorRes, colorRes));
    }

    public static Drawable getTintedVectorAsset(Context context, @DrawableRes int drawableVectorRes) {
        VectorDrawableCompat nonWhite = VectorDrawableCompat.create(context.getResources(),
                drawableVectorRes, context.getTheme());
        Drawable white  = DrawableCompat.wrap(nonWhite);
        return white;
    }

    public static Drawable getTintedVectorAsset(Context context, @DrawableRes int drawableVectorRes, @ColorRes int colorRes) {
        VectorDrawableCompat nonWhite = VectorDrawableCompat.create(context.getResources(),
                drawableVectorRes, context.getTheme());
        Drawable white = DrawableCompat.wrap(nonWhite);
        DrawableCompat.setTint(white, ContextCompat.getColor(context, colorRes));
        return white;
    }


    @SuppressLint(NEW_API)
    public static void captureTransitionExplode(ViewGroup rootView){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(rootView, new Explode());
        }
    }

    @SuppressLint(NEW_API)
    public static void captureTransitionSlide(ViewGroup rootView) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(rootView, new Slide());
        }
    }

    @SuppressLint(NEW_API)
    public static void captureTransitionSlideRightToLeft(ViewGroup rootView) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(rootView, new Slide(Gravity.END));
        }
    }

    @SuppressLint(NEW_API)
    public static void captureTransitionFade(ViewGroup rootView) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(rootView, new Fade());
        }
    }
}
