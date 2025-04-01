package co.tamara.sdk

import android.app.Activity

interface Tamara {
    fun init(authToken: String, apiUrl: String, notificationWebHook: String, publishKey: String, notificationToken: String, isSanbox: Boolean)
    fun paymentCheckout(checkOutUrl: String, successCallbackUrl: String, failureCallbackUrl: String, cancelCallbackUrl: String)
    fun getOrderDetail(orderId: String)
    fun getCapturePayment(jsonData: String)
    fun createOrder(orderReferenceId: String, description: String)
    fun setCountry(countryCode: String, currency: String)
    fun setPaymentType(paymentType: String)
    fun setInstalments(instalments: Int)
    fun setPlatform(platform: String)
    fun setLocale(locale: String)
    fun setOrderNumber(orderNumber: String)
    fun setExpiresInMinutes(expiresInMinutes: Int)
    fun setRiskAssessment(jsonData: String) : Boolean
    fun setAdditionalData(jsonData: String)
    fun addCustomFieldsAdditionalData(jsonData: String)
    fun setCustomerInfo(firstName: String, lastName: String, phoneNumber: String, email: String, isFirstOrder: Boolean = true)
    fun addItem(name: String, referenceId: String, sku: String, type: String, unitPrice: Double, tax: Double, discount: Double, quantity: Int)
    fun setShippingAddress(firstName: String, lastName: String, phoneNumber: String, addressLine1: String, addressLine2: String, country: String, region: String, city: String)
    fun setBillingAddress(firstName: String, lastName: String, phoneNumber: String, addressLine1: String, addressLine2: String, country: String, region: String, city: String)
    fun setCurrency(newCurrency: String)
    fun setShippingAmount(amount: Double)
    fun setDiscount(amount: Double, name: String)
    fun paymentOrder()

    fun checkPaymentOptions(jsonData: String)
    fun refunds(orderId: String, jsonData: String)
    fun cancelOrder(orderId: String, jsonData: String)
    fun updateOrderReference(orderId: String, orderReferenceId: String)
    fun renderCartPage(language: String, country: String, publicKey: String,
                       amount: Double)
    fun renderProduct(language: String, country: String, publicKey: String,
                      amount: Double)
    fun authoriseOrder(orderId: String)
    fun clearItem()
}
