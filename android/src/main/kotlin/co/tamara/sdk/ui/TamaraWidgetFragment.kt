package co.tamara.sdk.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.tamara.sdk.InformationResult
import co.tamara.sdk.R
import co.tamara.sdk.TamaraPayment
import co.tamara.sdk.const.Information
import co.tamara.sdk.model.request.WidgetProperties
import co.tamara.sdk.model.response.CartPage
import co.tamara.sdk.model.response.Product

class TamaraWidgetFragment: Fragment() {
    companion object {
        const val ARG_TYPE = "type"
        const val ARG_PROPERTIES = "properties"
        const val CART_PAGE = "CART"
        const val PRODUCT = "PRODUCT"
    }
    private var properties: WidgetProperties? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.tamara_fragment_widget, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            properties = it.getParcelable(ARG_PROPERTIES)
            val type = it.getString(ARG_TYPE)
            it.remove(ARG_PROPERTIES)
            it.remove(ARG_TYPE)
            properties?.let {
                when(type) {
                    CART_PAGE -> {
                        val intent = InformationResult.successIntent("INFORMATION_RESULT")
                        intent.putExtra(Information.CART.toString(), CartPage(script = generateUI(it.language, it.country, it.publicKey, it.amount, "3"),
                        url = generateURL(it.language, it.country, it.publicKey, it.amount, "3")))
                        activity?.setResult(
                            Activity.RESULT_OK, intent)
                        activity?.finish()

                    }
                    PRODUCT -> {
                        val intent = InformationResult.successIntent("INFORMATION_RESULT")
                        intent.putExtra(Information.PRODUCT.toString(), Product(script = generateUI(it.language, it.country, it.publicKey, it.amount, "2"),
                            url = generateURL(it.language, it.country, it.publicKey, it.amount, "2")))
                        activity?.setResult(
                            Activity.RESULT_OK, intent)
                        activity?.finish()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun generateUI(language: String, country: String, publicKey: String,
                   amount: Double, inline: String): String {
        var srcScript = "https://cdn-sandbox.tamara.co/widget-v2/tamara-widget.js"
        if (!TamaraPayment.getInstance().isSandbox) {
            srcScript = "https://cdn.tamara.co/widget-v2/tamara-widget.js"
        }
        return "<script>\n" +
                "        window.tamaraWidgetConfig = {\n" +
                "            lang: \"$language\",\n" +
                "            country: \"$country\",\n" +
                "            publicKey: \"$publicKey\"\n" +
                "        }\n" +
                "      </script>\n" +
                "      <script defer type=\"text/javascript\" src=\"$srcScript\"></script>\n" +
                "\n" +
                "      \n" +
                "      <tamara-widget type=\"tamara-summary\" amount=\"$amount\" inline-type=\"$inline\"></tamara-widget>"
    }

    fun generateURL(language: String, country: String, publicKey: String,
                   amount: Double, inline: String): String {
        if (TamaraPayment.getInstance().isSandbox) {
            return "https://cdn-sandbox.tamara.co/widget-v2/tamara-widget.html?lang=$language&public_key=$publicKey&country=$country&amount=$amount&inline_type=$inline"
        }

        return "https://cdn.tamara.co/widget-v2/tamara-widget.html?lang=$language&public_key=$publicKey&country=$country&amount=$amount&inline_type=$inline"
    }
}