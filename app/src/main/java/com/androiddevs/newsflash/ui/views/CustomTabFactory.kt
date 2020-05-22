package com.androiddevs.newsflash.ui.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.androiddevs.newsflash.R
import com.google.android.material.chip.Chip

private const val CHIP_PADDING = 20

fun getChip(context: Context, initBlock: Chip.() -> Unit): Chip {
    return Chip(context).apply {
        initBlock()
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        chipStartPadding = CHIP_PADDING * context.resources.displayMetrics.density
        chipEndPadding = CHIP_PADDING * context.resources.displayMetrics.density
        setTextColor(ContextCompat.getColorStateList(context, R.color.tablayout_tab_text))
        chipBackgroundColor =
            ContextCompat.getColorStateList(context, R.color.tablayout_tab_background)
    }
}
