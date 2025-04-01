package co.tamara.sdk.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.tamara.sdk.InformationResult
import co.tamara.sdk.R
import co.tamara.sdk.const.Information
import co.tamara.sdk.databinding.TamaraInformationFragmentBinding
import co.tamara.sdk.error.PaymentError
import co.tamara.sdk.log.Logging
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.request.CancelOrder
import co.tamara.sdk.model.request.CapturePaymentRequest
import co.tamara.sdk.model.request.OrderReference
import co.tamara.sdk.model.request.PaymentOptions
import co.tamara.sdk.model.request.PaymentRefund
import co.tamara.sdk.model.response.AuthoriseOrder
import co.tamara.sdk.model.response.CancelOrderResponse
import co.tamara.sdk.model.response.RefundsResponse
import co.tamara.sdk.model.response.PaymentOptionsResponse
import co.tamara.sdk.model.response.orderdetail.OrderDetail
import co.tamara.sdk.vo.Status

internal class TamaraInformationFragment: Fragment() {
    companion object {
        const val ARG_CAPTURE= "capture_payment"
        const val ARG_ORDER_ID= "order_id"
        const val ARG_REFUNDS= "refunds"
        const val ARG_REFERENCE= "reference"
        const val ARG_CANCEL= "cancel_order"
        const val ARG_PAYMENT_OPTIONS= "payment_options"
        const val ARG_TYPE= "type"
    }
    private lateinit var viewModel: TamaraInformationViewModel
    private var capturePayment: CapturePaymentRequest? = null
    private var paymentRefund: PaymentRefund? = null
    private var orderReference: OrderReference? = null
    private var cancelOrder: CancelOrder? = null
    private lateinit var binding: TamaraInformationFragmentBinding
    private var paymentOptions: PaymentOptions? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TamaraInformationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(TamaraInformationViewModel::class.java)
        arguments?.let {
            val orderId = it.getString(ARG_ORDER_ID)
            val type = it.getString(ARG_TYPE)
            capturePayment = it.getParcelable(ARG_CAPTURE)
            paymentRefund = it.getParcelable(ARG_REFUNDS)
            orderReference = it.getParcelable(ARG_REFERENCE)
            cancelOrder = it.getParcelable(ARG_CANCEL)
            paymentOptions = it.getParcelable(ARG_PAYMENT_OPTIONS)
            it.remove(ARG_ORDER_ID)
            it.remove(ARG_CAPTURE)
            it.remove(ARG_REFUNDS)
            it.remove(ARG_REFERENCE)
            it.remove(ARG_CANCEL)
            it.remove(ARG_PAYMENT_OPTIONS)
            it.remove(ARG_TYPE)
            when (type) {
                Information.ORDER_DETAIL.toString() -> {
                    viewModel.orderDetail(orderId).observe(viewLifecycleOwner, Observer {
                        when(it.status){
                            Status.LOADING -> {
                                showLoading()
                            }
                            Status.SUCCESS -> {
                                hideLoading()
                                val intent = InformationResult.successIntent("INFORMATION_RESULT")
                                val orderDetail: OrderDetail? = it.data
                                intent.putExtra(Information.ORDER_DETAIL.toString(), orderDetail)
                                activity?.setResult(
                                    Activity.RESULT_OK, intent)
                                activity?.finish()
                            }

                            Status.ERROR -> {
                                hideLoading()
                                val intent = InformationResult.failIntent("INFORMATION_RESULT", PaymentError(it.message.toString()))
                                activity?.setResult(Activity.RESULT_OK, intent)
                                activity?.finish()
                            }
                        }
                    })
                }
                Information.CAPTURE_PAYMENT.toString() -> {
                    capturePayment?.let {
                        viewModel.getCapturePayment(capturePayment!!).observe(viewLifecycleOwner, Observer {
                            when(it.status){
                                Status.LOADING -> {
                                    showLoading()
                                }
                                Status.SUCCESS -> {
                                    hideLoading()
                                    val intent = InformationResult.successIntent("INFORMATION_RESULT")
                                    intent.putExtra(Information.CAPTURE_PAYMENT.toString(), it.data)
                                    activity?.setResult(
                                        Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }

                                Status.ERROR -> {
                                    hideLoading()
                                    val intent = InformationResult.failIntent("INFORMATION_RESULT", PaymentError(it.message.toString()))
                                    activity?.setResult(Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }
                            }
                        })
                    }
                }
                Information.REFUNDS.toString() -> {
                    paymentRefund?.let {
                        viewModel.refunds(orderId, paymentRefund!!).observe(viewLifecycleOwner, Observer {
                            when(it.status){
                                Status.LOADING -> {
                                    showLoading()
                                }
                                Status.SUCCESS -> {
                                    hideLoading()
                                    val intent = InformationResult.successIntent("INFORMATION_RESULT")
                                    val refunds: RefundsResponse? = it.data
                                    intent.putExtra(Information.REFUNDS.toString(), refunds)
                                    activity?.setResult(
                                        Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }

                                Status.ERROR -> {
                                    hideLoading()
                                    val intent = InformationResult.failIntent("INFORMATION_RESULT", PaymentError(it.message.toString()))
                                    activity?.setResult(Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }
                            }
                        })
                    }
                }
                Information.REFERENCE.toString() -> {
                    orderReference?.let {
                        viewModel.updateOrderReference(orderId, orderReference!!).observe(viewLifecycleOwner, Observer {
                            when(it.status){
                                Status.LOADING -> {
                                    showLoading()
                                }
                                Status.SUCCESS -> {
                                    hideLoading()
                                    val intent = InformationResult.successIntent("INFORMATION_RESULT")
                                    val orderReference: co.tamara.sdk.model.response.OrderReference? = it.data
                                    intent.putExtra(Information.REFERENCE.toString(), orderReference)
                                    activity?.setResult(
                                        Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }

                                Status.ERROR -> {
                                    hideLoading()
                                    val intent = InformationResult.failIntent("INFORMATION_RESULT", PaymentError(it.message.toString()))
                                    activity?.setResult(Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }
                            }
                        })
                    }
                }
                Information.CANCEL.toString() -> {
                    cancelOrder?.let {
                        viewModel.cancelOrder(orderId, cancelOrder!!).observe(viewLifecycleOwner, Observer {
                            when(it.status){
                                Status.LOADING -> {
                                    showLoading()
                                }
                                Status.SUCCESS -> {
                                    hideLoading()
                                    val intent = InformationResult.successIntent("INFORMATION_RESULT")
                                    val cancelOrderResponse: CancelOrderResponse? = it.data
                                    intent.putExtra(Information.CANCEL.toString(), cancelOrderResponse)
                                    activity?.setResult(
                                        Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }

                                Status.ERROR -> {
                                    hideLoading()
                                    val intent = InformationResult.failIntent("INFORMATION_RESULT", PaymentError(it.message.toString()))
                                    activity?.setResult(Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }
                            }
                        })
                    }
                }
                Information.AUTHORISE.toString() -> {
                    orderId?.let {
                        viewModel.authoriseOrder(it).observe(viewLifecycleOwner, Observer {
                            when(it.status){
                                Status.LOADING -> {
                                    showLoading()
                                }
                                Status.SUCCESS -> {
                                    hideLoading()
                                    val intent = InformationResult.successIntent("INFORMATION_RESULT")
                                    val authoriseOrder: AuthoriseOrder? = it.data
                                    intent.putExtra(Information.AUTHORISE.toString(), authoriseOrder)
                                    activity?.setResult(
                                        Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }

                                Status.ERROR -> {
                                    hideLoading()
                                    val intent = InformationResult.failIntent("INFORMATION_RESULT", PaymentError(it.message.toString()))
                                    activity?.setResult(Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }
                            }
                        })
                    }
                }
                Information.PAYMENT_OPTIONS.toString() -> {
                    paymentOptions?.let {
                        viewModel.paymentOptions(it).observe(viewLifecycleOwner, Observer {
                            when(it.status){
                                Status.LOADING -> {
                                    showLoading()
                                }
                                Status.SUCCESS -> {
                                    hideLoading()
                                    val intent = InformationResult.successIntent("INFORMATION_RESULT")
                                    val paymentOptions: PaymentOptionsResponse? = it.data
                                    intent.putExtra(Information.PAYMENT_OPTIONS.toString(), paymentOptions)
                                    activity?.setResult(
                                        Activity.RESULT_OK, intent)
                                    activity?.finish()
                                }

                                Status.ERROR -> {
                                    hideLoading()
                                    val intent = InformationResult.failIntent("INFORMATION_RESULT", PaymentError(it.message.toString()))
                                    activity?.setResult(Activity.RESULT_OK, intent)
                                    activity?.finish()
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

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.GONE
    }
}