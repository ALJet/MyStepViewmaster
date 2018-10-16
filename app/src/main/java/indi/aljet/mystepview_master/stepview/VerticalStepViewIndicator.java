package indi.aljet.mystepview_master.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import indi.aljet.mystepview_master.R;

public class VerticalStepViewIndicator extends View {

    private final String TAG_NAME = this.getClass().getName();

    //定义默认高度
    private int defaultStepIndicatorNum = (int) TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    40,getResources()
            .getDisplayMetrics());

    //完成线的高度
    private float mCompletedLineHeight;
    private float mCircleRadius;//圆的半径
    private Drawable mCompleteIcon;//完成的图标
    private Drawable mAttentionIcon;//正在进行的图标
    private Drawable mDefaultIcon;//默认图标

    private float mCenterX;//该View的X轴中间位置
    private float mLeftY; //线的左上Y位置
    private float mRightY;//线的右下的Y位置

    private int mStepNum = 0;//当前有几步流程
    private float mLinePadding;//两线之间的间距

    //定义所有圆的圆心点位置的集合
    private List<Float> mCircleCenterPointPositionList;

    private Paint mUnCompletedPaint;//未完成Paint
    private Paint mCompletedPaint;//完成的Paint
    private int mUnCompletedLineColor = ContextCompat
            .getColor(getContext(),
                    R.color.uncompleted_color);
    private int mCompletedLineColor = Color.WHITE;
    private PathEffect mEffects;
    private int mComplectingPosition;//正在进行的Position
    private Path mPath;

    private OnDrawIndicatorListener mOnDrawListener;
    private Rect mRect;
    private  int mHeight;//这个控件的动态高度
    private boolean mIsReverseDraw;


    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener)
    {
        mOnDrawListener = onDrawListener;
    }

    /**
     * get圆的半径  get circle radius
     *
     * @return
     */
    public float getCircleRadius()
    {
        return mCircleRadius;
    }


    public VerticalStepViewIndicator(Context context) {
        this(context,null);
    }

    public VerticalStepViewIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerticalStepViewIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]
                {8,8,8,8},1);
        mCircleCenterPointPositionList = new ArrayList<>();

        mUnCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);
        mUnCompletedPaint.setStrokeWidth(2);

        mCompletedPaint = new Paint();
        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);

        mUnCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //已经完成线的宽高
        mCompletedLineHeight = 0.05f *
                defaultStepIndicatorNum;
        //圆的半径
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //线之间的间距
        mLinePadding = 0.85f * defaultStepIndicatorNum;

        mCompleteIcon = ContextCompat.getDrawable(getContext(),
                R.drawable.complted);
        mAttentionIcon = ContextCompat.getDrawable(getContext(),
                R.drawable.attention);
        mDefaultIcon = ContextCompat.getDrawable(getContext(),
                R.drawable.default_icon);

        mIsReverseDraw = true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG_NAME,"onMeasure");
        int width = defaultStepIndicatorNum;
        mHeight = 0;
        if(mStepNum > 0){
            mHeight = (int) (getPaddingTop() +
            getPaddingBottom() + mCircleRadius * 2
            * mStepNum + (mStepNum - 1) * mLinePadding);
        }
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)){
            width = Math.min(width,MeasureSpec.getSize
                    (widthMeasureSpec));
        }
        setMeasuredDimension(width,mHeight);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG_NAME,"onSizeChanged");
        mCenterX = getWidth() / 2;
        mLeftY = mCenterX - (mCompletedLineHeight / 2);
        mRightY = mCenterX + (mCompletedLineHeight / 2);

        /** 加的   的   */
