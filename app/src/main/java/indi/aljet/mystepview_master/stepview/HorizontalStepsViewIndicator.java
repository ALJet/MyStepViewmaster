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
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import indi.aljet.mystepview_master.R;
import indi.aljet.mystepview_master.stepview.bean.StepBean;

/**
 * StepsViewIndicator 指示器
 */
public class HorizontalStepsViewIndicator extends View {

    //定义默认高度
    private int defaultStepIndicatorNum = (int)
            TypedValue.applyDimension
                    (TypedValue.COMPLEX_UNIT_DIP,40,
                    getResources().getDisplayMetrics());


    private float mCompletedLineHeight;//完成线的高度

    private float mCircleRadius;//园的半径
    private Drawable mCompleteIcon;//完成的默认图标
    private Drawable mAttentionIcon;//注意的图标

    private Drawable mDefaultIcon;//默认图标
    private float mCenterY;//该view的Y轴中间位置
    private float mLeftY;//线左上方的Y位置
    private float mRightY;//线右下方的Y位置

    private List<StepBean> mStepBeanList;
    private int mStepNum = 0;//当前有几步流程
    private float mLinePadding;//两条连线之间的距离

    private List<Float> mCircleCenterPointPositionList;//定义所有圆的圆心点位置的集合
    private Paint mUnCompletedPaint;//未完成的Paint
    private Paint mCompletedPaint;//完成的Paint
    private int mUnCompletedLineColor =
            ContextCompat.getColor(getContext()
            , R.color.uncompleted_color);//定义默认未完成线的颜色
    private int mCompletedLineColor = Color.WHITE;
    private PathEffect mEffects;
    private int mComplectingPosition;//正在进行的position

