package com.example.yuan.ringview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.RecursiveAction;

/**
 * Created by yuanjize on 17-1-13.
 */

public class DianDianView extends View {
    private int distance;
    private int firstColor;
    private int secondColor;
    private int diandianSize;
    private int width;
    private Bitmap bitmap;
    //点点的数量
    private int count;
    private Paint paint;
    private int selected=1;

    private Context context;
    public DianDianView(Context context) {
     this(context,null);
    }

    public DianDianView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DianDianView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        TypedArray array=context.getTheme().obtainStyledAttributes(attrs,R.styleable.DianDianView,defStyleAttr,0);
        int indexCount = array.getIndexCount();
        for (int i=0;i<indexCount;i++){
            int index=array.getIndex(i);
            switch(index){
                case R.styleable.DianDianView_diandian_size:
                    diandianSize= (int) array.getDimension(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DianDianView_distance:
                    distance= array.getInteger(index,15);
                    break;
                case R.styleable.DianDianView_firstcolor:
                    firstColor=array.getColor(index,Color.BLUE);
                    break;
                case R.styleable.DianDianView_secondcolor:
                    secondColor=array.getColor(index,Color.RED);
                    break;
                case R.styleable.DianDianView_count:
                    count=array.getInt(index,10);
                    break;
                case R.styleable.DianDianView_paint_width:
                    width= (int) array.getDimension(index, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,15,getResources().getDisplayMetrics()));
                    Log.e("YUANJIZE","size is :"+TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,15,getResources().getDisplayMetrics())+"..."+dp2px(16));
                    break;
                case R.styleable.DianDianView_bitmap:
                    int resourceId = array.getResourceId(index, 0);
                    bitmap= BitmapFactory.decodeResource(getResources(),resourceId);
            }
        }
        array.recycle();
        paint=new Paint();
    }

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
        Log.e("YUANJIZE","widthMode"+widthMode);
        Log.e("YUANJIZE","heightMode"+heightMode);

//        if (desireHeight!=desireWith){
//            desireHeight=Math.min(desireHeight,desireWith);
//            desireWith=Math.min(desireHeight,desireWith);
//        }
        setMeasuredDimension(desireWith,desireHeight);
    }

    private Rect rect=new Rect();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("YUANJIZE","getWidth"+getWidth());
        Log.e("YUANJIZE","getHeight"+getHeight());
        Log.e("YUANJIZE","getMeasuredWidth"+getMeasuredWidth());
        Log.e("YUANJIZE","getMeasuredHeight"+getMeasuredHeight());
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setStrokeCap(Paint.Cap.ROUND);
        drawDiandian(canvas);

        //计算内切正放形，把图片放进去
        int center=getWidth()/2;
        int r=center-width;
        rect.left= (int) (center-(Math.sqrt(2)/2)*r);
        rect.top=rect.left;
        rect.right= (int) (center+(Math.sqrt(2)/2*r));
        rect.bottom=rect.right;
        //if(bitmap!=null){
            canvas.drawBitmap(bitmap,null,rect,paint);
        //}
    }

    private void drawDiandian(Canvas canvas) {
        int size=(360-count*distance)/count; //每个item角度
        RectF rectF=new RectF(width,width,getWidth()-width,getHeight()-width);
        paint.setColor(firstColor);
        for (int i=0;i<selected;i++){
            canvas.drawArc(rectF,i*(distance+size),size,false,paint);
        }
        paint.setColor(secondColor);
        for (int i=selected;i<count;i++){
            canvas.drawArc(rectF,i*(distance+size),size,false,paint);
        }
    }
    private int dp2px(int dp){
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(selected==count){
                    Toast.makeText(context, "已经是最大音量", Toast.LENGTH_SHORT).show();
                    return true;
                }
                selected++;
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if(selected==0){
                    Toast.makeText(context, "已经是小音量" , Toast.LENGTH_SHORT).show();
                    return true;
                }
                selected--;
                invalidate();
                return true;
            default:
                return true;
        }

    }
}