package com.android.ivorita.granary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.ivorita.granary.R;
import com.android.ivorita.granary.util.DisplayHelper;

import static android.content.ContentValues.TAG;
import static com.android.ivorita.granary.util.DisplayHelper.dpToPx;
import static com.android.ivorita.granary.util.DisplayHelper.sp2px;


/**
 * 计量仪 View
 */
public class MeterView extends View {

    //参数标题部分属性
    float param_textSize;
    int param_textColor;
    String param_name;

    //数值文本部分属性
    float value_textSize;
    int value_textColor;
    String value_text;
    String value_unit;

    //圆弧部分属性
    float arc_width;
    int totalArc_color;
    int valueArc_color;

    //数值范围文本部分属性
    int valueFlag_textColor;
    float valueFlag_textSize;

    //控件宽
    private int width;
    //控件高
    private int height;
    //圆弧半径
    private int arcRadius;
    //总圆弧画笔
    private Paint arcPaint;
    //数值圆弧画笔
    private Paint valueArcPaint;
    //内部标题画笔
    private Paint titlePaint;
    //内部数值画笔
    private Paint valuePaint;
    //温度标识画笔
    private Paint valueFlagPaint;
    //刻度高
    private int scaleHeight = dpToPx(10);
    //刻度盘画笔
    private Paint meterPaint;
    //标题
    private String title = "温度";
    //温度
    private int temperature = 16;
    //最高数值
    private int maxValue = 30;
    //最低数值
    private int minValue = 15;
    //4格代表数值1度
    private int angleRate = 4;
    //每格的角度
    private float anglePer = (float) 270 / (maxValue - minValue) / angleRate;
    //温度改变监听接口
    private OnValueChangeListener onValueChangeListener;

    //按钮旋转的角度
    private float rotateAngle;
    //当前角度
    private float currentAngle;

    public MeterView(Context context) {
        super(context);
    }

