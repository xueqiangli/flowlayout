package com.example.flowlayoutdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author storm 93084
 * @description: 不可滑动的流式布局
 * @date :2021/5/26 10:40
 */
public class FlowLayout extends ViewGroup {
    //记录每个View的位置
    private List<ChildPos> mChildPos = new ArrayList<ChildPos>();
    private int flowColumns=2;//默认单列和两列，默认为两列
    private Scroller scroller;
    private int mLastX=0;
    private int mLastY=0;
    private int height=0;//流布局的最终高度
    /**
     * 屏幕的高度
     */
    private int mScreenHeight;


    private class ChildPos {
        int left, top, right, bottom;

        public ChildPos(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 最终调用这个构造方法
     *
     * @param context  上下文
     * @param attrs    xml属性集合
     * @param defStyle Theme中定义的style
     */
    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.FlowLayout);
        flowColumns=a.getInt(R.styleable.FlowLayout_flow_columns,2);
        a.recycle();


        scroller=new Scroller(context);

        /**
         * 获得屏幕的高度
         */
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
    }

    //缓慢滑动到指定位置
    public void smoothScrollTo(int destX,int destY){
        int scrollx=getScrollX();
        int scrolly=getScrollY();
        int deltax=destX-scrollx;
        int deltaY=destY-scrolly;
        scroller.startScroll(0,scrolly,0,destY,1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x= (int) event.getX();
        int y= (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY=mLastY-y;
                // 边界值检查
                int scrollY = getScrollY();
                // 已经到达顶端，下拉多少，就往上滚动多少
                if (deltaY<0 && scrollY+deltaY<0){
                    deltaY=-scrollY;
                }
                //已经到底部，上拉多少，就往下滚动多少

                if (deltaY>0&&scrollY+deltaY>height-mScreenHeight){
                    deltaY=height-mScreenHeight+200-scrollY;
                }
                scrollBy(0,deltaY);
                mLastY=y;
                break;
            case MotionEvent.ACTION_UP:
                //smoothScrollTo(x,y-mLastY);
                mLastX=x;
                //mLastY=y;
                break;
            default:
                break;
        }
        return true;
    }

    public boolean isSlideToBottom() {
        if (computeVerticalScrollExtent() + computeVerticalScrollOffset()
                >= computeVerticalScrollRange())
            return true;
        return false;
    }




    /**
     * 测量宽度和高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取流式布局的宽度和模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取流式布局的高度和模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //使用wrap_content的流式布局的最终宽度和高度
        int width = 0;
        height = 0;
        //记录每一行的宽度和高度
        int lineWidth = 0, lineHeight = 0;
        //得到内部元素的个数
        int count = getChildCount();
        mChildPos.clear();

        //一行占view的个数
        int childNum = 0;
        for (int i = 0; i < count; i++) {
            //获取对应索引的view
            View child = getChildAt(i);

            //测量子view的宽和高
            measureChildWithMargins(child, widthMeasureSpec,0, heightMeasureSpec,0);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //子view占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            //子view占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            TextView textView=(TextView) child;
            Log.e("BBB","值是"+textView.getText().toString());

            int maxWidth=widthSize - getPaddingLeft() - getPaddingRight();
            //换行
            if (i==0||lineWidth>maxWidth/flowColumns||childWidth>maxWidth/flowColumns|| childNum == flowColumns-1) {
                childNum = 0;
                //取最大的行宽为流式布局宽度
                width = Math.max(width, lineWidth);
                //叠加行高得到流式布局高度
                height += lineHeight;
                //重置行宽度为第一个View的宽度
                lineWidth = childWidth;
                //重置行高度为第一个View的高度
                lineHeight = childHeight;
                //记录位置
                mChildPos.add(new ChildPos(
                        getPaddingLeft() + lp.leftMargin,
                        getPaddingTop() + height + lp.topMargin,
                        getPaddingLeft() + childWidth - lp.rightMargin,
                        getPaddingTop() + height + childHeight - lp.bottomMargin));
            } else {  //不换行
                childNum = childNum + 1;


                int left =(widthSize - getPaddingLeft() - getPaddingRight())/flowColumns;

                //记录位置
                mChildPos.add(new ChildPos(
                        getPaddingLeft() + lp.leftMargin+left,
                        getPaddingTop() + height + lp.topMargin,
                        getPaddingLeft() + childWidth - lp.rightMargin+left,
                        getPaddingTop() + height + childHeight - lp.bottomMargin));

                //叠加子View宽度得到新行宽度
                lineWidth = lineWidth + childWidth;
                //取当前行子View最大高度作为行高度
                lineHeight = Math.max(lineHeight, childHeight);



            }
            //最后一个控件
            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        Log.e("BBB","高度"+height);



        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? heightSize : height + getPaddingTop() + getPaddingBottom());
    }



    /**
     * 让ViewGroup能够支持margin属性
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    /**
     * 设置每个View的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ChildPos pos = mChildPos.get(i);
            //设置View的左边、上边、右边底边位置
            child.layout(pos.left, pos.top, pos.right, pos.bottom);

        }
    }

}