//        mCircleCenterPointPositionList.clear();

        for(int i = 0; i < mStepNum; i++)
        {
            //reverse draw VerticalStepViewIndicator
            if(mIsReverseDraw)
            {
                mCircleCenterPointPositionList.add(mHeight - (mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding));
            } else
            {
                mCircleCenterPointPositionList.add(mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
            }
        }

            if(mOnDrawListener != null){
                mOnDrawListener.onDrawIndicator();
            }

    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mOnDrawListener != null){
            mOnDrawListener.onDrawIndicator();
        }
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);

        for(int i = 0;i < mCircleCenterPointPositionList.size() - 1;i++){
            final float preComplectedXPosition =
                    mCircleCenterPointPositionList.get(i);
            final float afterComplectedXPosition =
                    mCircleCenterPointPositionList.get(i + 1);
            if( i < mComplectingPosition ){
                //判断在完成之前的所有点


                //判断在完成之前的所有点，画完成的线，这里是矩形,很细的矩形，类似线，为了做区分，好看些
                if(mIsReverseDraw){
                    //从下到上
                    canvas.drawRect(mLeftY,
                            afterComplectedXPosition +
                    mCircleRadius - 10,
                            mRightY,
                            preComplectedXPosition -
                    mCircleRadius + 10,mCompletedPaint);
                }else{
                    //从上到下
                    canvas.drawRect(mLeftY,
                            preComplectedXPosition + mCircleRadius - 10,
                            mRightY, afterComplectedXPosition - mCircleRadius + 10,
                            mCompletedPaint);
                }
            }else{
                if(mIsReverseDraw){
                    //从下到上
                    mPath.moveTo(mCenterX,
                            afterComplectedXPosition + mCircleRadius);
                    mPath.lineTo(mCenterX,
                            preComplectedXPosition - mCircleRadius);
                    canvas.drawPath(mPath,
                            mUnCompletedPaint);
                }else{
                    //从上到下
                    mPath.moveTo(mCenterX,
                            preComplectedXPosition + mCircleRadius);
                    mPath.lineTo(mCenterX,
                            afterComplectedXPosition - mCircleRadius);
                    canvas.drawPath(mPath,mUnCompletedPaint);
                }
            }
        }

        for(int i = 0;i < mCircleCenterPointPositionList.size();i++){
            final float currentComplectedXPosition =
                    mCircleCenterPointPositionList.get(i);
            mRect = new Rect((int) (mCenterX - mCircleRadius),
                    (int)(currentComplectedXPosition - mCircleRadius)
            ,(int) (mCenterX + mCircleRadius),(int)
                    (currentComplectedXPosition + mCircleRadius));
            if(i < mComplectingPosition){
                mCompleteIcon.setBounds(mRect);
                mCompleteIcon.draw(canvas);
            }else if( i == mComplectingPosition &&
                    mCircleCenterPointPositionList.size() != 1){
                mCompletedPaint.setColor(Color.WHITE);
                canvas.drawCircle(mCenterX,
                        currentComplectedXPosition ,
                        mCircleRadius * 1.1f,
                        mCompletedPaint);
                mAttentionIcon.setBounds(mRect);
                mAttentionIcon.draw(canvas);
            }else{
                mDefaultIcon.setBounds(mRect);
                mDefaultIcon.draw(canvas);
            }
        }
    }


    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    public List<Float> getCircleCenterPointPositionList()
    {
        return mCircleCenterPointPositionList;
    }

    /**
     * 设置流程步数
     *
     * @param stepNum 流程步数
     */
    public void setStepNum(int stepNum)
    {
        Log.d("TAG StepNum",stepNum + " ");
        this.mStepNum = stepNum;
        requestLayout();
    }

    /**
     *
     * 设置线间距的比例系数 set linePadding proportion
     * @param linePaddingProportion
     */
    public void setIndicatorLinePaddingProportion(float linePaddingProportion)
    {
        this.mLinePadding = linePaddingProportion * defaultStepIndicatorNum;
    }

    /**
     * 设置正在进行position
     *
     * @param complectingPosition
     */
    public void setComplectingPosition(int complectingPosition)
    {
        this.mComplectingPosition = complectingPosition;
        requestLayout();
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor)
    {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor)
    {
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * is reverse draw 是否倒序画
     */
    public void reverseDraw(boolean isReverseDraw)
    {
        this.mIsReverseDraw = isReverseDraw;
        invalidate();
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon)
    {
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon)
    {
        this.mCompleteIcon = completeIcon;
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon)
    {
        this.mAttentionIcon = attentionIcon;
    }

    /**
     * 设置对view监听
     */
    public interface OnDrawIndicatorListener{
        void onDrawIndicator();
    }
}
