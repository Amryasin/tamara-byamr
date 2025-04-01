package co.tamara.sdk.ui

import androidx.lifecycle.*
import co.tamara.sdk.DIHelper
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.response.CheckoutSession
import co.tamara.sdk.model.response.PaymentType
import co.tamara.sdk.repository.CheckOutRepository
import co.tamara.sdk.util.SingleLiveEvent
import co.tamara.sdk.vo.Resource
import co.tamara.sdk.vo.Status
import javax.inject.Inject

internal class TamaraPaymentViewModel : ViewModel() {
    init {
        DIHelper.inject(this)
    }

    @Inject
    lateinit var repository: CheckOutRepository

    private var orderLiveData = SingleLiveEvent<Order>()

    var orderInfoLiveData: LiveData<Resource<CheckoutSession>> = orderLiveData.switchMap{
        repository.createOrder(it)
    }

    fun updateOrder(order: Order){
        orderLiveData.postValue(order)
    }
}
