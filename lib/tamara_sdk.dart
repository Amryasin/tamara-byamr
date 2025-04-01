library tamara_sdk;

import 'dart:core';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';

class TamaraSdk {
  static var platform = Platform.isAndroid
      ? const MethodChannel('co.tamara.sdk')
      : const MethodChannel('co.tamara.sdk/ios');

  TamaraSdk._();

  static Future<void> initSdk(
    String authToken,
    String apiUrl,
    String notificationWebHookUrl,
    String publishKey,
    String notificationToken,
    bool isSandbox,
  ) async {
    try {
      await platform.invokeMethod('initSdk', <String, dynamic>{
        'authToken': authToken,
        'apiUrl': apiUrl,
        'notificationWebHook': notificationWebHookUrl,
        'publishKey': publishKey,
        'notificationToken': notificationToken,
        'isSandbox': isSandbox,
      });
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> createOrder(
      String orderReferenceId, String description) async {
    try {
      await platform.invokeMethod('createOrder', <String, String>{
        'orderReferenceId': orderReferenceId,
        'description': description
      });
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setCountry(String countryCode, String currency) async {
    try {
      await platform.invokeMethod('setCountry',
          <String, String>{'countryCode': countryCode, 'currency': currency});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setPaymentType(String paymentType) async {
    try {
      await platform.invokeMethod(
          'setPaymentType', <String, String>{'paymentType': paymentType});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setInstalments(int instalments) async {
    try {
      await platform.invokeMethod(
          'setInstalments', <String, int>{'instalments': instalments});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setPlatform(String platfm) async {
    try {
      await platform.invokeMethod(
          'setPlatform', <String, String>{'platfm': platfm});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setLocale(String locale) async {
    try {
      await platform
          .invokeMethod('setLocale', <String, String>{'locale': locale});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setOrderNumber(String orderNumber) async {
    try {
      await platform.invokeMethod(
          'setOrderNumber', <String, String>{'orderNumber': orderNumber});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setExpiresInMinutes(int expiresInMinutes) async {
    try {
      await platform.invokeMethod('setExpiresInMinutes',
          <String, int>{'expiresInMinutes': expiresInMinutes});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<bool> setRiskAssessment(String jsonData) async {
    try {
      final result = await platform.invokeMethod(
          'setRiskAssessment', <String, String>{'jsonData': jsonData});
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<void> setAdditionalData(String jsonData) async {
    try {
      await platform.invokeMethod(
          'setAdditionalData', <String, String>{'jsonData': jsonData});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> addCustomFieldsAdditionalData(String jsonData) async {
    try {
      await platform.invokeMethod('addCustomFieldsAdditionalData',
          <String, String>{'jsonData': jsonData});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setCustomerInfo(String firstName, String lastName,
      String phoneNumber, String email, bool isFirstOrder) async {
    try {
      await platform.invokeMethod('setCustomerInfo', <String, dynamic>{
        'firstName': firstName,
        'lastName': lastName,
        'phoneNumber': phoneNumber,
        'email': email,
        'isFirstOrder': isFirstOrder,
      });
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> addItem(
      String name,
      String referenceId,
      String sku,
      String type,
      double unitPrice,
      double tax,
      double discount,
      int quantity) async {
    try {
      await platform.invokeMethod('addItem', <String, dynamic>{
        'name': name,
        'referenceId': referenceId,
        'sku': sku,
        'type': type,
        'unitPrice': unitPrice,
        'tax': tax,
        'discount': discount,
        'quantity': quantity,
      });
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setShippingAddress(
      String firstName,
      String lastName,
      String phoneNumber,
      String addressLine1,
      String addressLine2,
      String country,
      String region,
      String city) async {
    try {
      await platform.invokeMethod('setShippingAddress', <String, String>{
        'firstName': firstName,
        'lastName': lastName,
        'phoneNumber': phoneNumber,
        'addressLine1': addressLine1,
        'addressLine2': addressLine2,
        'country': country,
        'region': region,
        'city': city,
      });
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setBillingAddress(
      String firstName,
      String lastName,
      String phoneNumber,
      String addressLine1,
      String addressLine2,
      String country,
      String region,
      String city) async {
    try {
      await platform.invokeMethod('setBillingAddress', <String, String>{
        'firstName': firstName,
        'lastName': lastName,
        'phoneNumber': phoneNumber,
        'addressLine1': addressLine1,
        'addressLine2': addressLine2,
        'country': country,
        'region': region,
        'city': city,
      });
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setCurrency(String newCurrency) async {
    try {
      await platform.invokeMethod(
          'setCurrency', <String, String>{'newCurrency': newCurrency});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setShippingAmount(double amount) async {
    try {
      await platform.invokeMethod(
          'setShippingAmount', <String, dynamic>{'amount': amount});
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<void> setDiscount(double amount, String name) async {
    try {
      await platform.invokeMethod('setDiscount', <String, dynamic>{
        'amount': amount,
        'name': name,
      });
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static Future<String> paymentOrder() async {
    try {
      final result = await platform.invokeMethod('paymentOrder');
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<String> getOrderDetail(String orderId) async {
    try {
      final result = await platform
          .invokeMethod('orderDetail', <String, String>{'orderId': orderId});
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<Object> getCapturePayment(String jsonData) async {
    try {
      Object result = await platform.invokeMethod(
          'capturePayment', <String, String>{'capturePayment': jsonData});
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<Object> checkPaymentOptions(String jsonData) async {
    try {
      Object result = await platform.invokeMethod(
          'checkPaymentOptions', <String, String>{'checkPaymentOptions': jsonData});
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<Object> refunds(String orderId, String jsonData) async {
    try {
      Object result = await platform.invokeMethod(
          'refunds', <String, String>{'orderId': orderId, 'refunds': jsonData});
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<String> cancelOrder(String orderId, String jsonData) async {
    try {
      final result = await platform.invokeMethod('cancelOrder',
          <String, String>{'orderId': orderId, 'cancelOrder': jsonData});
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<String> updateOrderReference(
      String orderId, String orderReferenceId) async {
    try {
      final result = await platform.invokeMethod(
          'updateOrderReference', <String, String>{
        'orderId': orderId,
        'orderReferenceId': orderReferenceId
      });
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<String> renderCartPage(
      String language, String country, String publicKey, double amount) async {
    try {
      final result =
          await platform.invokeMethod('renderCartPage', <String, dynamic>{
        'language': language,
        'country': country,
        'publicKey': publicKey,
        'amount': amount
      });
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<String> renderProduct(
      String language, String country, String publicKey, double amount) async {
    try {
      final result =
          await platform.invokeMethod('renderProduct', <String, dynamic>{
        'language': language,
        'country': country,
        'publicKey': publicKey,
        'amount': amount
      });
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<String> authoriseOrder(String orderId) async {
    try {
      final result = await platform
          .invokeMethod('authoriseOrder', <String, String>{'orderId': orderId});
      return result;
    } on PlatformException catch (e) {
      showToast(e.message.toString());
      rethrow;
    }
  }

  static Future<void> clearItem() async {
    try {
      await platform.invokeMethod(
        'clearItem',
      );
    } on PlatformException catch (e) {
      showToast(e.message.toString());
    }
  }

  static showToast(String message) async {
    Fluttertoast.showToast(
        msg: message,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        backgroundColor: const Color.fromARGB(255, 110, 110, 110),
        textColor: Colors.white,
        fontSize: 14.0);
  }
}
