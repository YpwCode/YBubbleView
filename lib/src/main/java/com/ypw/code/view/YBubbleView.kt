package com.ypw.code.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * *************************
 * Ypw
 * ypwcode@163.com
 * 2020/9/28 10:26 AM
 * -------------------------
 * 角标气泡
 * *************************
 */
class YBubbleView(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private var mCount = -1
    private var mText = ""
    private var mTextColor = 0
    private var mTextSize = 0f
    private var mBgColor = 0
    private var mBorderColor = 0
    private var mBorderSize = 0f
    private var mRadius = 0f
    private var mTempRadius = 0f
    private var mCircle = false
    private var mRound = false

    private var mFontOffset = 0f
    private var mWidth = 0
    private var mHeight = 0
    private val mBgRect = RectF()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        attrs?.let {
            val types = context.obtainStyledAttributes(it, R.styleable.YBubbleView)
            val count = types.getInt(R.styleable.YBubbleView_bubble_count, 0)
            val text = types.getString(R.styleable.YBubbleView_bubble_text)
            mTextColor = types.getColor(R.styleable.YBubbleView_bubble_textColor, Color.WHITE)
            mTextSize = types.getDimension(R.styleable.YBubbleView_bubble_textSize, 28f)
            mBgColor = types.getColor(R.styleable.YBubbleView_bubble_bgColor, Color.RED)
            mBorderColor = types.getColor(R.styleable.YBubbleView_bubble_borderColor, Color.RED)
            mBorderSize = types.getDimension(R.styleable.YBubbleView_bubble_borderSize, 5f)
            mRadius = types.getDimension(R.styleable.YBubbleView_bubble_radius, 5f)
            mCircle = types.getBoolean(R.styleable.YBubbleView_bubble_circle, true)
            mRound = types.getBoolean(R.styleable.YBubbleView_bubble_round, true)
            types.recycle()
            if (text.isNullOrBlank()) {
                setCount(count)
            } else {
                setText(text)
            }
        }
        onInit()
    }

    private fun onInit() {
        mPaint.apply {
            color = mTextColor
            textSize = mTextSize
            textAlign = Paint.Align.CENTER
        }
        mBgPaint.color = mBgColor
        mBorderPaint.apply {
            color = mBorderColor
            strokeWidth = mBorderSize
            style = Paint.Style.STROKE
        }
    }

    private fun onComputer() {
        val fontMetrics = mPaint.fontMetrics
        mFontOffset = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent

        val bounds = Rect()
        mPaint.getTextBounds(mText, 0, mText.length, bounds)

        mWidth = paddingLeft + paddingRight + bounds.width() + mBorderSize.toInt() * 2
        mHeight = paddingTop + paddingBottom + bounds.height() + mBorderSize.toInt() * 2
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        onComputer()

        mWidth = Math.max(suggestedMinimumWidth, mWidth)
        mHeight = Math.max(suggestedMinimumHeight, mHeight)

        mWidth = getSize(mWidth, widthMeasureSpec)
        mHeight = getSize(mHeight, heightMeasureSpec)

        if (mRound) {
            val maxSize = Math.max(mWidth, mHeight)
            mWidth = maxSize
            mHeight = maxSize
        }

        setMeasuredDimension(mWidth, mHeight)

        mBgRect.left = mBorderSize / 2
        mBgRect.top = mBorderSize / 2
        mBgRect.right = mWidth - mBorderSize / 2
        mBgRect.bottom = mHeight - mBorderSize / 2

        mTempRadius = if (mCircle || mRound) {
            mBgRect.height() / 2
        } else {
            val minRadius = Math.min(mBgRect.width(), mBgRect.height())
            Math.min(minRadius / 2, mRadius)
        }
    }

    private fun getSize(size: Int, measureSpec: Int): Int {
        var result = size
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.AT_MOST -> result = size
            MeasureSpec.EXACTLY -> result = specSize
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(mBgRect, mTempRadius, mTempRadius, mBgPaint)
        canvas.drawRoundRect(mBgRect, mTempRadius, mTempRadius, mBorderPaint)
        canvas.drawText(mText, width / 2f, height / 2f + mFontOffset, mPaint)
    }

    fun setCount(count: Int) {
        if (mCount == count) {
            return
        }

        mCount = count

        visibility = if (mCount < 1) {
            GONE
        } else {
            VISIBLE
        }

        toRefresh(mCount.toString())
    }

    fun getCount(): Int {
        return mCount
    }

    fun setText(text: String?) {
        text?.let {
            if(it.isBlank() || mText == it) {
                return
            }
            toRefresh(it)
        }
    }

    private fun toRefresh(newText: String) {
        val oldText = mText
        mText = newText
        if (newText.length != oldText.length){
            requestLayout()
        } else {
            postInvalidate()
        }
    }
}