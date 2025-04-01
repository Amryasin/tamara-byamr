package co.tamara.sdk.repository

import androidx.lifecycle.LiveData
import co.tamara.sdk.AppExecutors
import co.tamara.sdk.api.ApiResponse
import co.tamara.sdk.api.ApiSuccessResponse
import co.tamara.sdk.api.Service
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.response.CheckoutSession
import co.tamara.sdk.vo.AbsentLiveData
import co.tamara.sdk.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CheckOutRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val service: Service
) {
    fun createOrder(order: Order): LiveData<Resource<CheckoutSession>>{
        return object : NetworkBoundResource<CheckoutSession, CheckoutSession>(appExecutors){

            override fun shouldFetch(data: CheckoutSession?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<CheckoutSession> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<ApiResponse<CheckoutSession>> {
                return service.createOrder(order)
            }

            override fun processResponse(response: ApiSuccessResponse<CheckoutSession>): CheckoutSession {
                return response.body
            }

        }.asLiveData()
    }
}