package com.wentao.messagemanagement.Animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

/**
 * Created by Administrator on 2017/11/30.
 */

public class AnimationUtil {
    private static final String TAG = AnimationUtil.class.getSimpleName();

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(400);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(400);
        return mHiddenAction;
    }


    public static void setAnimationTopToButton(View view) {
        if (view.getVisibility() == View.VISIBLE){
            view.setVisibility(View.GONE);
            view.setAnimation(moveToViewBottom());
        } else {
            view.setVisibility(View.VISIBLE);
            view.setAnimation(moveToViewLocation());
        }
    }

    public static void setAnimationTopToButton(View[] views) {
        for (View v : views) {
            setAnimationTopToButton(v);
        }
    }

    public static void setAnimationLeftToRightGone(View view, Context context) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            view.setAnimation(AnimationUtils.makeOutAnimation(context, true));
        } else {
            view.setVisibility(View.VISIBLE);
            view.setAnimation(AnimationUtils.makeInAnimation(context, false));
        }
    }
    public static void setAnimationRightToLeftGone(View view, Context context) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            view.setAnimation(AnimationUtils.makeOutAnimation(context, false));
        } else {
            view.setVisibility(View.VISIBLE);
            view.setAnimation(AnimationUtils.makeInAnimation(context, true));
        }
    }

}

