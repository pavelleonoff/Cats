package com.redprism.cats.animation

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class PageImageTransform : ViewPager2.PageTransformer {
    private val MIN_SCALE = 0.75f

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        if (position < -1) {
            view.alpha = 0f
        } else if (position <= 0) {
            view.alpha = 1f
            view.translationX = 0f
            view.translationZ = 0f
            view.scaleX = 1f
            view.scaleY = 1f
        } else if (position <= 1) {
            view.alpha = 1 - position
            view.translationX = pageWidth * -position
            view.translationZ = -1f
            val scaleFactor = (MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - abs(position)))
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        } else {
            view.alpha = 0f
        }
    }
}