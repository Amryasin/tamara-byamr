import UIKit
import Flutter
import Foundation

public class TamaraPlugin: NSObject, FlutterPlugin {
   public static func register(with registrar: FlutterPluginRegistrar) {
       let channel = FlutterMethodChannel(name: "co.tamara.sdk/ios", binaryMessenger: registrar.messenger())
       let instance = TamaraPlugin()
       registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
            switch call.method {
            case "initSdk":
                guard let args = call.arguments as? [String : Any] else {return}
                let authToken = args["authToken"] as! String
                let apiUrl = args["apiUrl"] as! String
                let notificationWebHook = args["notificationWebHook"] as! String
                let publishKey = args["publishKey"] as! String
                let notificationToken = args["notificationToken"] as! String
                let isSandbox = args["isSandbox"] as! Bool
                self.initialize(token: authToken, apiUrl: apiUrl, pushUrl: notificationWebHook, publishKey: publishKey, notificationToken: notificationToken, isSandbox: isSandbox)
                print("number: 0") // In ra number: 0
            case "createOrder":
                guard let args = call.arguments as? [String : Any] else {return}
                let orderReferenceId = args["orderReferenceId"] as! String
                let description = args["description"] as! String
                self.createOrder(orderReferenceId: orderReferenceId, description: description)
            case "setCountry":
                guard let args = call.arguments as? [String : Any] else {return}
                let countryCode = args["countryCode"] as! String
                let currency = args["currency"] as! String
                self.setCountry(countryCode: countryCode, currency: currency)
            case "setPaymentType" :
                guard let args = call.arguments as? [String : Any] else {return}
                let paymentType = args["paymentType"] as! String
                self.setPaymentType(paymentType: paymentType)
            case "setPlatform" :
                guard let args = call.arguments as? [String : Any] else {return}
                let platform = args["platfm"] as! String
                self.setPlatform(platform: platform)
            case "setInstalments" :
                guard let args = call.arguments as? [String : Any] else {return}
                let instalments = args["instalments"] as! Int
                self.setInstalments(instalments: instalments)
            case "setLocale" :
                guard let args = call.arguments as? [String : Any] else {return}
                let locale = args["locale"] as! String
                self.setLocale(locale: locale)
            case "setOrderNumber" :
                guard let args = call.arguments as? [String : Any] else {return}
                let orderNumber = args["orderNumber"] as! String
                self.setOrderNumber(orderNumber: orderNumber)
            case "setExpiresInMinutes" :
                guard let args = call.arguments as? [String : Any] else {return}
                let expiresInMinutes = args["expiresInMinutes"] as! Int
                self.setExpiresInMinutes(expiresInMinutes: expiresInMinutes)
            case "setRiskAssessment" :
                guard let args = call.arguments as? [String : Any] else {return}
                let jsonData = args["jsonData"] as! String
                let risk = self.setRiskAssessment(jsonData: jsonData)
                result(risk)
            case "setAdditionalData" :
                guard let args = call.arguments as? [String : Any] else {return}
                let jsonData = args["jsonData"] as! String
                self.setAdditionalData(jsonData: jsonData)
            case "addCustomFieldsAdditionalData" :
                guard let args = call.arguments as? [String : Any] else {return}
                let jsonData = args["jsonData"] as! String
                self.addCustomFieldsAdditionalData(jsonData: jsonData)
            case "setCustomerInfo":
                guard let args = call.arguments as? [String : Any] else {return}
                let firstName = args["firstName"] as! String
                let lastName = args["lastName"] as! String
                let phoneNumber = args["phoneNumber"] as! String
                let email = args["email"] as! String
                let isFirstOrder = args["isFirstOrder"] as! Bool
                self.setCustomerInfo(firstName: firstName, lastName: lastName, phoneNumber: phoneNumber, email: email, isFirstOrder: isFirstOrder)
            case "addItem" :
                guard let args = call.arguments as? [String : Any] else {return}
                let name = args["name"] as! String
                let referenceId = args["referenceId"] as! String
                let sku = args["sku"] as! String
                let type = args["type"] as! String
                let unitPrice = args["unitPrice"] as! Double
                let tax = args["tax"] as! Double
                let discount = args["discount"] as! Double
                let quantity = args["quantity"] as! Int
                self.addItem(name: name, referenceId: referenceId, sku: sku, type: type, unitPrice: unitPrice, tax: tax, discount: discount, quantity: quantity)
            case "setShippingAddress" :
                guard let args = call.arguments as? [String : Any] else {return}
                let firstName = args["firstName"] as! String
                let lastName = args["lastName"] as! String
                let phoneNumber = args["phoneNumber"] as! String
                let addressLine1 = args["addressLine1"] as! String
                let addressLine2 = args["addressLine2"] as! String
                let country = args["country"] as! String
                let region = args["region"] as! String
                let city = args["city"] as! String
                self.setShippingAddress(firstName: firstName, lastName: lastName, phoneNumber: phoneNumber, addressLine1: addressLine1, addressLine2: addressLine2, country: country, region: region, city: city)
            case "setBillingAddress" :
                guard let args = call.arguments as? [String : Any] else {return}
                let firstName = args["firstName"] as! String
                let lastName = args["lastName"] as! String
                let phoneNumber = args["phoneNumber"] as! String
                let addressLine1 = args["addressLine1"] as! String
                let addressLine2 = args["addressLine2"] as! String
                let country = args["country"] as! String
                let region = args["region"] as! String
                let city = args["city"] as! String
                self.setBillingAddress(firstName: firstName, lastName: lastName, phoneNumber: phoneNumber, addressLine1: addressLine1, addressLine2: addressLine2, country: country, region: region, city: city)
            case "setCurrency" :
                guard let args = call.arguments as? [String : Any] else {return}
                let newCurrency = args["newCurrency"] as! String
                self.setCurrency(newCurrency: newCurrency)
            case "setShippingAmount" :
                guard let args = call.arguments as? [String : Any] else {return}
                let amount = args["amount"] as! Double
                self.setShippingAmount(amount: amount)
            case "setDiscount" :
                guard let args = call.arguments as? [String : Any] else {return}
                let amount = args["amount"] as! Double
                let name = args["name"] as! String
                self.setDiscount(amount: amount, name: name)
            case "paymentOrder" :
                self.paymentOrder(fResult: result)
            case "clearItem" :
                self.clearItem()
            case "authoriseOrder" :
                guard let args = call.arguments as? [String : Any] else {return}
                let orderId = args["orderId"] as! String
                self.authoriseOrder(orderId: orderId, fResult: result)
            case "checkPaymentOptions" :
                guard let args = call.arguments as? [String : Any] else {return}
                let jsonData = args["checkPaymentOptions"] as! String
                self.checkPaymentOptions(jsonData: jsonData, fResult: result)
            case "refunds" :
                guard let args = call.arguments as? [String : Any] else {return}
                let orderId = args["orderId"] as! String
                let jsonData = args["refunds"] as! String
                self.refunds(orderId: orderId, jsonData: jsonData, fResult: result)
            case "capturePayment" :
                guard let args = call.arguments as? [String : Any] else {return}
                let jsonData = args["capturePayment"] as! String
                self.capturePayment(jsonData: jsonData, fResult: result)
            case "orderDetail" :
                guard let args = call.arguments as? [String : Any] else {return}
                let orderId = args["orderId"] as! String
                self.orderDetail(orderId: orderId, fResult: result)
            case "cancelOrder" :
                guard let args = call.arguments as? [String : Any] else {return}
                let orderId = args["orderId"] as! String
                let jsonData = args["cancelOrder"] as! String
                self.cancelOrder(orderId: orderId, jsonData: jsonData, fResult: result)
            case "updateOrderReference" :
                guard let args = call.arguments as? [String : Any] else {return}
                let orderId = args["orderId"] as! String
                let orderReferenceId = args["orderReferenceId"] as! String
                self.updateOrderReference(orderId: orderId, orderReferenceId: orderReferenceId, fResult: result)
            case "renderCartPage" :
                guard let args = call.arguments as? [String : Any] else {return}
                let language = args["language"] as! String
                let country = args["country"] as! String
                let publicKey = args["publicKey"] as! String
                let amount = args["amount"] as! Double
                self.renderWidgetCartPage(language: language, country: country, publicKey: publicKey, amount: amount, result: result)
            case "renderProduct" :
                guard let args = call.arguments as? [String : Any] else {return}
                let language = args["language"] as! String
                let country = args["country"] as! String
                let publicKey = args["publicKey"] as! String
                let amount = args["amount"] as! Double
                self.renderWidgetProduct(language: language, country: country, publicKey: publicKey, amount: amount, result: result)
            default:
                result(FlutterMethodNotImplemented)
            }
    }
    
