package com.example.yuan.ringview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
/**
 * Created by yuanjize on 17-1-13.
 */

public class RingView extends View{
    private int speed;
    private int width;
    private int firstColor=Color.RED;
    private int secondColor=Color.BLUE;
    private Paint paint;

    public RingView(Context context){
        this(context, null);
    }

    public RingView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.getTheme().obtainStyledAttributes(attrs,R.styleable.RingView,defStyleAttr,0);
        int indexCount = array.getIndexCount();
        for (int i=0;i<indexCount;i++){
            int index=array.getIndex(i);
            switch(index){
                case R.styleable.RingView_ringWidth:
                    width= (int) array.getDimension(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RingView_firstColor:
                    firstColor=array.getColor(index, Color.RED);
                    break;
                case R.styleable.RingView_secondColor:
                    secondColor=array.getColor(index,Color.BLUE);
                    break;
                case R.styleable.RingView_speed:
                    speed=array.getInt(index,20);
                    break;
            }
        }
        array.recycle();
        paint=new Paint();
    }

    //默认100dp大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int desireWith;
        int desireHeight;
        if(widthMode!=MeasureSpec.EXACTLY){
            desireWith=dp2px(100);
        }else{
            desireWith=widthSize;
        }
        if(heightMode!=MeasureSpec.EXACTLY){
            desireHeight=dp2px(100);
        }else{
            desireHeight=heightSize;
        }
        setMeasuredDimension(desireWith,desireHeight);
    }

    int sweep;
    RectF oval;
    boolean first=false;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);
        int center=Math.min(getWidth()/2,getHeight()/2);
        int r=center-width;
        if(oval==null){
            oval=new RectF(getWidth()/2 - r, getHeight()/2 - r, getWidth()/2 + r, getHeight()/2 + r);
        }
        if(first){
            paint.setColor(firstColor);
            canvas.drawCircle(getWidth()/2,getHeight()/2,r,paint);
            paint.setColor(secondColor);
            canvas.drawArc(oval,-90,sweep,false,paint);
        }else{
            paint.setColor(secondColor);
            canvas.drawCircle(getWidth()/2,getHeight()/2,r,paint);
            paint.setColor(firstColor);
            canvas.drawArc(oval,-90,sweep,false,paint);
        }

    }
    ValueAnimator valueAnimator;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator!=null&&valueAnimator.isRunning()){
               valueAnimator.cancel();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility==GONE){
            if (valueAnimator!=null&&valueAnimator.isRunning()){
                valueAnimator.pause();
            }
        }else if(visibility==VISIBLE){
            if(valueAnimator!=null&&valueAnimator.isPaused()){
                valueAnimator.resume();
            }
        }
    }

    //停止旋转
    public void finish(){
        setVisibility(INVISIBLE);
        if(valueAnimator!=null){
            if(valueAnimator.isRunning()||valueAnimator.isStarted()||valueAnimator.isPaused()){
                valueAnimator.cancel();
            }
        }
    }
    //开始旋转
    public void start(){
        setVisibility(VISIBLE);
        if(valueAnimator==null){
            valueAnimator=ValueAnimator.ofInt(0,360);
            valueAnimator.setDuration((360/speed)*100);
            valueAnimator.setRepeatCount(1000);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    sweep= (int) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });

            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    first=!first;
                }
            });
            valueAnimator.start();
        }
    }
    private int dp2px(int dp){
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
