package co.tamara.sdk

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.tamara.sdk.error.*
import co.tamara.sdk.model.*
import co.tamara.sdk.model.request.CancelOrder
import co.tamara.sdk.model.request.CapturePaymentRequest
import co.tamara.sdk.model.request.PaymentRefund
import co.tamara.sdk.model.request.PaymentOptions
import co.tamara.sdk.model.request.ShippingInfo
import com.google.gson.Gson

/**
 * Creates order session in Tamara need a lot of data and this data is disposable.
 * This class help creating data step by step with minimum effort.
 * Session of an order payment need those data:
 * - Consumer: data of User who will make payment
 *
 */
class TamaraPayment {
    internal var token: String = ""
    internal var apiUrl: String = "http://api.tamara.co"
    internal var pushUrl: String = ""
    internal var publishKey: String = ""
    internal var notificationToken: String = ""
    internal var isSandbox: Boolean = true
    internal var successUrl: String = ""
    internal var failureUrl: String = ""
    internal var cancelUrl: String = ""
    internal var defaultCountryCode = "SA"
    private var order = Order()
    internal var currency: String = "SAR"
    internal var widgetSandboxUrl: String = "https://cdn-sandbox.tamara.co"
    internal var widgetLiveUrl: String = "https://cdn.tamara.co"
    private var state = STATE_NEW

    /**
     * Since TamaraPayment is singleton, call this function
     * to reset order before adding new data
     */
    private fun resetOrder(){
        order = Order(countryCode = defaultCountryCode)
        state = STATE_BEGIN
    }

    private fun buildOrder(): Order {
        validateData()
        var totalAmount = 0.0
        var totalTax = 0.0
        val shippingFee = order.shippingAmount?.amount ?: 0.0
        val discount = order.discount?.amount?.amount ?: 0.0
        order.items.forEach {
            totalAmount += it.totalAmount!!.amount
            totalTax += it.taxAmount!!.amount
        }
        order.merchantUrl = MerchantUrl(notification = pushUrl)
        order.totalAmount = Amount(totalAmount + shippingFee - discount, currency)
        order.taxAmount = Amount(totalTax, currency)
        return order
    }

    private fun validateData() {
        if(order.items.isNullOrEmpty()){
            throw InvalidItemException()
        }
        if(order.shippingAddress == null || order.billingAddress == null){
            throw InvalidAddressException()
        }
        if(order.consumer == null){
            throw InvalidCustomerInfoException()
        }
        if(order.shippingAmount == null){
            throw InvalidShippingFeeException()
        }
    }

