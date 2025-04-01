package co.tamara.sdk.ui

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import co.tamara.sdk.PaymentResult
import co.tamara.sdk.R
import co.tamara.sdk.TamaraPayment
import co.tamara.sdk.TamaraPaymentActivity
import co.tamara.sdk.const.PaymentStatus
import co.tamara.sdk.databinding.TamaraFragmentPaymentBinding
import co.tamara.sdk.error.PaymentError
import co.tamara.sdk.log.Logging
import co.tamara.sdk.model.MerchantUrl
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.response.CheckoutSession
import co.tamara.sdk.vo.Status

internal class TamaraPaymentFragment : Fragment() {

    companion object {
        const val ARG_ORDER = "order"
        const val ARG_PAYMENT_STATUS = "payment_status"
        const val ARG_CHECKOUT_SESSION = "checkout_session"
    }

    private var order: Order? = null
    private var checkoutSession: CheckoutSession? = null
    private lateinit var viewModel: TamaraPaymentViewModel
    private lateinit var binding: TamaraFragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TamaraFragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(TamaraPaymentViewModel::class.java)
        arguments?.let {
            val status = it.getString(ARG_PAYMENT_STATUS)
            it.remove(ARG_PAYMENT_STATUS)
            status?.let {status ->
                when(PaymentStatus.valueOf(status)){
                    PaymentStatus.STATUS_SUCCESS ->{
                        TamaraPayment.endSession()
                        activity?.setResult(Activity.RESULT_OK, PaymentResult.successIntent(TamaraPaymentActivity.EXTRA_RESULT))
                        activity?.finish()
                    }
                    PaymentStatus.STATUS_CANCEL -> {
                        activity?.setResult(Activity.RESULT_OK, PaymentResult.cancelIntent(TamaraPaymentActivity.EXTRA_RESULT))
                        activity?.finish()
                    }
                    PaymentStatus.STATUS_ERROR -> {
                        activity?.setResult(Activity.RESULT_OK, PaymentResult.failIntent(TamaraPaymentActivity.EXTRA_RESULT,  PaymentError("Something went wrong!")))
                        activity?.finish()
                    }
                    PaymentStatus.STATUS_INITIALIZE -> {
                        order = it.getParcelable(ARG_ORDER)
                        checkoutSession = it.getParcelable(ARG_CHECKOUT_SESSION)
                        order?.let { order ->
                            if (savedInstanceState == null) {
                                viewModel.updateOrder(order)
                            }
                        } ?: run {
                            checkoutSession?.let {
//                                processCheckout()
                            }
                        }
                    }
                    else -> {

                    }
                }

            }
        }
        viewModel.orderInfoLiveData.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    hideLoading()
                    checkoutSession = it.data
                    val intent = PaymentResult.successIntent(TamaraPaymentActivity.EXTRA_RESULT)
                    intent.putExtra("CHECK_OUT_SESSION", it.data)
                    activity?.setResult(
                        Activity.RESULT_OK, intent)
                    activity?.finish()

                    Logging.d("API", "checkout session: "+checkoutSession?.checkout_url + " "+checkoutSession?.order_id )
//                    processCheckout()
            }

                Status.ERROR -> {
                    hideLoading()
                    activity?.setResult(Activity.RESULT_OK, PaymentResult.failIntent(TamaraPaymentActivity.EXTRA_RESULT,  PaymentError(it.message.toString())))
                    activity?.finish()
                    Logging.d("API", it.message.toString())
                }
            }
        })
    }

    private fun processCheckout() {
        checkoutSession?.let {
            var bundle = Bundle()
            bundle.putString(WebViewFragment.ARG_URL,  it.checkout_url)
            order?.merchantUrl?.let {
                bundle.putParcelable(WebViewFragment.ARG_MERCHANT_URL, order?.merchantUrl)
            } ?: run {
                bundle.putParcelable(WebViewFragment.ARG_MERCHANT_URL, MerchantUrl(TamaraPayment.getInstance().pushUrl,
                    TamaraPayment.getInstance().cancelUrl, TamaraPayment.getInstance().failureUrl, TamaraPayment.getInstance().successUrl))
            }
            findNavController(this).navigate(R.id.webViewFragment, bundle)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.GONE
    }

}
