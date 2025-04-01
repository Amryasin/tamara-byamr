package co.tamara.sdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

class TamaraPlugin: FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener, Tamara {
    private val CHANNEL = "co.tamara.sdk"
    private lateinit var _result: MethodChannel.Result
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    var activity: Activity? = null

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, CHANNEL)
        channel.setMethodCallHandler(this)
        context = binding.applicationContext
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "initSdk" -> {
                val authToken : String? = call.argument("authToken")
                val apiUrl : String? = call.argument("apiUrl")
                val notificationWebHook : String? = call.argument("notificationWebHook")
                val publishKey : String? = call.argument("publishKey")
                val notificationToken : String? = call.argument("notificationToken")
                val isSanbox: Boolean? = call.argument("isSandbox")
                init(authToken = authToken!!, apiUrl = apiUrl!!, notificationWebHook = notificationWebHook!!,
                    publishKey = publishKey!!, notificationToken = notificationToken!!, isSanbox = isSanbox!!)
            }
            "startPayment" -> {
                val checkoutUrl : String? = call.argument("checkoutUrl")
                val successCallbackUrl : String? = call.argument("successCallbackUrl")
                val failureCallbackUrl : String? = call.argument("failureCallbackUrl")
                val cancelCallbackUrl : String? = call.argument("cancelCallbackUrl")
                paymentCheckout(checkoutUrl!!, successCallbackUrl!!, failureCallbackUrl!!, cancelCallbackUrl!!)
            }
            "orderDetail" -> {
                _result = result
                val orderId : String? = call.argument("orderId")
                getOrderDetail(orderId!!)
            }
            "capturePayment" -> {
                _result = result
                val jsonData : String? = call.argument("capturePayment")
                getCapturePayment(jsonData!!)
            }
            "createOrder" -> {
                val orderReferenceId : String? = call.argument("orderReferenceId")
                val description : String? = call.argument("description")
                createOrder(orderReferenceId+"", description+"")
            }
            "setCountry" -> {
                val countryCode : String? = call.argument("countryCode")
                val currency : String? = call.argument("currency")
                setCountry(countryCode!!, currency!!)
            }
            "setPaymentType" -> {
                val paymentType : String? = call.argument("paymentType")
                setPaymentType(paymentType!!)
            }
            "setPlatform" -> {
                val platform : String? = call.argument("platfm")
                setPlatform(platform!!)
            }
            "setInstalments" -> {
                val instalments : Int? = call.argument("instalments")
                setInstalments(instalments!!)
            }
            "setLocale" -> {
                val locale : String? = call.argument("locale")
                setLocale(locale!!)
            }
            "setOrderNumber" -> {
                val orderNumber : String? = call.argument("orderNumber")
                setOrderNumber(orderNumber!!)
            }
            "setExpiresInMinutes" -> {
                val expiresInMinutes : Int? = call.argument("expiresInMinutes")
                setExpiresInMinutes(expiresInMinutes!!)
            }
            "setRiskAssessment" -> {
                val jsonData : String? = call.argument("jsonData")
                val risk = setRiskAssessment(jsonData!!)
                result.success(risk)
            }
            "setAdditionalData" -> {
                val jsonData : String? = call.argument("jsonData")
                setAdditionalData(jsonData!!)
            }
            "addCustomFieldsAdditionalData" -> {
                val jsonData : String? = call.argument("jsonData")
                addCustomFieldsAdditionalData(jsonData!!)
            }
            "setCustomerInfo" -> {
                val firstName : String? = call.argument("firstName")
                val lastName : String? = call.argument("lastName")
                val phoneNumber : String? = call.argument("phoneNumber")
                val email : String? = call.argument("email")
                val isFirstOrder : Boolean? = call.argument("isFirstOrder")
                setCustomerInfo(firstName!!, lastName!!, phoneNumber!!, email!!, isFirstOrder!!)
            }
            "addItem" -> {
                val name : String? = call.argument("name")
                val referenceId : String? = call.argument("referenceId")
                val sku : String? = call.argument("sku")
                val type : String? = call.argument("type")
                val unitPrice : Double? = call.argument("unitPrice")
                val tax : Double? = call.argument("tax")
                val discount : Double? = call.argument("discount")
                val quantity : Int? = call.argument("quantity")
                addItem(name!!, referenceId!!, sku!!, type!!, unitPrice!!, tax!!, discount!!, quantity!!)
            }
            "setShippingAddress" -> {
                val firstName : String? = call.argument("firstName")
                val lastName : String? = call.argument("lastName")
                val phoneNumber : String? = call.argument("phoneNumber")
                val addressLine1 : String? = call.argument("addressLine1")
                val addressLine2 : String? = call.argument("addressLine2")
                val country : String? = call.argument("country")
                val region : String? = call.argument("region")
                val city : String? = call.argument("city")
                setShippingAddress(firstName!!, lastName!!, phoneNumber!!, addressLine1!!, addressLine2!!, country!!, region!!, city!!)
            }
            "setBillingAddress" -> {
                val firstName : String? = call.argument("firstName")
                val lastName : String? = call.argument("lastName")
                val phoneNumber : String? = call.argument("phoneNumber")
                val addressLine1 : String? = call.argument("addressLine1")
                val addressLine2 : String? = call.argument("addressLine2")
                val country : String? = call.argument("country")
                val region : String? = call.argument("region")
                val city : String? = call.argument("city")
                setBillingAddress(firstName!!, lastName!!, phoneNumber!!, addressLine1!!, addressLine2!!, country!!, region!!, city!!)
            }
            "setCurrency" -> {
                val newCurrency : String? = call.argument("newCurrency")
                setCurrency(newCurrency!!)
            }
            "setShippingAmount" -> {
                val amount : Double? = call.argument("amount")
                setShippingAmount(amount!!)
            }
            "setDiscount" -> {
                val amount : Double? = call.argument("amount")
                val name : String? = call.argument("name")
                setDiscount(amount!!, name!!)
            }
            "paymentOrder" -> {
                _result = result
                paymentOrder()
            }
            "checkPaymentOptions" -> {
                _result = result
                val jsonData : String? = call.argument("checkPaymentOptions")
                checkPaymentOptions(jsonData!!)
            }
            "refunds" -> {
                _result = result
                val orderId : String? = call.argument("orderId")
                val jsonData : String? = call.argument("refunds")
                refunds(orderId!!, jsonData!!)
            }
            "cancelOrder" -> {
                _result = result
                val orderId : String? = call.argument("orderId")
                val jsonData : String? = call.argument("cancelOrder")
                cancelOrder(orderId!!, jsonData!!)
            }
            "updateOrderReference" -> {
                _result = result
                val orderId : String? = call.argument("orderId")
                val orderReferenceId : String? = call.argument("orderReferenceId")
                updateOrderReference(orderId!!, orderReferenceId!!)
            }
            "renderCartPage" -> {
                _result = result
                val language : String? = call.argument("language")
                val country : String? = call.argument("country")
                val publicKey : String? = call.argument("publicKey")
                val amount : Double? = call.argument("amount")
                renderCartPage(language!!, country!!, publicKey!!, amount!!)
            }
            "renderProduct" -> {
                _result = result
                val language : String? = call.argument("language")
                val country : String? = call.argument("country")
                val publicKey : String? = call.argument("publicKey")
                val amount : Double? = call.argument("amount")
                renderProduct(language!!, country!!, publicKey!!, amount!!)
            }
            "authoriseOrder" -> {
                _result = result
                val orderId : String? = call.argument("orderId")
                authoriseOrder(orderId!!)
            }
            "clearItem" -> {
                clearItem()
            }
            else -> {
            }
        }
    }

    override fun init(
        authToken: String,
        apiUrl: String,
        notificationWebHook: String,
        publishKey: String,
        notificationToken: String,
        isSanbox: Boolean
    ) {
        TamaraPayment.initialize(authToken, apiUrl, notificationWebHook, publishKey, notificationToken, isSanbox)
    }

    override fun paymentCheckout(
        checkOutUrl: String,
        successCallbackUrl: String,
        failureCallbackUrl: String,
        cancelCallbackUrl: String
    ) {
        TamaraPayment.startPayment(activity!!, checkOutUrl, successCallbackUrl, failureCallbackUrl, cancelCallbackUrl)
    }

    override fun getOrderDetail(orderId: String) {
        TamaraPayment.getOrderDetail(activity!!, orderId)
    }

    override fun getCapturePayment(jsonData: String) {
        TamaraPayment.getCapturePayment(activity!!, jsonData)
    }

    override fun createOrder(orderReferenceId: String, description: String) {
        TamaraPayment.createOrder(orderReferenceId, description)
    }

    override fun setCountry(countryCode: String, currency: String) {
        TamaraPayment.setCountry(countryCode, currency)
    }

    override fun setPaymentType(paymentType: String) {
        TamaraPayment.setPaymentType(paymentType)
    }
    override fun setPlatform(platform: String) {
        TamaraPayment.setPlatform(platform)
    }
    override fun setInstalments(instalments: Int) {
        TamaraPayment.setInstalments(instalments)
    }

    override fun setLocale(locale: String) {
        TamaraPayment.setLocale(locale)
    }

    override fun setOrderNumber(orderNumber: String) {
        TamaraPayment.setOrderNumber(orderNumber)
    }

    override fun setExpiresInMinutes(expiresInMinutes: Int) {
        TamaraPayment.setExpiresInMinutes(expiresInMinutes)
    }

    override fun setRiskAssessment(jsonData: String): Boolean {
        return TamaraPayment.setRiskAssessment(jsonData, activity!!)
    }

    override fun setAdditionalData(jsonData: String) {
        TamaraPayment.setAdditionalData(jsonData)
    }

    override fun addCustomFieldsAdditionalData(jsonData: String) {
        TamaraPayment.addCustomFieldsAdditionalData(jsonData)
    }

    override fun setCustomerInfo(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        isFirstOrder: Boolean
    ) {
        TamaraPayment.setCustomerInfo(firstName, lastName, phoneNumber, email, isFirstOrder)
    }

    override fun addItem(
        name: String,
        referenceId: String,
        sku: String,
        type: String,
        unitPrice: Double,
        tax: Double,
        discount: Double,
        quantity: Int
    ) {
        TamaraPayment.addItem(name, referenceId, sku, type, unitPrice, tax, discount, quantity)
    }

    override fun setShippingAddress(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        addressLine1: String,
        addressLine2: String,
        country: String,
        region: String,
        city: String
    ) {
        TamaraPayment.setShippingAddress(firstName, lastName, phoneNumber, addressLine1, addressLine2, country, region, city)
    }

    override fun setBillingAddress(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        addressLine1: String,
        addressLine2: String,
        country: String,
        region: String,
        city: String
    ) {
        TamaraPayment.setBillingAddress(firstName, lastName, phoneNumber, addressLine1, addressLine2, country, region, city)
    }

    override fun setCurrency(newCurrency: String) {
        TamaraPayment.setCurrency(newCurrency)
    }

    override fun setShippingAmount(amount: Double) {
        TamaraPayment.setShippingAmount(amount)
    }

    override fun setDiscount(amount: Double, name: String) {
        TamaraPayment.setDiscount(amount, name)
    }

    override fun paymentOrder() {
        TamaraPayment.startPayment(activity!!)
    }

    override fun refunds(orderId: String, jsonData: String) {
        TamaraPayment.refunds(activity!!, orderId, jsonData)
    }

    override fun checkPaymentOptions(jsonData: String) {
        TamaraPayment.checkPaymentOptions(activity!!, jsonData)
    }

    override fun cancelOrder(orderId: String, jsonData: String) {
        TamaraPayment.cancelOrder(activity!!, orderId, jsonData)
    }

    override fun updateOrderReference(orderId: String, orderReferenceId: String) {
        TamaraPayment.updateOrderReference(activity!!, orderId, orderReferenceId)
    }

    override fun renderCartPage(
        language: String,
        country: String,
        publicKey: String,
        amount: Double
    ) {
        TamaraPayment.renderWidgetCartPage(activity!!, language, country, publicKey, amount)
    }

    override fun renderProduct(
        language: String,
        country: String,
        publicKey: String,
        amount: Double
    ) {
        TamaraPayment.renderWidgetProduct(activity!!, language, country, publicKey, amount)
    }

    override fun authoriseOrder(orderId: String) {
        TamaraPayment.authoriseOrder(activity!!, orderId)
    }

    override fun clearItem() {
        TamaraPayment.clearItem()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        when(requestCode){
            TamaraPayment.REQUEST_TAMARA_PAYMENT -> {
                if(TamaraPaymentHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraPaymentHelper.getData(data!!)
                    when(result?.status){
                        PaymentResult.STATUS_CANCEL ->{
                            _result.success("Payment canceled")
                            Toast.makeText(activity, "Payment canceled", Toast.LENGTH_LONG).show()
                        }
                        PaymentResult.STATUS_FAILURE -> {
                            _result.success(result.getMessage() ?: "Payment error")
                            Toast.makeText(activity, result.getMessage() ?: "Payment error", Toast.LENGTH_LONG).show()
                        }
                        PaymentResult.STATUS_SUCCESS -> {
                            val checkout = TamaraPaymentHelper.checkOutSession(data);
                            _result.success(Gson().toJson(checkout))
                        }
                    }
                }
            }
            TamaraPayment.REQUEST_TAMARA_ORDER_DETAIL -> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Order Detail error", Toast.LENGTH_LONG).show()
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val orderDetail = TamaraInformationHelper.getDataOrderDetail(data)
                            _result.success(Gson().toJson(orderDetail))
                        }
                    }
                }
            }

            TamaraPayment.REQUEST_TAMARA_CAPTURE_PAYMENT-> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Capture Payment error", Toast.LENGTH_LONG).show()
                            _result.success(result.getMessage() ?: "Capture Payment error")
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val capturePayment = TamaraInformationHelper.getDataCapturePayment(data)
                            _result.success(Gson().toJson(capturePayment))
                        }
                    }
                }
            }
            TamaraPayment.REQUEST_TAMARA_REFUNDS-> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Refunds error", Toast.LENGTH_LONG).show()
                            _result.success(result.getMessage() ?: "Refunds error")
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val refunds = TamaraInformationHelper.getDataRefunds(data)
                            _result.success(Gson().toJson(refunds))
                        }
                    }
                }
            }
            TamaraPayment.REQUEST_TAMARA_REFERENCE-> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Order Reference error", Toast.LENGTH_LONG).show()
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val orderReference = TamaraInformationHelper.getOrderReference(data)
                            _result.success(Gson().toJson(orderReference))
                        }
                    }
                }
            }

            TamaraPayment.REQUEST_TAMARA_CART_PAGE-> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Cart page error", Toast.LENGTH_LONG).show()
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val cartPage = TamaraInformationHelper.getCartPage(data)
                            _result.success(Gson().toJson(cartPage))
                        }
                    }
                }
            }

            TamaraPayment.REQUEST_TAMARA_PRODUCT-> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Product error", Toast.LENGTH_LONG).show()
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val product = TamaraInformationHelper.getProduct(data)
                            _result.success(Gson().toJson(product))
                        }
                    }
                }
            }

            TamaraPayment.REQUEST_TAMARA_CANCEL-> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Cancel order error", Toast.LENGTH_LONG).show()
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val cancelOrder = TamaraInformationHelper.getCancelOrder(data)
                            _result.success(Gson().toJson(cancelOrder))
                        }
                    }
                }
            }
            TamaraPayment.REQUEST_TAMARA_AUTHORISE-> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Authorise order error", Toast.LENGTH_LONG).show()
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val authorise = TamaraInformationHelper.getAuthoriseOrder(data)
                            Handler(Looper.getMainLooper()).post {
                                _result.success(Gson().toJson(authorise))
                            }
                        }
                    }
                }
            }

            TamaraPayment.REQUEST_TAMARA_PAYMENT_OPTIONS-> {
                if(TamaraInformationHelper.shouldHandleActivityResult(requestCode, resultCode, data)){
                    var result = TamaraInformationHelper.getData(data!!)
                    when(result?.status){
                        InformationResult.STATUS_FAILURE -> {
                            Toast.makeText(activity, result.getMessage() ?: "Check payment availability error", Toast.LENGTH_LONG).show()
                        }
                        InformationResult.STATUS_SUCCESS -> {
                            val paymentOptions = TamaraInformationHelper.getPaymentOptions(data)
                            _result.success(Gson().toJson(paymentOptions))
                        }
                    }
                }
            }
        }

        return false
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        binding.addActivityResultListener(this)
        activity = binding.getActivity()
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.getActivity()
    }

    override fun onDetachedFromActivity() {
        activity = null
    }
}