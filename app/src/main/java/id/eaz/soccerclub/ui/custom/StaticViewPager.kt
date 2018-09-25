package id.eaz.soccerclub.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.KeyEvent
import id.eaz.soccerclub.R
import android.view.MotionEvent


class StaticViewPager(ctx : Context, attrs: AttributeSet) : ViewPager(ctx, attrs) {

    private val swipeable: Boolean

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StaticViewPager)
        try {
            swipeable = a.getBoolean(R.styleable.StaticViewPager_swipeable, true)
        } finally {
            a.recycle()
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (swipeable) super.onInterceptTouchEvent(event) else false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (swipeable) super.onTouchEvent(event) else false
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        return if (swipeable) super.executeKeyEvent(event) else false
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, false)
    }
}