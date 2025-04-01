package co.tamara.sdk.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.FragmentActivity
import co.tamara.sdk.R
import java.util.*
import android.R.attr.x
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.style.DynamicDrawableSpan


class TamaraWidgetText(context: Context?, attrs: AttributeSet?) :
    LinearLayoutCompat(context!!, attrs) {
    private var contentTxt: TextView? = null
    private var mode: Int = MODE_INLINE
    private var value: Double = 0.0
    private var currency: String = "SAR"
    private var payIn: Int = 3
    private var unequal: Boolean = false

    init {
        val a: TypedArray =
            context!!.obtainStyledAttributes(attrs, R.styleable.TamaraWidgetText)

        mode = a.getInt(R.styleable.TamaraWidgetText_widget_mode, MODE_INLINE)
        value = a.getString(R.styleable.TamaraWidgetText_pay_value)?.toDouble() ?: 0.0
        currency = a.getString(R.styleable.TamaraWidgetText_currency) ?: "SAR"
        payIn = a.getInt(R.styleable.TamaraWidgetText_pay_in, 3)
        unequal = a.getBoolean(R.styleable.TamaraWidgetText_unequal, false)

        when (mode) {
            MODE_INLINE -> {
                setLayoutForInline()
            }
            MODE_BORDER -> {
                setLayoutForBorder()
            }
        }

        a.recycle()
    }

    private fun setLayoutForBorder() {
        val view = inflate(context, R.layout.tamara_widget_border, this)
        contentTxt = view.findViewById(R.id.twContentTxt)
        val symbol = Currency.getInstance(currency).symbol
        val valueString = context.getString(R.string.tw_amount_with_currency, symbol, String.format("%.2f",value/payIn))
        val learnMoreText = context.getString(R.string.tw_learn_more)
        val contentString =
            if (unequal) {
                context.getString(
                    R.string.tw_content_border_unequal,
                    payIn.toString(),
                    learnMoreText
                )
            } else {
                context.getString(
                    R.string.tw_content_border,
                    payIn.toString(),
                    valueString,
                    learnMoreText
                )
            }
        val spannable = SpannableString(contentString)

        val valueIndex = contentString.indexOf(valueString)
        if (valueIndex >= 0) {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                valueIndex,
                valueIndex + valueString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        val learnMoreSpan = object: ClickableSpan() {
            override fun onClick(view: View) {
                openInfoDialog()
            }
        }
        val learnMoreIndex = contentString.indexOf(learnMoreText)
        spannable.setSpan(learnMoreSpan, learnMoreIndex, learnMoreIndex + learnMoreText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        contentTxt?.text = spannable
        contentTxt?.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setLayoutForInline() {
        val view = inflate(context, R.layout.tamara_wdiget_inline, this)
        contentTxt = view.findViewById(R.id.twContentTxt)
        val symbol = Currency.getInstance(currency).symbol
        val valueString = context.getString(R.string.tw_amount_with_currency, symbol, String.format("%.2f",value/payIn))
        val learnMoreText = context.getString(R.string.tw_learn_more)
        val contentString =
            if (unequal) {
                context.getString(
                    R.string.tw_content_inline_unequal,
                    payIn.toString(),
                    learnMoreText
                )
            } else {
                context.getString(
                    R.string.tw_content_inline,
                    payIn.toString(),
                    valueString,
                    learnMoreText
                )
            }
        val tamaraDrawable = context.resources.getDrawable(R.drawable.tw_ic_logo_badge)
        tamaraDrawable.setBounds(0,0,tamaraDrawable.intrinsicWidth, tamaraDrawable.minimumHeight)
        val span = CenteredImageSpan(context, R.drawable.tw_ic_logo_badge)
        val keyword = "tamara_logo"
        val index = contentString.indexOf(keyword)
        val spannable = SpannableString(contentString)
        spannable.setSpan(span, index, index + keyword.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

        val learnMoreSpan = object: ClickableSpan() {
            override fun onClick(view: View) {
                openInfoDialog()
            }
        }
        val learnMoreIndex = contentString.indexOf(learnMoreText)
        spannable.setSpan(learnMoreSpan, learnMoreIndex, learnMoreIndex + learnMoreText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val valueIndex = contentString.indexOf(valueString)
        if (valueIndex >= 0) {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                valueIndex,
                valueIndex + valueString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        contentTxt?.text = spannable
        contentTxt?.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun openInfoDialog() {
        if (context is FragmentActivity) {
            val popup = TamaraWidgetPopup()
            popup.show((context as FragmentActivity).supportFragmentManager, TamaraWidgetPopup.TAG)
        }
    }


    companion object {
        const val MODE_INLINE = 0
        const val MODE_BORDER = 1

    }
}