    func initialize(token: String, apiUrl: String, pushUrl: String, publishKey: String, notificationToken: String, isSandbox: Bool) {
        TamaraSDKPayment.shared.initialize(token: token, apiUrl: apiUrl, pushUrl: pushUrl, publishKey: publishKey, notificationToken: notificationToken, isSandbox: isSandbox)
    }
    
    func createOrder(orderReferenceId: String, description: String) {
        TamaraSDKPayment.shared.createOrder(orderReferenceId: orderReferenceId, description: description)
    }
    
    func setCountry(countryCode: String, currency: String) {
        TamaraSDKPayment.shared.setCountry(countryCode: countryCode, currency: currency)
    }

    func setPaymentType(paymentType: String) {
            TamaraSDKPayment.shared.setPaymentType(paymentType: paymentType)
    }

    func setPlatform(platform: String) {
        TamaraSDKPayment.shared.setPlatform(platform: platform)
    }

    func setInstalments(instalments: Int) {
        TamaraSDKPayment.shared.setInstalments(instalments: instalments)
    }

    func setLocale(locale: String) {
        TamaraSDKPayment.shared.setLocale(locale: locale)
    }

    func setOrderNumber(orderNumber: String) {
        TamaraSDKPayment.shared.setOrderNumber(orderNumber: orderNumber)
    }