    public MeterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeterView);
        try {

            value_textSize = typedArray.getDimension(R.styleable.MeterView_value_textSize, sp2px(getContext(), 23));
            value_textColor = typedArray.getColor(R.styleable.MeterView_value_textColor, Color.parseColor("#E27A3F"));
            value_text = typedArray.getString(R.styleable.MeterView_value_text);
            value_unit = typedArray.getString(R.styleable.MeterView_value_unit);

            param_textSize = typedArray.getDimension(R.styleable.MeterView_param_textSize, sp2px(getContext(), 15));
            param_textColor = typedArray.getColor(R.styleable.MeterView_param_textColor, Color.parseColor("#3B434E"));
            param_name = typedArray.getString(R.styleable.MeterView_param_name);

            Log.d(TAG, "value_text: " + value_text);

            valueFlag_textColor = typedArray.getColor(R.styleable.MeterView_valueFlag_textColor, Color.parseColor("#E4A07E"));
            valueFlag_textSize = typedArray.getDimension(R.styleable.MeterView_valueFlag_textSize, sp2px(getContext(), 18));

            arc_width = typedArray.getDimension(R.styleable.MeterView_arc_width, dpToPx(5));
            totalArc_color = typedArray.getColor(R.styleable.MeterView_totalArc_color, Color.parseColor("#E0E0E0"));
            valueArc_color = typedArray.getColor(R.styleable.MeterView_valueArc_color, Color.parseColor("#2196F3"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    public MeterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MeterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化画笔
     */
    private void init() {

        //总圆弧画笔初始化
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(totalArc_color);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setStrokeWidth(arc_width);
        arcPaint.setStyle(Paint.Style.STROKE);

        //数值圆弧画笔初始化
        valueArcPaint = new Paint();
        valueArcPaint.setAntiAlias(true);
        if (Integer.parseInt(value_text) > maxValue) {
            valueArcPaint.setColor(Color.parseColor("#FF4040"));
        } else {
            valueArcPaint.setColor(valueArc_color);
        }
        //valueArcPaint.setColor(valueArc_color);
        valueArcPaint.setStrokeCap(Paint.Cap.ROUND);
        valueArcPaint.setStrokeWidth(arc_width);
        valueArcPaint.setStyle(Paint.Style.STROKE);

        //刻度盘画笔初始化
        meterPaint = new Paint();
        meterPaint.setAntiAlias(true);
        meterPaint.setStrokeWidth(dpToPx(2));
        meterPaint.setStyle(Paint.Style.STROKE);

        //参数名画笔初始化
        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(param_textSize);
        titlePaint.setColor(param_textColor);
        titlePaint.setStyle(Paint.Style.FILL);

        //数值画笔初始化
        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setTextSize(value_textSize);
        if (Integer.parseInt(value_text) > maxValue) {
            valuePaint.setColor(Color.parseColor("#FF4040"));
        } else {
            valuePaint.setColor(Color.parseColor("#E27A3F"));
        }
        valuePaint.setStyle(Paint.Style.FILL);

        //范围文本画笔初始化
        valueFlagPaint = new Paint();
        valueFlagPaint.setAntiAlias(true);
        valueFlagPaint.setTextSize(valueFlag_textSize);
        valueFlagPaint.setColor(valueFlag_textColor);
        valueFlagPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int myWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int myWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int myHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // 获取宽
        if (myWidthSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            width = myWidthSpecSize;
        } else {
            // wrap_content
            width = DisplayHelper.dpToPx(150);
        }

        // 获取高
        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            height = myHeightSpecSize;
        } else {
            // if wrap_content, default height is 150dp.
            height = DisplayHelper.dpToPx(150);
        }

        arcRadius = width / 2 - dpToPx(20);

        // 设置该view的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        init();
        //drawScale(canvas);
        drawArc(canvas);
        drawValueArc(canvas);
        drawTitle(canvas);
        drawValue(canvas);

        Log.d(TAG, "onDraw: execute");

    }


    /*private void drawScale(Canvas canvas) {
        canvas.save();

        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(-133);
        meterPaint.setColor(Color.parseColor("#3CB7EA"));
        for (int i = 0; i < 60; i++) {
            canvas.drawLine(0, -arcRadius - dpToPx(20), 0, -arcRadius - dpToPx(20) + scaleHeight, meterPaint);
            canvas.rotate(4.5f);
        }

        canvas.restore();
    }*/

    /**
     * 绘制总圆弧
     */
    private void drawArc(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(135);
        RectF rectF = new RectF(-arcRadius, -arcRadius, arcRadius, arcRadius);
        canvas.drawArc(rectF, 0, 270, false, arcPaint);
        canvas.restore();
    }

    /**
     * 绘制数值圆弧
     */
    private void drawValueArc(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(135);
        RectF rectF = new RectF(-arcRadius, -arcRadius, arcRadius, arcRadius);
        //canvas.drawArc(rectF, 0, (Integer.parseInt(value_text) - minValue) * angleRate * anglePer, false, valueArcPaint);
        canvas.drawArc(rectF, 0, rotateAngle, false, valueArcPaint);
        canvas.restore();
    }

    /**
     * 绘制标题与数值标识
     */
    private void drawTitle(Canvas canvas) {
        canvas.save();

        //绘制标题
        float titleWidth = titlePaint.measureText(param_name);
        canvas.drawText(param_name, (width - titleWidth) / 2, arcRadius * 2 + dpToPx(10), titlePaint);

        float minValueFlagWidth = valueFlagPaint.measureText(minValue + "");
        float maxValueFlagWidth = valueFlagPaint.measureText(maxValue + "");

        /*float degrees :旋转角度
          float px:旋转中心横坐标
          float py:旋转中心纵坐标*/
        canvas.rotate(55, width / 2, height / 2); //顺时针旋转55度
        canvas.drawText(minValue + "", (width - minValueFlagWidth) / 2, height, valueFlagPaint);

        canvas.rotate(-110, width / 2, height / 2); //逆时针旋转105度
        canvas.drawText(maxValue + "", (width - maxValueFlagWidth) / 2, height, valueFlagPaint);

        canvas.restore();
    }


    /**
     * 绘制中心数值
     */
    private void drawValue(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);

        //float valueWidth = valuePaint.measureText(temperature + "℃");
        float valueWidth = valuePaint.measureText(value_text + value_unit);

        //1.基准点是baseline
        //2.ascent：是baseline之上至字符最高处的距离
        // 3.descent：是baseline之下至字符最低处的距离*/
        float valueHeight = (valuePaint.ascent() + valuePaint.descent()) / 2;
        //canvas.drawText(temperature + "℃", -valueWidth / 2, -valueHeight, valuePaint);
        Log.d(TAG, "drawValue: " + value_text);
        canvas.drawText(value_text + value_unit, -valueWidth / 2, -valueHeight, valuePaint);

        canvas.restore();
    }

    /**
     * 数值改变监听接口
     */
    public interface OnValueChangeListener {
        /**
         * @param value 温度
         */
        void change(int value);
    }

    /**
     * 设置数值改变监听接口
     *
     * @param onValueChangeListener 数值改变监听接口
     */
    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    /**
     * 设置数值
     *
     * @param temp 设置的数值
     */
    public void setValue(int temp) {
        setValue(minValue, maxValue, temp);
    }


    /**
     * 设置数值
     *
     * @param minValue 最低数值
     * @param maxValue 最高数值
     * @param value    设置的数值
     */
    public void setValue(int minValue, int maxValue, int value) {
        this.minValue = minValue;
        this.maxValue = maxValue;

        /*if (temp < minTemp) {
            this.value_text = String.valueOf(minTemp);
        } else if (temp > maxTemp) {
            this.value_text = String.valueOf(maxTemp);
        } else {
            this.value_text = String.valueOf(temp);
        }*/

        this.value_text = String.valueOf(value);

        //每格的角度
        anglePer = (float) 270 / (maxValue - minValue) / angleRate;
        //计算旋转角度
        if (value < minValue) {
            rotateAngle = (0) * angleRate * anglePer;
        } else if (value > maxValue) {
            rotateAngle = (maxValue - minValue) * angleRate * anglePer;

            Log.d("c_view", "else if rotateAngle: " + rotateAngle);
        } else {
            rotateAngle = (value - minValue) * angleRate * anglePer;
        }

        Log.d("c_view", "temp: " + value);
        Log.d("c_view", "minValue: " + minValue);
        Log.d("c_view", "maxValue: " + maxValue);
        Log.d("c_view", "angleRate: " + angleRate);
        Log.d("c_view", "anglePer: " + anglePer);
        Log.d("c_view", "rotateAngle: " + rotateAngle);

        //刷新视图
        invalidate();
    }

    public void setAngleRate(int angleRate) {
        this.angleRate = angleRate;
    }
}
