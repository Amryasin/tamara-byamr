package co.tamara.sdk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.tamara.sdk.DIHelper
import co.tamara.sdk.model.request.CancelOrder
import co.tamara.sdk.model.request.CapturePaymentRequest
import co.tamara.sdk.model.request.OrderReference
import co.tamara.sdk.model.request.PaymentOptions
import co.tamara.sdk.model.request.PaymentRefund
import co.tamara.sdk.model.response.*
import co.tamara.sdk.model.response.PaymentType
import co.tamara.sdk.model.response.orderdetail.OrderDetail
import co.tamara.sdk.repository.InformationRepository
import co.tamara.sdk.vo.Resource
import javax.inject.Inject

internal class TamaraInformationViewModel: ViewModel() {
    init {
        DIHelper.inject(this)
    }

    @Inject
    lateinit var repository: InformationRepository

    fun paymentTypeInfo(country: String?, currency: String?) : LiveData<Resource<ArrayList<PaymentType>>> {
        return repository.paymentType(country, currency)
    }

    fun orderDetail(orderId: String?) : LiveData<Resource<OrderDetail>> {
        return repository.orderDetail(orderId)
    }

    fun getCapturePayment(capturePayment: CapturePaymentRequest) : LiveData<Resource<CapturePayment>> {
        return repository.getCapturePayment(capturePayment)
    }

    fun refunds(orderId: String?, paymentRefund: PaymentRefund) : LiveData<Resource<RefundsResponse>> {
        return repository.refunds(orderId, paymentRefund)
    }

    fun updateOrderReference(orderId: String?, orderReference: OrderReference) : LiveData<Resource<co.tamara.sdk.model.response.OrderReference>> {
        return repository.updateOrderReference(orderId, orderReference)
    }

    fun cancelOrder(orderId: String?,cancelOrder: CancelOrder) : LiveData<Resource<CancelOrderResponse>> {
        return repository.cancelOrder(orderId, cancelOrder)
    }

    fun authoriseOrder(orderId: String?) : LiveData<Resource<AuthoriseOrder>> {
        return repository.authoriseOrder(orderId)
    }

    fun paymentOptions(paymentOptions: PaymentOptions) : LiveData<Resource<PaymentOptionsResponse>> {
        return repository.paymentOptions(paymentOptions)
    }
}