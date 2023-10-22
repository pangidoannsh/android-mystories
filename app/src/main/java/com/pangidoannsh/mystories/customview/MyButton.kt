package com.pangidoannsh.mystories.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.pangidoannsh.mystories.R

class MyButton : AppCompatButton {

    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.btn_primary) as Drawable
        disabledBackground =
            ContextCompat.getDrawable(context, R.drawable.btn_primary_disable) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        textSize = 16f
        gravity = Gravity.CENTER
    }
}