package co.tamara.sdk.ui

import android.app.Activity
import androidx.lifecycle.*
import co.tamara.sdk.DIHelper
import co.tamara.sdk.PaymentResult
import co.tamara.sdk.TamaraPayment
import co.tamara.sdk.TamaraPaymentActivity
import co.tamara.sdk.error.PaymentError
import co.tamara.sdk.log.Logging
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.response.CheckoutSession
import co.tamara.sdk.model.response.PaymentType
import co.tamara.sdk.repository.CheckOutRepository
import co.tamara.sdk.vo.Resource
import co.tamara.sdk.vo.Status
import javax.inject.Inject


internal class TamaraPaymentViewModel : ViewModel() {
    init {
        DIHelper.inject(this)
    }

    @Inject
    lateinit var repository: CheckOutRepository

    private var orderLiveData = MutableLiveData<Order>()

    var orderInfoLiveData: LiveData<Resource<CheckoutSession>> = Transformations.switchMap(orderLiveData){
        repository.createOrder(it)
    }

    fun updateOrder(order: Order){
        orderLiveData.postValue(order)
    }
}
