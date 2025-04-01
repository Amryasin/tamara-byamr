package co.tamara.sdk.ui

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
    private var _binding: TamaraFragmentPaymentBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_ORDER = "order"
        const val ARG_PAYMENT_STATUS = "payment_status"
    }

    private var order: Order? = null
    private var checkoutSession: CheckoutSession? = null
    private lateinit var viewModel: TamaraPaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TamaraFragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            val status = it.getString(ARG_PAYMENT_STATUS)
            it.remove(ARG_PAYMENT_STATUS)
            status?.let { status ->
                when (PaymentStatus.valueOf(status)) {
                    PaymentStatus.STATUS_INITIALIZE -> {
                        order = it.getParcelable(ARG_ORDER)
                        order?.let { order ->
                            viewModel = ViewModelProvider(requireActivity()).get(TamaraPaymentViewModel::class.java)
                            if (savedInstanceState == null) {
                                viewModel.updateOrder(order)
                            }
                            viewModel.orderInfoLiveData.observe(viewLifecycleOwner, Observer {
                                when (it.status) {
                                    Status.LOADING -> {
                                        showLoading()
                                    }

                                    Status.SUCCESS -> {
                                        hideLoading()
                                        checkoutSession = it.data
                                        checkoutSession?.let {
                                            Logging.d(
                                                "API",
                                                "checkout session: " + checkoutSession?.checkout_url + " " + checkoutSession?.order_id
                                            )
                                            val intent = PaymentResult.successIntent(TamaraPaymentActivity.EXTRA_RESULT)
                                            intent.putExtra("CHECK_OUT_SESSION", checkoutSession)
                                            activity?.setResult(
                                                Activity.RESULT_OK, intent)
                                            activity?.finish()
                                        }

                                    }

                                    Status.ERROR -> {
                                        hideLoading()
                                        activity?.setResult(
                                            Activity.RESULT_OK,
                                            PaymentResult.failIntent(
                                                TamaraPaymentActivity.EXTRA_RESULT,
                                                PaymentError(it.message.toString())
                                            )
                                        )
                                        activity?.finish()
                                        Logging.d("API", it.message.toString())
                                    }
                                }
                            })
                        }
                    }

                    else -> {

                    }
                }

            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

}
