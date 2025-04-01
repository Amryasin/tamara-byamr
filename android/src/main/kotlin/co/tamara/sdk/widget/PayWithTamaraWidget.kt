package co.tamara.sdk.widget

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageButton
import co.tamara.sdk.R
import co.tamara.sdk.databinding.TwInfoDialogBinding

class PayWithTamaraWidget(context: Context?, attrs: AttributeSet?) : AppCompatImageButton(context!!, attrs) {
    constructor(context: Context?) : this(context, null)

    init {
        setImageResource(R.drawable.tw_ic_cart)
        setBackgroundColor(Color.TRANSPARENT)
        setOnClickListener {
            showInfoDialog()
        }
    }

    private fun openLearnMore() {
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://tamara.co/"))
        context.startActivity(intent)
    }

    private fun showInfoDialog() {
        var builder = AlertDialog.Builder(context)
        var view: TwInfoDialogBinding = TwInfoDialogBinding.inflate(LayoutInflater.from(context), null, false)
        builder.setView(view.root)
        var dialog = builder.show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.tw_dialog_bg)
        view.twCloseBtn.setOnClickListener {
            dialog.dismiss()
        }
        view.twFooterLearnMoreTxt.text = Html.fromHtml(context.getString(R.string.tw_info_footer_subtitle))
        view.twFooterLearnMoreTxt.setOnClickListener {
            openLearnMore()
        }
    }
}