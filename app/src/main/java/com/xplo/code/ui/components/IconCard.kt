package com.xplo.code.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import com.xplo.code.R
import com.xplo.code.databinding.ViewIconCardBinding

class IconCard(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var _binding: ViewIconCardBinding? = null
    private val binding: ViewIconCardBinding get() = _binding!!

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_icon_card, this)
        _binding = ViewIconCardBinding.inflate(LayoutInflater.from(context), this, true)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.IconCard)
        try {
            val text = ta.getString(R.styleable.IconCard_title)
//            val subtitle = ta.getString(R.styleable.FeatureRow_subtitle)
            val drawableId = ta.getResourceId(R.styleable.IconCard_icon, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                binding.ivIcon.setImageDrawable(drawable)
            }
            binding.tvTitle.text = text
            //binding.tvSubtitle.text = subtitle
        } finally {
            ta.recycle()
            _binding = null
        }
    }

//    private fun setTextColorUponTheTheme(tvTitle: TextView) {
//        // Get the primary text color of the theme
//        val typedValue = TypedValue()
//        val theme: Resources.Theme = context.theme
//        theme.resolveAttribute(R.attr.colorPrimaryText, typedValue, true)
//        val arr: TypedArray =
//            context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimaryText))
//        val primaryColor = arr.getColor(0, -1)
//        tvTitle.setTextColor(primaryColor)
//    }

}