    func setExpiresInMinutes(expiresInMinutes: Int) {
        TamaraSDKPayment.shared.setExpiresInMinutes(expiresInMinutes: expiresInMinutes)
    }

    func setRiskAssessment(jsonData: String)-> Bool {
        return TamaraSDKPayment.shared.setRiskAssessment(jsonData: jsonData)
    }

    func setAdditionalData(jsonData: String) {
        TamaraSDKPayment.shared.setAdditionalData(jsonData: jsonData)
    }
    
    func addCustomFieldsAdditionalData(jsonData: String) {
        TamaraSDKPayment.shared.addCustomFieldsAdditionalData(jsonData: jsonData)
    }

    func setCustomerInfo(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        isFirstOrder: Bool
    ) {
        TamaraSDKPayment.shared.setCustomerInfo(firstName: firstName, lastName: lastName, phoneNumber: phoneNumber, email: email, isFirstOrder: isFirstOrder)
    }
    
    func addItem(
        name: String,
        referenceId: String,
        sku: String,
        type: String,
        unitPrice: Double,
        tax: Double,
        discount: Double,
        quantity: Int
    ) {
        TamaraSDKPayment.shared.addItem(name: name, referenceId: referenceId, sku: sku, type: type, unitPrice: unitPrice, tax: tax, discount: discount, quantity: quantity)
    }
    
    func setShippingAddress(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        addressLine1: String,
        addressLine2: String,
        country: String,
        region: String,
        city: String
    ) {
        TamaraSDKPayment.shared.setShippingAddress(firstName: firstName, lastName: lastName, phoneNumber: phoneNumber, addressLine1: addressLine1, addressLine2: addressLine2, country: country, region: region, city: city)
    }
    
    func setBillingAddress(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        addressLine1: String,
        addressLine2: String,
        country: String,
        region: String,
        city: String
    ) {
        TamaraSDKPayment.shared.setBillingAddress(firstName: firstName, lastName: lastName, phoneNumber: phoneNumber, addressLine1: addressLine1, addressLine2: addressLine2, country: country, region: region, city: city)
    }
    
    func setCurrency(newCurrency: String) {
        TamaraSDKPayment.shared.setCurrency(newCurrency: newCurrency)
    }
    
    func setShippingAmount(amount: Double) {
        TamaraSDKPayment.shared.setShippingAmount(amount: amount)
    }
    
    func setDiscount(amount: Double, name: String) {
        TamaraSDKPayment.shared.setDiscount(amount: amount, name: name)
    }
    
    func paymentOrder(fResult: @escaping FlutterResult) {
        TamaraSDKPayment.shared.startPayment() { result in
            switch result {
            case let .success(response):
                fResult(response.convertToJson())
            case let .failure(error):
                fResult(FlutterError(code: "",
                                    message: error.localizedDescription,
                                        details: nil))
            }
        }

    }
    
