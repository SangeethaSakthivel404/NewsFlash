package com.androiddevs.newsflash.ui.views

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import com.androiddevs.newsflash.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class CustomTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr), View.OnClickListener {


    interface ICustomTabLayoutPlatformActions {
        fun getColor(context: Context): ColorStateList?

        fun getTextColor(context: Context): ColorStateList?
    }


    private val linearLayout: ChipGroup = ChipGroup(context)
    private val chipList: MutableList<Chip> = ArrayList()
    private var selectedIndex = 0

    private lateinit var onTabPressCallback: (String) -> Unit

    init {
        isFillViewport = true
        addView(linearLayout)
    }


    fun setTabs(arrayTabNames: Array<String>) {
        for (index in arrayTabNames.indices) {
            val chip: Chip = ChipFactory.getChipInstance(context).apply {
                tag = index
                text = arrayTabNames[index]
                if (index == 0) {
                    isSelected = true
                }
                setOnClickListener(this@CustomTabLayout)
            }
            linearLayout.addView(chip)
            chipList.add(chip)
        }
        setLayoutParams()
    }

    private fun setLayoutParams() {
        if (chipList.size < 4) {
            linearLayout.apply {
                chipSpacingHorizontal = (16 * resources.displayMetrics.density).toInt()
                isSingleLine = true
            }
        }
    }

    override fun onClick(v: View?) {
        (v as Chip).let {
            val viewTag = Integer.parseInt(it.tag.toString())
            if (viewTag != selectedIndex) {
                chipList[selectedIndex].isSelected = false
                selectedIndex = viewTag
                chipList[selectedIndex].isSelected = true
                onTabPressCallback(it.text.toString())
            }
        }
    }

    fun setOnTabChangeCallback(onTabPressCallback: (String) -> Unit) {
        this.onTabPressCallback = onTabPressCallback
    }


    @RestrictTo(RestrictTo.Scope.LIBRARY)
    class ChipFactory {


        companion object {

            private const val CHIP_PADDING = 8

            private val customTabLayoutPlatformActions: ICustomTabLayoutPlatformActions =
                if (Build.VERSION.SDK_INT > 22)
                    CustomTabLayoutImplAPI23()
                else
                    CustomTabLayoutImplAPI21()


            fun getChipInstance(context: Context): Chip {
                return Chip(
                    context,
                    null,
                    R.style.Widget_MaterialComponents_Chip_Choice
                ).apply {
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    chipStartPadding = CHIP_PADDING * context.resources.displayMetrics.density
                    chipEndPadding = CHIP_PADDING * context.resources.displayMetrics.density
                    setTextColor(customTabLayoutPlatformActions.getTextColor(context))
                    chipBackgroundColor = customTabLayoutPlatformActions.getColor(context)
                }
            }
        }
    }


    @TargetApi(23)
    private class CustomTabLayoutImplAPI23 : ICustomTabLayoutPlatformActions {

        override fun getColor(context: Context): ColorStateList {
            return context.resources.getColorStateList(R.color.tablayout_tab_background, null)
        }

        override fun getTextColor(context: Context): ColorStateList {
            return context.resources.getColorStateList(R.color.tablayout_tab_text, null)
        }
    }

    @TargetApi(21)
    private class CustomTabLayoutImplAPI21 : ICustomTabLayoutPlatformActions {

        override fun getColor(context: Context): ColorStateList? {
            return ContextCompat.getColorStateList(context, R.color.tablayout_tab_background)
        }

        override fun getTextColor(context: Context): ColorStateList? {
            return ContextCompat.getColorStateList(context, R.color.tablayout_tab_text)
        }
    }
}