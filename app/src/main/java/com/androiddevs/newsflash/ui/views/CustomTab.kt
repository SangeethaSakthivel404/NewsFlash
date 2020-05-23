package com.androiddevs.newsflash.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.androiddevs.newsflash.R
import kotlin.math.roundToInt

class CustomTab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        fun getChip(context: Context, initBlock: CustomTab.() -> Unit = {}): CustomTab {
            val chip = CustomTab(context)
            chip.apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
                initBlock()
            }
            return chip
        }

    }

    val defaultPadding by lazy {
        16 * context.resources.displayMetrics.density.roundToInt()
    }

    private val backgroundRect by lazy {
        Rect(
            0,
            0,
            measuredWidth,
            measuredHeight
        )
    }

    private val backgroundPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = Color.TRANSPARENT
            isAntiAlias = true
        }

    }

    fun setSelectedBackgroundColor(selectedColor: Int) {
        backgroundPaint.apply { color = selectedColor }
    }

    fun setSelectedTabWidth(width: Int) {
        backgroundRect.right = width
        requestLayout()
    }

    init {
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setBackgroundColor(Color.TRANSPARENT)
        setTextColor(ContextCompat.getColorStateList(context, R.color.tablayout_tab_text))
        setPadding(defaultPadding, defaultPadding / 2, defaultPadding, defaultPadding / 2)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawRoundRect(
                backgroundRect.toRectF(),
                measuredHeight.toFloat(),
                measuredHeight.toFloat(),
                backgroundPaint
            )
        }
        super.onDraw(canvas)
    }


}