    private Path mPath;
    private OnDrawIndicatorListener mOnDrawListener;
    private int scrrenWidth;//



    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener){
        mOnDrawListener = onDrawListener;
    }

    public float getCircleRadius(){
        return mCircleRadius;
    }





    public HorizontalStepsViewIndicator(Context context) {
        this(context,null);
    }

    public HorizontalStepsViewIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalStepsViewIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mStepBeanList = new ArrayList<>();
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]
                {8,8,8,8},1);

        mCircleCenterPointPositionList = new ArrayList<>();

        mUnCompletedPaint = new Paint();
        mCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);
        mUnCompletedPaint.setStrokeWidth(2);

        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);

        mUnCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //已经完成线的宽高
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum;

        //园的半径
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //线与线之间的间距
        mLinePadding = 0.85f * defaultStepIndicatorNum;

        //已经完成的icon
        mCompleteIcon = ContextCompat.getDrawable(getContext(),
                R.mipmap.complted);
        mAttentionIcon = ContextCompat.getDrawable(getContext(),
                R.mipmap.attention);
        mDefaultIcon = ContextCompat.getDrawable(getContext(),
                R.mipmap.default_icon);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = defaultStepIndicatorNum * 2;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)){
            scrrenWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = defaultStepIndicatorNum;
        if(MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED){
            height = Math.min(height,
                    MeasureSpec.getSize(heightMeasureSpec));
        }


        /**这个 什么意思 不懂啊 不应该是加法吗 怎么是减法了  **/
        width = (int) (mStepNum * mCircleRadius * 2 -
                (mStepNum - 1) * mLinePadding);
        setMeasuredDimension(width,height);

    }


    /**
     * 有点难理解  最后为了添加mCircleCenterPointPositionList
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /** 获取中间的高度， 目的是为了让该View绘制的线和园在该
         * View垂直居中
         * */
        mCenterY = 0.5f * getHeight();

        /** 获取左上方Y的位置，获取该点的意义是为了方便画
         * 矩形左上的Y位置
         */
        mLeftY = mCenterY - (mCompletedLineHeight / 2);


        /**
         * 获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
         */
        mRightY = mCenterY + mCompletedLineHeight / 2;

        mCircleCenterPointPositionList.clear();
        for(int i = 0 ;i < mStepNum;i++){
            /**
             * 先计算全部最左边的padding值(getWidth() -
             * (圆形直径 +两圆之间距离) *2)，
             */
            float paddingLeft = (scrrenWidth - mStepNum *
            mCircleRadius * 2 - (mStepNum - 1) *
            mLinePadding) / 2;

            /**
             * 还需要加个mCircleRadius 因为是中心点
             */
            float pointPosition = paddingLeft + mCircleRadius
                    + i * mCircleRadius * 2 + i
                    * mLinePadding;
            mCircleCenterPointPositionList.add
                    (pointPosition );

        }

        if(mOnDrawListener != null){
            mOnDrawListener.onDrawIndicator();
        }

    }


    /**
     * 重点 画图
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mOnDrawListener != null){
            mOnDrawListener.onDrawIndicator();
        }

        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);
        /**
         * 划线
         */

        for(int i = 0;i <
                mCircleCenterPointPositionList.
                        size() -1; i++){
            //当前一个ComplectedXPosition
            final float preComplectedXPosition=
                    mCircleCenterPointPositionList.get(i);
            //后一个ComplectedXPosition
            final float afterComplectedXPosition =
                    mCircleCenterPointPositionList.get(i + 1);
            if(i <= mComplectingPosition &&  mStepBeanList
                    .get(0).getState() != StepBean.STEP_UNDO){
                /**
                 * 画实线
                 * 这段代码不错
                 */
                //判断在完成之前的所有点，画完成的线，这里是矩形,很细的矩形，类似线，为了做区分，好看些
                canvas.drawRect(preComplectedXPosition +
                mCircleRadius - 10,mLeftY,
                        afterComplectedXPosition - mCircleRadius +
                10 , mRightY , mCompletedPaint);
            }else {
                /**
                 * 这个画虚线的
                 */
                mPath.moveTo(preComplectedXPosition +
                mCircleRadius,mCenterY);
                mPath.lineTo(afterComplectedXPosition -
                mCircleRadius,mCenterY);
                canvas.drawPath(mPath,
                        mUnCompletedPaint);
            }

        }

        /**
         * 画图标
         */
        for(int i = 0 ;i < mCircleCenterPointPositionList.size();i++){
            final float currentComplectedXPosition =
                    mCircleCenterPointPositionList.get(i);
            Rect rect = new Rect((int) (currentComplectedXPosition
            - mCircleRadius),(int) (mCenterY - mCircleRadius),
                    (int) (currentComplectedXPosition
            + mCircleRadius),(int) (mCenterY + mCircleRadius));
            StepBean stepBean = mStepBeanList.get(i);

            if(stepBean.getState() == StepBean.STEP_UNDO){
                mDefaultIcon.setBounds(rect);
                mDefaultIcon.draw(canvas);
            }else if(stepBean.getState() == StepBean.STEP_CURRENT){
                mCompletedPaint.setColor(Color.WHITE);
                canvas.drawCircle(currentComplectedXPosition,
                        mCenterY,mCircleRadius * 1.1f,
                        mCompletedPaint);
                mAttentionIcon.setBounds(rect);
                mAttentionIcon.draw(canvas);
            }else if(stepBean.getState() == StepBean.STEP_COMPLETED){
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
            }

        }
    }


    public List<Float> getCircleCenterPointPositionList(){
        return mCircleCenterPointPositionList;
    }

    public void setStepNum(List<StepBean> stepsBeanList){
        this.mStepBeanList = stepsBeanList;
        mStepNum = mStepBeanList.size();
        if(mStepBeanList != null && mStepBeanList.size() > 0){
            for(int i = 0 ;i < mStepNum;i++){
                StepBean stepBean = mStepBeanList.get(i);
                if(stepBean.getState() == StepBean.STEP_COMPLETED){
                    mComplectingPosition = i;
                }
            }
        }
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
     * 设置对View的监听
     */
    public interface OnDrawIndicatorListener{
        void onDrawIndicator();
    }
}
