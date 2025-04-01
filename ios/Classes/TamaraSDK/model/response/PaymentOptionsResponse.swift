//
//  PaymentOptionsResponse.swift
//  TamaraSDK
//
//  Created by phong on 1/4/24.
//

import Foundation
public struct PaymentOptionsResponse: Codable {
    var hasAvailablePaymentOptions: Bool? = false
    var singleCheckoutEnabled: Bool? = false
    var availablePaymentLabels: Array<AvailablePayment>? = Array()
    
    enum CodingKeys: String, CodingKey {
        case hasAvailablePaymentOptions = "has_available_payment_options"
        case singleCheckoutEnabled = "single_checkout_enabled"
        case availablePaymentLabels = "available_payment_labels"
    }

    init(jsonData: Data) {
        let decoder = JSONDecoder()
        self = try! decoder.decode(PaymentOptionsResponse.self, from: jsonData)
    }

    func convertToJson() -> String {
        let jsonData = try! JSONEncoder().encode(self)
        return String(data: jsonData, encoding: .utf8)!
    }
}