    func clearItem() {
        TamaraSDKPayment.shared.clearItem()
    }
    
    func authoriseOrder(orderId: String, fResult: @escaping FlutterResult){
        TamaraSDKPayment.shared.authoriseOrder(orderId: orderId) { result in
            switch result {
            case let .success(data):
                fResult(data.convertToJson())
            case let .failure(error):
                fResult(error.localizedDescription)
            }
        }
    }

    func checkPaymentOptions(jsonData: String, fResult: @escaping FlutterResult){
        TamaraSDKPayment.shared.checkPaymentOptions(jsonData: jsonData) { result in
            switch result {
            case let .success(data):
                fResult(data.convertToJson())
            case let .failure(error):
                fResult(error.localizedDescription)
            }
        }
    }

    func refunds(orderId: String, jsonData: String, fResult: @escaping FlutterResult){
        TamaraSDKPayment.shared.refunds(orderId: orderId, jsonData: jsonData) { result in
            switch result {
            case let .success(data):
                fResult(data.convertToJson())
            case let .failure(error):
                fResult(error.localizedDescription)
            }
        }
    }
    
    func orderDetail(orderId: String, fResult: @escaping FlutterResult) {
        TamaraSDKPayment.shared.orderDetail(orderId: orderId) { result in
            switch result {
            case let .success(response):
                fResult(response.convertToJson())
            case let .failure(error):
                fResult(FlutterError(code: "",
                                    message: error.localizedDescription,
                                        details: nil))
            }
        }
    }
    
    func capturePayment(jsonData: String, fResult: @escaping FlutterResult){
        TamaraSDKPayment.shared.capturePayment(jsonData: jsonData) { result in
            switch result {
            case let .success(response):
                fResult(response.convertToJson())
            case let .failure(error):
                fResult(error.localizedDescription)
            }
        }
    }
    
    func cancelOrder(orderId: String, jsonData: String, fResult: @escaping FlutterResult){
        //get order detail -> get total amount
        TamaraSDKPayment.shared.orderDetail(orderId: orderId) { result in
            switch result {
            case let .success(response):
                TamaraSDKPayment.shared.cancelOrder(orderId: orderId, jsonData: CancelOrder(discountAmount: nil, items: Array(), shippingAmount: nil, taxAmount: nil, totalAmount: response.totalAmount!).convertToJson()) { result in
                    switch result {
                    case let .success(data):
                        fResult(data.convertToJson())
                    case let .failure(error):
                        fResult(error.localizedDescription)
                    }
                }
            case let .failure(error):
                fResult(error.localizedDescription)
            }
        }
    }
    
    func updateOrderReference(orderId: String, orderReferenceId: String, fResult: @escaping FlutterResult) {
        TamaraSDKPayment.shared.updateOrderReference(orderId: orderId, orderReferenceId: orderReferenceId) { result in
            switch result {
            case let .success(data):
                fResult(data.convertToJson())
            case let .failure(error):
                fResult(error.localizedDescription)
            }
        }
    }
    
    func renderWidgetCartPage(language: String, country: String, publicKey: String,
                              amount: Double, result: @escaping FlutterResult) {
        TamaraSDKPayment.shared.renderWidgetCartPage(language: language, country: country, publicKey: publicKey, amount: amount){ response, error, code in
            if let response = response as? NSDictionary {
                switch code {
                case 200:
                    if let theJSONData = try? JSONSerialization.data(
                        withJSONObject: response,
                        options: []) {
                        let data = WidgetProperties(jsonData: theJSONData)
                        result(data.convertToJson())
                    }
                default:
                    result(response.allValues)
                }
            }
       }
    }
    
    func renderWidgetProduct(language: String, country: String, publicKey: String,
                              amount: Double, result: @escaping FlutterResult) {
        TamaraSDKPayment.shared.renderWidgetProduct(language: language, country: country, publicKey: publicKey, amount: amount){ response, error, code in
            if let response = response as? NSDictionary {
                switch code {
                case 200:
                    if let theJSONData = try? JSONSerialization.data(
                        withJSONObject: response,
                        options: []) {
                        let data = WidgetProperties(jsonData: theJSONData)
                        result(data.convertToJson())
                    }
                default:
                    result(response.allValues)
                }
            }
       }
    }
}
