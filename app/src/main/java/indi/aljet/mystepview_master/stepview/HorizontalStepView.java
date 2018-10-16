package indi.aljet.mystepview_master.stepview;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import indi.aljet.mystepview_master.R;
import indi.aljet.mystepview_master.stepview.bean.StepBean;

public class HorizontalStepView extends LinearLayout
        implements HorizontalStepsViewIndicator.OnDrawIndicatorListener {

    private RelativeLayout mTextContainer;
    private HorizontalStepsViewIndicator mStepsViewIndicator;
    private List<StepBean> mStepBeanList;
    private int mComplectingPosition;
    private int mUnComplectedTextColor = ContextCompat
            .getColor(getContext(),
                    R.color.uncompleted_text_color);
    private int mComplectedTextColor = ContextCompat
            .getColor(getContext(),
                    android.R.color.white);

    private int mTextSize = 14;
    private TextView mTextView;

    public HorizontalStepView(Context context) {
        this(context,null);
    }

    public HorizontalStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        View rootView = LayoutInflater.from(getContext()
        ).inflate(R.layout.widget_horizontal_stepview,this);
        mStepsViewIndicator = rootView.findViewById(R.id.
        steps_indicator);
        mStepsViewIndicator.setOnDrawListener(this);
        mTextContainer = rootView.findViewById(R.id.rl_text_container);

    }


    /**
     * 设置显示的文字
     *
     * @param stepsBeanList
     * @return
     */
    public HorizontalStepView setStepViewTexts
            (List<StepBean> stepsBeanList){
        this.mStepBeanList = stepsBeanList;
        mStepsViewIndicator.setStepNum(mStepBeanList);
        return this;
    }


    /**
     * 设置未完成文字的颜色
     *
     * @param unComplectedTextColor
     * @return
     */
    public HorizontalStepView setStepViewUnComplectedTextColor(int unComplectedTextColor)
    {
        mUnComplectedTextColor = unComplectedTextColor;
        return this;
    }

    /**
     * 设置完成文字的颜色
     *
     * @param complectedTextColor
     * @return
     */
    public HorizontalStepView setStepViewComplectedTextColor(int complectedTextColor)
    {
        this.mComplectedTextColor = complectedTextColor;
        return this;
    }



    /**
     * 设置StepsViewIndicator未完成线的颜色
     *
     * @param unCompletedLineColor
     * @return
     */
    public HorizontalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor)
    {
        mStepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
        return this;
    }



    /**
     * 设置StepsViewIndicator完成线的颜色
     *
     * @param completedLineColor
     * @return
     */
    public HorizontalStepView setStepsViewIndicatorCompletedLineColor(int completedLineColor)
    {
        mStepsViewIndicator.setCompletedLineColor(completedLineColor);
        return this;
    }

    /**
     * 设置StepsViewIndicator默认图片
     *
     * @param defaultIcon
     */
    public HorizontalStepView setStepsViewIndicatorDefaultIcon(Drawable defaultIcon)
    {
        mStepsViewIndicator.setDefaultIcon(defaultIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator已完成图片
     *
     * @param completeIcon
     */
    public HorizontalStepView setStepsViewIndicatorCompleteIcon(Drawable completeIcon)
    {
        mStepsViewIndicator.setCompleteIcon(completeIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     *
     * @param attentionIcon
     */
    public HorizontalStepView setStepsViewIndicatorAttentionIcon(Drawable attentionIcon)
    {
        mStepsViewIndicator.setAttentionIcon(attentionIcon);
        return this;
    }


    public HorizontalStepView setTextSize(int textSize) {
        if(textSize > 0)
        {
            mTextSize = textSize;
        }
        return this;
    }

    /**
     * 那个回调函数  需要把下面的文字 和上面的图形分开处理
     */
    @Override
    public void onDrawIndicator() {
        if(mTextContainer != null){
            mTextContainer.removeAllViews();
            List<Float> complectedXPosition =
                    mStepsViewIndicator.getCircleCenterPointPositionList();
            if(mStepBeanList != null &&
                    complectedXPosition != null &&
                    complectedXPosition.size() > 0){
                for(int i = 0;i < mStepBeanList.size();i++){
                    mTextView = new TextView(getContext());
                    mTextView.setTextSize(TypedValue
                    .COMPLEX_UNIT_SP,mTextSize);
                    mTextView.setText(mStepBeanList.get(i)
                    .getName());
                    /**
                     * 下面这些 有点看不懂  spec
                     */
                    int spec = View.MeasureSpec
                            .makeMeasureSpec(0,
                                    MeasureSpec
                            .UNSPECIFIED);
                    mTextView.measure(spec,spec);
                    int measuredWidth = mTextView
                            .getMeasuredWidth();
                    mTextView.setX(complectedXPosition.get(i)
                    - measuredWidth / 2);
                    mTextView.setLayoutParams(new ViewGroup.LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    if(i <= mComplectingPosition){
                        mTextView.setTypeface(null,
                                Typeface.BOLD);
                        mTextView.setTextColor(mComplectedTextColor);
                    }else{
                        mTextView.setTextColor
                                (mUnComplectedTextColor);
                    }
                    mTextContainer.addView(mTextView);
                }
            }
        }
    }
}
