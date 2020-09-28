package com.ypw.code.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ypw.code.temp.R

class TriangleView(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        attrs?.let {
            val types = context.obtainStyledAttributes(it, R.styleable.TriangleView)
            val color = types.getColor(R.styleable.TriangleView_tri_color, Color.BLACK)
            types.recycle()
            paint.color = color
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = measuredWidth
        val height = measuredHeight
        path.reset()
        path.moveTo(width / 2f, 0f)
        path.lineTo(0f, height.toFloat())
        path.lineTo(width.toFloat(), height.toFloat())
        path.close()
        canvas?.drawPath(path, paint)
    }

}