    private fun validateDataCapture(activity: Activity, capturePayment : CapturePaymentRequest?): Boolean {
        var error = ""
        capturePayment?.let {
            if(capturePayment?.orderId.isNullOrEmpty()){
                error += "Order id is required"
            }
            if(capturePayment?.totalAmount == null){
                if (error.isNotEmpty()) {
                    error += "\n"
                }
                error += "Total Amount is required"
            }
            if(capturePayment?.shippingInfo == null){
                if (error.isNotEmpty()) {
                    error += "\n"
                }
                error += "Shipping Info is required"
            }
        }
        if (error.isNotEmpty()) {
            Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun validatePaymentOptions(
        activity: Activity,
        paymentOptions: PaymentOptions?
    ): Boolean {
        var error = ""
        paymentOptions?.let {
            if (it.country.isNullOrEmpty()) {
                error += "country is required"
            }

            if (it.orderValue == null) {
                if (error.isNotEmpty()) {
                    error += "\n"
                }
                error += "orderValue is required"
            }
        }

        if (error.isNotEmpty()) {
            Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun validateDataRefunds(orderId: String?, activity: Activity, paymentRefund : PaymentRefund?): Boolean {
        var error = ""
        if(orderId.isNullOrEmpty()){
            error += "Order id is required"
        }

        paymentRefund?.let {
            if(paymentRefund.totalAmount == null){
                error += "Total amount id is required"
            }
            if(paymentRefund.comment.isNullOrEmpty()){
                if (error.isNotEmpty()) {
                    error += "\n"
                }
                error += "Comment is required"
            }
        }

        if (error.isNotEmpty()) {
            Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun validateDataCancel(activity: Activity, cancelOrder: CancelOrder?): Boolean {
        var error = ""
        cancelOrder?.let {
            if(cancelOrder?.totalAmount == null){
                error += "totalAmount is required"
            }
        }

        if (error.isNotEmpty()) {
            Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    companion object {
        const val REQUEST_TAMARA_PAYMENT = 23314
        const val REQUEST_TAMARA_ORDER_DETAIL = 23315
        const val REQUEST_TAMARA_CAPTURE_PAYMENT = 23316
        const val REQUEST_TAMARA_REFUNDS = 23317
        const val REQUEST_TAMARA_REFERENCE = 23318
        const val REQUEST_TAMARA_CART_PAGE = 23319
        const val REQUEST_TAMARA_PRODUCT = 23320
        const val REQUEST_TAMARA_CANCEL = 23321
        const val REQUEST_TAMARA_AUTHORISE = 23322
        const val REQUEST_TAMARA_PAYMENT_OPTIONS = 23323
        private const val STATE_NEW = 0
        private const val STATE_INITIALIZED = 1
        private const val STATE_BEGIN = 2
        private const val STATE_END = 3
        private var sInstance: TamaraPayment? = null

        internal fun getInstance(): TamaraPayment {
            if (sInstance == null) {
                sInstance = TamaraPayment()
            }
            return sInstance!!
        }

        private fun validateStateForAddingData(){
            if(getInstance().state < STATE_BEGIN){
                throw InvalidState("Please call createOrder before add data to Tamara Payment")
            }
        }

        fun initialize(token: String, apiUrl: String, pushUrl: String, publishKey: String, notificationToken: String,
                       isSandbox: Boolean){
            getInstance().token = token
            getInstance().apiUrl = apiUrl
            getInstance().publishKey = publishKey
            getInstance().pushUrl = pushUrl
            getInstance().notificationToken = notificationToken
            getInstance().isSandbox = isSandbox
            getInstance().state = STATE_INITIALIZED
        }

        /**
         * Create order with reference id and description
         * @param orderReferenceId unique id on your server
         * @param description of this order
         */
        fun createOrder(orderReferenceId: String, description: String) {
            if(getInstance().state < STATE_INITIALIZED){
                throw InvalidState("Tamara Payment has not been initialed")
            }
            getInstance().resetOrder()
            getInstance().order.orderReferenceId = orderReferenceId
            getInstance().order.description = description
        }

        /**
         * Set Payment Type
         * @param paymentType
         */
        fun setCountry(
            countryCode: String,
            currency: String
        ) {
            getInstance().defaultCountryCode = countryCode
            getInstance().order.countryCode = countryCode
        }

        /**
         * Set Instalments
         * @param instalments
         */
        fun setInstalments(instalments: Int) {
            validateStateForAddingData()
            getInstance().order.instalments = instalments
        }

        /**
         * Set Platform
         * @param platform
         */
        fun setPlatform(platform: String) {
            validateStateForAddingData()
            getInstance().order.platform = platform
        }

        /**
         * Set Locale
         * @param locale
         */
        fun setLocale(locale: String) {
            validateStateForAddingData()
            getInstance().order.locale = locale
        }

        /**
         * Set orderNumber
         * @param orderNumber
         */
        fun setOrderNumber(orderNumber: String) {
            validateStateForAddingData()
            getInstance().order.orderNumber = orderNumber
        }

        /**
         * Set expiresInMinutes
         * @param expiresInMinutes
         */
        fun setExpiresInMinutes(expiresInMinutes: Int) {
            validateStateForAddingData()
            getInstance().order.expiresInMinutes = expiresInMinutes
        }

        /**
         * Set riskAssessment
         * @param jsonData
         */
        fun setRiskAssessment(jsonData: String, activity: Activity): Boolean {
            validateStateForAddingData()
            return getInstance().order.updateRiskAssessmentFromJson(jsonData.trim(), activity)
        }

        /**
         * Set AdditionalData
         * @param jsonData
         */
        fun setAdditionalData(jsonData: String) {
            validateStateForAddingData()
            getInstance().order.updateAdditionalDataFromJson(jsonData)
        }

        /**
         * Add Custom Fields AdditionalData
         * @param jsonData
         */
        fun addCustomFieldsAdditionalData(jsonData: String) {
            validateStateForAddingData()
            getInstance().order.updateAdditionalDataFromJson(jsonData)
        }

        /**
         * Set Payment Type
         * @param paymentType
         */
        fun setPaymentType(
            paymentType: String
        ) {
            validateStateForAddingData()
            getInstance().order.paymentType = paymentType
        }

        /**
         * Set Customer Information
         * @param firstName
         * @param lastName
         * @param phoneNumber
         * @param email
         * @param isFirstOrder true if this is first order of customer
         */
        fun setCustomerInfo(
            firstName: String,
            lastName: String,
            phoneNumber: String,
            email: String,
            isFirstOrder: Boolean = true
        ) {
            validateStateForAddingData()
            getInstance().order.consumer =
                Consumer(firstName, lastName, phoneNumber, email, isFirstOrder)
        }

        /**
         * Clear item
         */
        fun clearItem() {
            validateStateForAddingData()
            getInstance().order.items = arrayListOf()
        }

        /**
         * Add an Item
         * @param name
         * @param referenceId reference id
         * @param sku
         * @param unitPrice original price of item
         * @param tax tax fee for each item
         * @param discount for each item
         * @param quantity
         */
        fun addItem(
            name: String,
            referenceId: String,
            sku: String,
            type: String,
            unitPrice: Double,
            tax: Double,
            discount: Double,
            quantity: Int
        ) {
            validateStateForAddingData()
            val currency = getInstance().currency
            getInstance().order.items.add(
                Item(
                    name,
                    referenceId,
                    sku,
                    type,
                    Amount(unitPrice, currency),
                    quantity,
                    Amount(discount, currency),
                    Amount(tax, currency),
                    Amount((unitPrice + tax - discount)*quantity, currency)
                )
            )
        }

        /**
         * Set shipping address
         * @param firstName
         * @param lastName
         * @param phoneNumber
         * @param addressLine1 line 1 of address
         * @param addressLine2 line 2 of address
         * @param country The two-character ISO 3166-1 country code
         * @param region
         * @param city
         */
        fun setShippingAddress(
            firstName: String,
            lastName: String,
            phoneNumber: String,
            addressLine1: String,
            addressLine2: String,
            country: String,
            region: String,
            city: String
        ){
            validateStateForAddingData()
            getInstance().order.shippingAddress = Address(firstName, lastName, phoneNumber, addressLine1, addressLine2, country,region, city)
        }

        /**
         * Set billing address
         * @param firstName
         * @param lastName
         * @param phoneNumber
         * @param addressLine1 line 1 of address
         * @param addressLine2 line 2 of address
         * @param country The two-character ISO 3166-1 country code
         * @param region
         * @param city
         */
        fun setBillingAddress(
            firstName: String,
            lastName: String,
            phoneNumber: String,
            addressLine1: String,
            addressLine2: String,
            country: String,
            region: String,
            city: String
        ){
            validateStateForAddingData()
            getInstance().order.billingAddress = Address(firstName, lastName, phoneNumber, addressLine1, addressLine2, country,region, city)
        }

        /**
         * Update currency if needed
         * @param newCurrency Default is SAR
         */
        fun setCurrency(newCurrency: String){
            validateStateForAddingData()
            getInstance().currency = newCurrency
        }

        /**
         * Set shipping fee
         * @param amount default currency is SAR
         */
        fun setShippingAmount(amount: Double){
            validateStateForAddingData()
            var currency = getInstance().currency
            getInstance().order.shippingAmount = Amount(amount, currency)
        }

        /**
         * Set discount for this order
         * @param amount discount value
         * @param name discount campaign's name
         */
        fun setDiscount(amount: Double, name: String){
            validateStateForAddingData()
            var currency = getInstance().currency
            var discountAmount = Amount(amount, currency)
            var discount = Discount(discountAmount, name)
            getInstance().order.discount = discount
        }

        /**
         * End session, request to create order again
         */
        fun endSession() {
            getInstance().state = STATE_END
        }

        /**
         * Start Tamara Payment Activity
         * @param activity Activity which will receive callback data form SDK
         */
        fun startPayment(activity: Activity){
            TamaraPaymentActivity.start(activity, getInstance().buildOrder(), REQUEST_TAMARA_PAYMENT)
        }

        /**
         * Start Tamara Payment Activity
         * @param activity Activity which will receive callback data form SDK
         */
        fun startPayment(activity: Activity, checkOutUrl: String,
                         successCallbackUrl: String, failureCallbackUrl: String,
                         cancelCallbackUrl: String){
            getInstance().state = STATE_INITIALIZED
            getInstance().successUrl = successCallbackUrl
            getInstance().failureUrl = failureCallbackUrl
            getInstance().cancelUrl = cancelCallbackUrl
            TamaraPaymentActivity.start(activity, checkOutUrl, REQUEST_TAMARA_PAYMENT)
        }

        /**
         * Start Tamara Payment Activity
         * @param activity Activity which will receive callback data form SDK
         */
        fun startPayment(fragment: Fragment){
            TamaraPaymentActivity.start(fragment, getInstance().buildOrder(), REQUEST_TAMARA_PAYMENT)
        }

        /**
         * Start Tamara Payment Activity
         * @param activity Activity which will receive callback data form SDK
         */
        fun startPayment(fragment: Fragment, checkOutUrl: String, successCallbackUrl: String, failureCallbackUrl: String, cancelCallbackUrl: String){
            getInstance().state = STATE_INITIALIZED
            getInstance().successUrl = successCallbackUrl
            getInstance().failureUrl = failureCallbackUrl
            getInstance().cancelUrl = cancelCallbackUrl
            TamaraPaymentActivity.start(fragment, checkOutUrl, REQUEST_TAMARA_PAYMENT)
        }

        /**
         * Get order detail
         * @param activity Activity which will receive callback data form SDK
         * @param orderId order id value
         */
        fun getOrderDetail(activity: Activity, orderId: String){
            TamaraInformationActivity.getOrderDetail(activity, orderId, REQUEST_TAMARA_ORDER_DETAIL)
        }

        /**
         * Get order detail
         * @param fragment Fragment which will receive callback data form SDK
         * @param orderId order id value
         */
        fun getOrderDetail(fragment: Fragment, orderId: String){
            TamaraInformationActivity.getOrderDetail(fragment, orderId, REQUEST_TAMARA_ORDER_DETAIL)
        }

        /**
         * Get capture payment
         * @param activity Activity which will receive callback data form SDK
         */
        fun getCapturePayment(activity: Activity, jsonData: String){
            val capturePayment = Gson().fromJson(jsonData, CapturePaymentRequest::class.java)
            val validateResult = getInstance().validateDataCapture(activity, capturePayment)
            if (validateResult) {
                TamaraInformationActivity.getCapturePayment(activity, capturePayment, REQUEST_TAMARA_CAPTURE_PAYMENT)
            }
        }

        /**
         * Get capture payment
         * @param fragment Fragment which will receive callback data form SDK
         */
        fun getCapturePayment(fragment: Fragment, jsonData: String){
            val capturePayment = Gson().fromJson(jsonData, CapturePaymentRequest::class.java)
            val validateResult = getInstance().validateDataCapture(fragment.requireActivity(), capturePayment)
            if (validateResult) {
                TamaraInformationActivity.getCapturePayment(fragment, capturePayment, REQUEST_TAMARA_CAPTURE_PAYMENT)
            }
        }

        /**
         * refunds
         * @param activity Activity which will receive callback data form SDK
         */
        fun refunds(activity: Activity, orderId: String, jsonData: String){
            val paymentRefund = Gson().fromJson(jsonData, PaymentRefund::class.java)
            val validateResult = getInstance().validateDataRefunds(orderId, activity, paymentRefund)
            if (validateResult) {
                TamaraInformationActivity.refunds(activity, orderId, paymentRefund, REQUEST_TAMARA_REFUNDS)
            }
        }

        /**
         * refunds
         * @param fragment Fragment which will receive callback data form SDK
         */
        fun refunds(fragment: Fragment, orderId: String, jsonData: String){
            val paymentRefund = Gson().fromJson(jsonData, PaymentRefund::class.java)
            val validateResult = getInstance().validateDataRefunds(orderId, fragment.requireActivity(), paymentRefund)
            if (validateResult) {
                TamaraInformationActivity.refunds(fragment, orderId, paymentRefund, REQUEST_TAMARA_REFUNDS)
            }
        }

        /**
         * refunds
         * @param activity Activity which will receive callback data form SDK
         */
        fun cancelOrder(activity: Activity, orderId: String, jsonData: String){
            val cancelOrder = Gson().fromJson(jsonData, CancelOrder::class.java)
            val validateResult = getInstance().validateDataCancel(activity, cancelOrder)
            if (validateResult) {
                TamaraInformationActivity.cancelOrder(activity, orderId, cancelOrder, REQUEST_TAMARA_CANCEL)
            }
        }

        /**
         * refunds
         * @param fragment Fragment which will receive callback data form SDK
         */
        fun cancelOrder(fragment: Fragment, orderId: String, jsonData: String){
            val cancelOrder = Gson().fromJson(jsonData, CancelOrder::class.java)
            val validateResult = getInstance().validateDataCancel(fragment.requireActivity(), cancelOrder)
            if (validateResult) {
                TamaraInformationActivity.cancelOrder(fragment, orderId, cancelOrder, REQUEST_TAMARA_CANCEL)
            }
        }

        /**
         * update order reference
         * @param activity Activity which will receive callback data form SDK
         * @param orderId order id value
         * @param orderReferenceId order reference id value
         */
        fun updateOrderReference(activity: Activity, orderId: String, orderReferenceId: String){
            TamaraInformationActivity.updateOrderReference(activity, orderId, orderReferenceId, REQUEST_TAMARA_REFERENCE)
        }

        /**
         * update order reference
         * @param fragment Fragment which will receive callback data form SDK
         * @param orderId order id value
         * @param orderReferenceId order reference id value
         */
        fun updateOrderReference(fragment: Fragment, orderId: String, orderReferenceId: String){
            TamaraInformationActivity.updateOrderReference(fragment, orderId, orderReferenceId, REQUEST_TAMARA_REFERENCE)
        }

        /**
         * cart page
         * @param activity Activity which will receive callback data form SDK
         * @param language language value
         * @param country country id value
         * @param publicKey publicKey id value
         * @param amount amount id value
         */
        fun renderWidgetCartPage(activity: Activity, language: String, country: String, publicKey: String,
                                 amount: Double){
            TamaraWidgetActivity.renderWidgetCartPage(activity, language, country, publicKey, amount, REQUEST_TAMARA_CART_PAGE)
        }

        /**
         * cart page
         * @param fragment Fragment which will receive callback data form SDK
         * @param language language value
         * @param country country id value
         * @param publicKey publicKey id value
         * @param amount amount id value
         */
        fun renderWidgetCartPage(fragment: Fragment, language: String, country: String, publicKey: String,
                                 amount: Double){
            TamaraWidgetActivity.renderWidgetCartPage(fragment, language, country, publicKey, amount, REQUEST_TAMARA_CART_PAGE)
        }

        /**
         * product
         * @param activity Activity which will receive callback data form SDK
         * @param language language value
         * @param country country id value
         * @param publicKey publicKey id value
         * @param amount amount id value
         */
        fun renderWidgetProduct(activity: Activity, language: String, country: String, publicKey: String,
                                 amount: Double){
            TamaraWidgetActivity.renderWidgetProduct(activity, language, country, publicKey, amount, REQUEST_TAMARA_PRODUCT)
        }

        /**
         * product
         * @param fragment Fragment which will receive callback data form SDK
         * @param language language value
         * @param country country id value
         * @param publicKey publicKey id value
         * @param amount amount id value
         */
        fun renderWidgetProduct(fragment: Fragment, language: String, country: String, publicKey: String,
                                 amount: Double){
            TamaraWidgetActivity.renderWidgetProduct(fragment, language, country, publicKey, amount, REQUEST_TAMARA_PRODUCT)
        }

        /**
         * Authorise order
         * @param activity Activity which will receive callback data form SDK
         * @param orderId order id value
         */
        fun authoriseOrder(activity: Activity, orderId: String){
            TamaraInformationActivity.authoriseOrder(activity, orderId, REQUEST_TAMARA_AUTHORISE)
        }

        /**
         * Authorise order
         * @param fragment Fragment which will receive callback data form SDK
         * @param orderId order id value
         */
        fun authoriseOrder(fragment: Fragment, orderId: String){
            TamaraInformationActivity.authoriseOrder(fragment, orderId, REQUEST_TAMARA_AUTHORISE)
        }

        /**
         * paymentOptions
         * @param activity Activity which will receive callback data form SDK
         */
        fun checkPaymentOptions(activity: Activity, jsonData: String) {
            val paymentOptions = Gson().fromJson(jsonData, PaymentOptions::class.java)
            val validateResult = getInstance().validatePaymentOptions(activity, paymentOptions)
            if (validateResult) {
                TamaraInformationActivity.paymentOptions(
                    activity,
                    paymentOptions,
                    REQUEST_TAMARA_PAYMENT_OPTIONS
                )
            }
        }

        /**
         * paymentOptions
         * @param fragment Fragment which will receive callback data form SDK
         */
        fun checkPaymentOptions(fragment: Fragment, jsonData: String) {
            val paymentOptions = Gson().fromJson(jsonData, PaymentOptions::class.java)
            val validateResult = getInstance().validatePaymentOptions(fragment.requireActivity(), paymentOptions)
            if (validateResult) {
                TamaraInformationActivity.paymentOptions(
                    fragment,
                    paymentOptions,
                    REQUEST_TAMARA_PAYMENT_OPTIONS
                )
            }
        }
    }
}