/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.tamara.sdk.api

import androidx.lifecycle.LiveData
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.request.CancelOrder
import co.tamara.sdk.model.request.CapturePaymentRequest
import co.tamara.sdk.model.request.PaymentOptions
import co.tamara.sdk.model.request.PaymentRefund
import co.tamara.sdk.model.response.*
import co.tamara.sdk.model.response.CheckoutSession
import co.tamara.sdk.model.response.PaymentType
import co.tamara.sdk.model.response.orderdetail.OrderDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points
 */
internal interface Service {
    @POST("checkout")
    fun createOrder(@Body order: Order): LiveData<ApiResponse<CheckoutSession>>

    @POST("checkout/payment-options-pre-check")
    fun paymentOptions(@Body paymentOptions: PaymentOptions): LiveData<ApiResponse<PaymentOptionsResponse>>
    @GET("checkout/payment-types")
    fun paymentType(@Query("country") country: String?, @Query("currency") currency: String?): LiveData<ApiResponse<ArrayList<PaymentType>>>

    @GET("orders/{order_id}")
    fun getOrderDetail(@Path("order_id") orderId: String?): LiveData<ApiResponse<OrderDetail>>

    @POST("payments/capture")
    fun getCapturePayment(@Body capturePayment: CapturePaymentRequest): LiveData<ApiResponse<CapturePayment>>

    @POST("payments/simplified-refund/{orderId}")
    fun refunds(@Path("orderId") orderId: String?, @Body paymentRefund: PaymentRefund): LiveData<ApiResponse<RefundsResponse>>

    @POST("orders/{orderId}/cancel")
    fun cancelOrder(@Path("orderId") orderId: String?, @Body cancelOrder: CancelOrder): LiveData<ApiResponse<CancelOrderResponse>>

    @PUT("orders/{orderId}/reference-id")
    fun updateOrderReference(@Path("orderId") orderId: String?, @Body orderReference: co.tamara.sdk.model.request.OrderReference): LiveData<ApiResponse<OrderReference>>

    @POST("orders/{orderId}/authorise")
    fun authoriseOrder(@Path("orderId") orderId: String?): LiveData<ApiResponse<AuthoriseOrder>>
}