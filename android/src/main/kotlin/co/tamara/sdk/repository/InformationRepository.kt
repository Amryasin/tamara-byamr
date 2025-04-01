package co.tamara.sdk.repository

import androidx.lifecycle.LiveData
import co.tamara.sdk.AppExecutors
import co.tamara.sdk.api.ApiResponse
import co.tamara.sdk.api.ApiSuccessResponse
import co.tamara.sdk.api.Service
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.request.CancelOrder
import co.tamara.sdk.model.request.CapturePaymentRequest
import co.tamara.sdk.model.request.PaymentOptions
import co.tamara.sdk.model.request.PaymentRefund
import co.tamara.sdk.model.response.*
import co.tamara.sdk.model.response.PaymentType
import co.tamara.sdk.model.response.orderdetail.OrderDetail
import co.tamara.sdk.vo.AbsentLiveData
import co.tamara.sdk.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class InformationRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val service: Service
) {
    fun paymentType(country: String?, currency: String?): LiveData<Resource<ArrayList<PaymentType>>> {
        return object : NetworkBoundResource<ArrayList<PaymentType>, ArrayList<PaymentType>>(appExecutors){

            override fun shouldFetch(data: ArrayList<PaymentType>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<ArrayList<PaymentType>> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<ArrayList<PaymentType>>> {
                return service.paymentType(country, currency)
            }

            override fun processResponse(response: ApiSuccessResponse<ArrayList<PaymentType>>): ArrayList<PaymentType> {
                return response.body
            }
        }.asLiveData()
    }

    fun orderDetail(orderId: String?): LiveData<Resource<OrderDetail>> {
        return object : NetworkBoundResource<OrderDetail, OrderDetail>(appExecutors){

            override fun shouldFetch(data: OrderDetail?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<OrderDetail> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<OrderDetail>> {
                return service.getOrderDetail(orderId)
            }

            override fun processResponse(response: ApiSuccessResponse<OrderDetail>): OrderDetail {
                return response.body
            }
        }.asLiveData()
    }

    fun getCapturePayment(capturePayment: CapturePaymentRequest): LiveData<Resource<CapturePayment>> {
        return object : NetworkBoundResource<CapturePayment, CapturePayment>(appExecutors){

            override fun shouldFetch(data: CapturePayment?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<CapturePayment> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<CapturePayment>> {
                return service.getCapturePayment(capturePayment)
            }

            override fun processResponse(response: ApiSuccessResponse<CapturePayment>): CapturePayment {
                return response.body
            }
        }.asLiveData()
    }

    fun refunds(orderId: String?, paymentRefund: PaymentRefund): LiveData<Resource<RefundsResponse>>{
        return object : NetworkBoundResource<RefundsResponse, RefundsResponse>(appExecutors){

            override fun shouldFetch(data: RefundsResponse?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<RefundsResponse> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<RefundsResponse>> {
                return service.refunds(orderId, paymentRefund)
            }

            override fun processResponse(response: ApiSuccessResponse<RefundsResponse>): RefundsResponse {
                return response.body
            }

        }.asLiveData()
    }

    fun cancelOrder(orderId: String?, cancelOrder: CancelOrder): LiveData<Resource<CancelOrderResponse>>{
        return object : NetworkBoundResource<CancelOrderResponse, CancelOrderResponse>(appExecutors){

            override fun shouldFetch(data: CancelOrderResponse?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<CancelOrderResponse> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<CancelOrderResponse>> {
                return service.cancelOrder(orderId, cancelOrder)
            }

            override fun processResponse(response: ApiSuccessResponse<CancelOrderResponse>): CancelOrderResponse {
                return response.body
            }

        }.asLiveData()
    }

    fun updateOrderReference(orderId: String?, orderReference: co.tamara.sdk.model.request.OrderReference): LiveData<Resource<OrderReference>>{
        return object : NetworkBoundResource<OrderReference, OrderReference>(appExecutors){

            override fun shouldFetch(data: OrderReference?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<OrderReference> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<OrderReference>> {
                return service.updateOrderReference(orderId, orderReference)
            }

            override fun processResponse(response: ApiSuccessResponse<OrderReference>): OrderReference {
                return response.body
            }
        }.asLiveData()
    }

    fun authoriseOrder(orderId: String?): LiveData<Resource<AuthoriseOrder>> {
        return object : NetworkBoundResource<AuthoriseOrder, AuthoriseOrder>(appExecutors){

            override fun shouldFetch(data: AuthoriseOrder?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<AuthoriseOrder> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<AuthoriseOrder>> {
                return service.authoriseOrder(orderId)
            }

            override fun processResponse(response: ApiSuccessResponse<AuthoriseOrder>): AuthoriseOrder {
                return response.body
            }
        }.asLiveData()
    }

    fun paymentOptions(paymentOptions: PaymentOptions): LiveData<Resource<PaymentOptionsResponse>>{
        return object : NetworkBoundResource<PaymentOptionsResponse, PaymentOptionsResponse>(appExecutors){

            override fun shouldFetch(data: PaymentOptionsResponse?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<PaymentOptionsResponse> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<PaymentOptionsResponse>> {
                return service.paymentOptions(paymentOptions)
            }

            override fun processResponse(response: ApiSuccessResponse<PaymentOptionsResponse>): PaymentOptionsResponse {
                return response.body
            }

        }.asLiveData()
    }
}