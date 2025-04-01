package co.tamara.sdk.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import androidx.annotation.DrawableRes


class CenteredImageSpan @JvmOverloads constructor(
    val context: Context?, @DrawableRes val resourceId: Int
) : DynamicDrawableSpan(ALIGN_BOTTOM) {


    override fun getDrawable(): Drawable {
        val drawable: Drawable? = context?.resources?.getDrawable(resourceId)
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        return drawable!!
    }

}