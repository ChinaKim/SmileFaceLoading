package com.kim.smilefooter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by kim on 16-11-30.
 */

public class SmileView extends View implements SmileListener {
    private final int STATE_STOP = 0;
    private final int STATE_START_LOAD = 1;
    private final int STATE_LOADING = 2;
    private final int STATE_END_LOADING = 3;
    private final int STATE_STOP_ROATE = 4;
    private final int STATE = -1;

    private int mCurrentState = STATE_START_LOAD;

    //    private Path path;
    private Paint paint;
    private float mRaidus = dp2px(30);

    private ValueAnimator mValueAnimatorStartLoad;
    private ValueAnimator mValueAnimatorLoading;
    private ValueAnimator mValueAnimatorEndLoading;
    private ValueAnimator mValueAnimatorStopLoading;

    private ValueAnimator.AnimatorListener mAnimatorListener;

    private float mProgressLoad;
    private float mLoadingProgress;
    private float mEndLoadingProgress;
    private float mStopLoadingProgress;
    private int strokeWidth = dp2px(7);

    private int mViewWidth, mViewHeight;

    //动画是否结束
    private boolean isStop = false;

    public SmileView(Context context) {
        super(context);
        init();
    }

    public SmileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STATE_STOP:
                    if (!isStop) {
                        mValueAnimatorStartLoad.start();
                        mCurrentState = STATE_START_LOAD;
                    }
                    break;
                case STATE_START_LOAD:
                    mValueAnimatorLoading.start();
                    mCurrentState = STATE_LOADING;
                    break;
                case STATE_LOADING:
                    mValueAnimatorEndLoading.start();
                    mCurrentState = STATE_END_LOADING;
                    break;
                case STATE_END_LOADING:
                    mValueAnimatorStopLoading.start();
                    mCurrentState = STATE_STOP_ROATE;
                    break;
                case STATE_STOP_ROATE:
                    mHandler.sendEmptyMessage(STATE_STOP);
                    break;
                case STATE:
                    break;
            }
        }
    };

    @Override
    public void start() {
        isStop = false;
        mCurrentState = STATE_STOP;
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void stop() {
        isStop = true;
        mHandler.removeCallbacksAndMessages(null);
    }

    void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(getResources().getColor(R.color.smile_green));

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(mCurrentState);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };


        mValueAnimatorStartLoad = ValueAnimator.ofFloat(0f, 1f).setDuration(800);
        mValueAnimatorStartLoad.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgressLoad = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimatorStartLoad.addListener(mAnimatorListener);

        mValueAnimatorLoading = ValueAnimator.ofFloat(0f, 1f).setDuration(700);
        mValueAnimatorLoading.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLoadingProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimatorLoading.addListener(mAnimatorListener);

        mValueAnimatorEndLoading = ValueAnimator.ofFloat(0f, 1f).setDuration(600);
        mValueAnimatorEndLoading.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mEndLoadingProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimatorEndLoading.addListener(mAnimatorListener);


        mValueAnimatorStopLoading = ValueAnimator.ofFloat(0f, 1f).setDuration(800);
        mValueAnimatorStopLoading.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStopLoadingProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimatorStopLoading.addListener(mAnimatorListener);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int contentWidthSize = (int) (mRaidus * 2 + strokeWidth * 2 + this.getPaddingLeft() + this.getPaddingRight());
        int contentHeightSize = (int) (mRaidus * 2 + strokeWidth * 2 + this.getPaddingTop() + this.getPaddingBottom());

        int width = resolveSize(contentWidthSize, widthMeasureSpec);
        int height = resolveSize(contentHeightSize, heightMeasureSpec);

        width = contentWidthSize > width ? contentWidthSize : width;
        height = contentHeightSize > height ? contentHeightSize : height;

        setMeasuredDimension(width, height);
        mViewWidth = width;
        mViewHeight = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mViewWidth / 2, mViewHeight / 2);

        RectF rectf_circle = new RectF(-mRaidus, -mRaidus, mRaidus, mRaidus);
        Path path = new Path();

        //eyes
        float eyeXDistance = mRaidus * 2 / 3;
        float eyeYDistance = (float) Math.sqrt(mRaidus * mRaidus - eyeXDistance * eyeXDistance);

        switch (mCurrentState) {
            case STATE_START_LOAD:

                path.addArc(rectf_circle, 0f, 360f);
                PathMeasure pathMeasure = new PathMeasure();
                pathMeasure.setPath(path, true);
                Path dst = new Path();
                float length = pathMeasure.getLength();
                float startD = pathMeasure.getLength() / 4 * mProgressLoad;
                float loadingDisance = pathMeasure.getLength() / 2 + pathMeasure.getLength() / 4 * mProgressLoad;
                pathMeasure.getSegment(startD, loadingDisance + startD, dst, true);

                canvas.drawPath(dst, paint);

                //眼睛应该同时顺时针旋转一部分
                float eyeXDisance_left = eyeXDistance - mProgressLoad * mRaidus / 3;
                float eyeYDisance_left = (float) Math.sqrt(mRaidus * mRaidus - eyeXDisance_left * eyeXDisance_left);
                float eyeXDisance_right = eyeXDistance + mProgressLoad * mRaidus / 3;
                float eyeYDisance_right = (float) Math.sqrt(mRaidus * mRaidus - eyeXDisance_right * eyeXDisance_right);
                canvas.drawPoint(-eyeXDisance_left, -eyeYDisance_left, paint);//左边
                canvas.drawPoint(eyeXDisance_right, -eyeYDisance_right, paint);//右边
                break;

            case STATE_LOADING:
                float startAngle = 90f + mLoadingProgress * 360;
                path.addArc(rectf_circle, startAngle, 360.f * 3 / 4);
                canvas.drawPath(path, paint);
                break;

            case STATE_END_LOADING:
                float startDEndLoading = 90f + 180f * mEndLoadingProgress;
                float endDEndLoading = 180f + 90f * (1 - mEndLoadingProgress);

                path.addArc(rectf_circle, startDEndLoading, endDEndLoading);
                canvas.drawPath(path, paint);

                //眼睛应该同时顺时针旋转一部分
                float eyeXDisance_end_left_default = mRaidus;
                float eyeXDisance_end_left = eyeXDisance_end_left_default - mEndLoadingProgress * mRaidus * 1 / 3;
                float eyeYDisance_end_left = (float) Math.sqrt(mRaidus * mRaidus - eyeXDisance_end_left * eyeXDisance_end_left);
                canvas.drawPoint(-eyeXDisance_end_left, -eyeYDisance_end_left, paint);//左边

                float eyeXDisance_end_right_default = 0;
                float eyeXDisance_end_right = eyeXDisance_end_right_default + mEndLoadingProgress * mRaidus * 2 / 3;
                float eyeYDisance_end_right = (float) Math.sqrt(mRaidus * mRaidus - eyeXDisance_end_right * eyeXDisance_end_right);
                canvas.drawPoint(-eyeXDisance_end_right, eyeYDisance_end_right, paint);

                break;

            case STATE_STOP_ROATE:

                float startStop = -90f + 90f * mStopLoadingProgress;
                float endDStop = 180f;
                path.addArc(rectf_circle, startStop, endDStop);
                canvas.drawPath(path, paint);

                float eyeY_stop_left = eyeYDistance * (1 - 2 * mStopLoadingProgress);
                float eyeX_stop_left = (float) Math.sqrt(mRaidus * mRaidus - eyeY_stop_left * eyeY_stop_left);
                canvas.drawPoint(-eyeX_stop_left, eyeY_stop_left, paint);

                float eyeX_stop_right = eyeXDistance * (-1 + 2 * mStopLoadingProgress);
                float eyeY_stop_right = (float) Math.sqrt(mRaidus * mRaidus - eyeX_stop_right * eyeX_stop_right);
                canvas.drawPoint(eyeX_stop_right, -eyeY_stop_right, paint);
                break;
        }

    }


    private int dp2px(int dpValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics()));
    }


}
