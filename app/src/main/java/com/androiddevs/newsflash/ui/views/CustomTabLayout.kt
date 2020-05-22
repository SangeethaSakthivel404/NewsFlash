package com.androiddevs.newsflash.ui.views

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.androiddevs.newsflash.utils.toDp
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.math.roundToInt


class CustomTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val chipGroup by lazy { ChipGroup(context) }
    private val chipList: MutableList<Chip> = ArrayList()
    private var selectedIndex = 0

    private var onTabPressCallback: ((String) -> Unit)? = null

    private var parentViewGroup = LinearLayout(context)
    private var parentViewGroupScrollable = HorizontalScrollView(context)

    init {
        parentViewGroup.addView(chipGroup)
        parentViewGroupScrollable.apply {
            isHorizontalScrollBarEnabled = false
        }
        chipGroup.apply {
            isSingleLine = true
            chipSpacingHorizontal = 8F.toDp(context).roundToInt()
        }
    }


    fun initTabs(initBlock: CustomTabLayout.() -> Unit) {
        apply {
            initBlock()
        }
    }

    fun buildTabsWithText(vararg titleList: String) {
        titleList.forEachIndexed { index, title ->
            addTab(title)
        }
    }

    fun addTab(titleList: String) {
        val chip: Chip = getChip(context) {
            id = View.generateViewId()
            text = titleList
            isSelected = chipList.isEmpty()
            isClickable = true
            setOnClickListener(this@CustomTabLayout)
        }
        chipGroup.addView(chip)
        chipList.add(chip)
    }

    private fun setLayoutParams() {
        var width = 0
        val display = display
        display?.let {

            val pointWidth = Point()
            val pointHeight = Point()

            display.getSize(pointWidth)
            display.getSize(pointHeight)
            chipList.forEach {
                it.measure(pointWidth.x, pointHeight.y)
                width += it.measuredWidth
            }

            if (width + 100 > context.resources.displayMetrics.widthPixels) {
                parentViewGroup.removeAllViews()
                removeAllViews()
                parentViewGroupScrollable.addView(chipGroup)
                addView(parentViewGroupScrollable)
            } else {
                addView(parentViewGroup)
            }
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            selectedIndex = v.id
            chipList.forEach {
                it.isSelected = it.id == selectedIndex
            }
        }
    }

    fun setOnTabChangeCallback(onTabPressCallback: (String) -> Unit) {
        this.onTabPressCallback = onTabPressCallback
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setLayoutParams()
    }
}
