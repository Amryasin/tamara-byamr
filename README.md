# Flutter SDK

## Requirements

- Dart sdk: "^3.5.0"
- Flutter: ">=3.24.0"
- Android: `minSdkVersion >= 21`, `compileSdk >= 34`, [AGP](https://developer.android.com/build/releases/gradle-plugin) (use [Android Studio - Android Gradle plugin Upgrade Assistant](https://developer.android.com/build/agp-upgrade-assistant) for help), support for `androidx` (see [AndroidX Migration](https://flutter.dev/docs/development/androidx-migration) to migrate an existing app)
- iOS 12.0+: `--ios-language swift`, Xcode version `>= 15.0`
- MacOS 12+: Xcode version `>= 15.0`
- Windows: [NuGet CLI](https://learn.microsoft.com/en-us/nuget/install-nuget-client-tools?tabs=windows#nugetexe-cli) available on your PATH environment variable

## Setup SDK

- Set path TamaraSDK to your project(TamaraSDK Example)
Example:
Add sdk to your pubspec.yaml file

dependencies:
   tamara_flutter_sdk: version

## Install
- run command line TamaraSDK Example: 
+ flutter clean + flutter pub get: update new library
+ cd to IOS folder-> pod install -> install library IOS for TamaraSDK and
TamaraSDK Example(pod file)

##How to use it
Include function:
* Init:
Initialize before using it:
TamaraPayment.initialize(AUTH_TOKEN, API_URL, NOTIFICATION_WEB_HOOK_URL, PUBLISH_KEY, NOTIFICATION_TOKEN, isSandbox)

## Create and pay order
Before adding order's information, create Order by call this method with referenceOrderId and description.
RefId is your unique id of your order.
```
TamaraPayment.createOrder(referenceOrderId, description)
```
### These informations are mandatory:

Set customer's information:
```
TamaraPayment.setCustomerInfo(firstName, lastName,
            phoneNumber, email, isFirstOrder)
```

Set payment type (optional: default: PAY_BY_INSTALMENTS):
```
TamaraPayment.setPaymentType(paymentType)
```

Add Item with its price, tax and discount:
```
TamaraPayment.addItem(name, referenceId ,sku, type, unitPrice,
                    taxAmount ,discountAmount, quantity)
```

Set shipping address and billing address:
```
TamaraPayment.setShippingAddress(firstName,lastName, phone,
                    addressLine1, addressLine2, country, region, city)
TamaraPayment.setBillingAddress(firstName,lastName, phone,
                    addressLine1, addressLine2, country, region, city)
```

Set shipping fee:
```
TamaraPayment.setShippingAmount(shippingFee)
```

Set discount (optional):
```
TamaraPayment.setDiscount(discount, name)
```

Set instalments:
```
TamaraPayment.setInstalments(instalments)
```

Set locale:
```
TamaraPayment.setLocale(locale)
```

Set order number:
```
TamaraPayment.setOrderNumber(orderNumber)
```

Set expires in minutes:
```
TamaraPayment.setExpiresInMinutes(expiresInMinutes)
```

Set risk assessment:
```
TamaraPayment.setRiskAssessment(jsonData)
```
Example: 
bool result = await TamaraPayment.setRiskAssessment(jsonData);
if (result) {
  //json ok
}
```

Set additional data:
```
TamaraPayment.setAdditionalData(jsonData)
```

Add Custom Fields AdditionalData: 
Example val jsonData = "{\"custom_field1\": 42, \"custom_field2\": \"value2\" }"
```
TamaraPayment.addCustomFieldsAdditionalData(jsonData)
```

Processes to Tamara payment page using:
```
TamaraPayment.paymentOrder()
```

## Order detail
Get order detail
param mandatory: orderId
```
TamaraPayment.getOrderDetail(orderId)
```
Example:
Response: parse json data -> model
String result = await TamaraSdk.getOrderDetail(orderId);
final orderDetail = OrderDetail.fromJson(jsonDecode(result));
OrderDetail.fromJson(Map<String, dynamic> json) {
    billingAddress = json['billing_address'] != null
        ? BillingAddress.fromJson(json['billing_address'])
        : null;
    canceledAmount = json['canceled_amount'] != null
        ? CanceledAmount.fromJson(json['canceled_amount'])
        : null;
    capturedAmount = json['captured_amount'] != null
        ? CanceledAmount.fromJson(json['captured_amount'])
        : null;
    consumer =
        json['consumer'] != null ? Consumer.fromJson(json['consumer']) : null;
    countryCode = json['country_code'];
    createdAt = json['created_at'];
    description = json['description'];
    discountAmount = json['discount_amount'] != null
        ? CanceledAmount.fromJson(json['discount_amount'])
        : null;
    instalments = json['instalments'];
    if (json['items'] != null) {
      items = <Items>[];
      json['items'].forEach((v) {
        items!.add(Items.fromJson(v));
      });
    }
    orderId = json['order_id'];
    orderNumber = json['order_number'];
    orderReferenceId = json['order_reference_id'];
    paidAmount = json['paid_amount'] != null
        ? CanceledAmount.fromJson(json['paid_amount'])
        : null;
    paymentType = json['payment_type'];
    platform = json['platform'];
    refundedAmount = json['refunded_amount'] != null
        ? CanceledAmount.fromJson(json['refunded_amount'])
        : null;
    settlementStatus = json['settlement_status'];
    shippingAddress = json['shipping_address'] != null
        ? BillingAddress.fromJson(json['shipping_address'])
        : null;
    shippingAmount = json['shipping_amount'] != null
        ? CanceledAmount.fromJson(json['shipping_amount'])
        : null;
    status = json['status'];
    taxAmount = json['tax_amount'] != null
        ? CanceledAmount.fromJson(json['tax_amount'])
        : null;
    totalAmount = json['total_amount'] != null
        ? CanceledAmount.fromJson(json['total_amount'])
        : null;
    walletPrepaidAmount = json['wallet_prepaid_amount'] != null
        ? CanceledAmount.fromJson(json['wallet_prepaid_amount'])
        : null;
  }

## Authorise order
Authorise order by call this method with orderId.
param mandatory: orderId
```
TamaraPayment.authoriseOrder(orderId)
```
Example:
Response:
String result = await TamaraSdk.authoriseOrder(orderId);
final authoriseOrder = AuthoriseOrder.fromJson(jsonDecode(result));
AuthoriseOrder.fromJson(Map<String, dynamic> json) {
    autoCaptured = json['auto_captured'];
    orderExpiryTime = json['order_expiry_time'];
    orderId = json['order_id'];
    paymentType = json['payment_type'];
    status = json['status'];
  }

## Cancel order
Note: Need call authorise order method before call cancel order
Cancel order reference by call this method with orderId and jsonData.

param mandatory: orderId
jsonData: use library convert class CancelOrderRequest to json (Gson)
```
TamaraPayment.cancelOrder(orderId, jsonData)
```
Example:
Response:
```
final cancelOrder = CancelOrder.fromJson(jsonDecode(result));

## Update order reference

Update order reference by call this method with orderId and orderReference.

param mandatory: orderId, orderReference
```
TamaraPayment.updateOrderReference(orderId, orderReference)
```
Example:
```
Response:
final orderReference = OrderReference.fromJson(jsonDecode(result));
OrderReference.fromJson(Map<String, dynamic> json) {
    message = json['message'];
  }
```
## Capture a payment
Note: Need call authorise order method before call capture a payment
Cancel order reference by call this method with orderId and jsonData.

param mandatory: orderId
jsonData: use library convert class Capture to json (Gson)
```
TamaraPayment.getCapturePayment(jsonData)
```

Example:
Response:
TamaraSdk.getCapturePayment(jsonEncode(capture.toJson()));
Capture.fromJson(Map<String, dynamic> json) {
    orderId = json['order_id'];
    totalAmount = EAmount.fromJson(json['total_amount']);
    taxAmount = EAmount.fromJson(json['tax_amount']);
    shippingAmount = EAmount.fromJson(json['shipping_amount']);
    discountAmount = EAmount.fromJson(json['discount_amount']);
    if (json['items'] != null) {
      items = <Item>[];
      json['items'].forEach((itemJson) {
        items!.add(Item.fromJson(itemJson));
      });
    }
    shippingInfo = ShippingInfo.fromJson(json['shipping_info']);
  }

## Refunds
Cancel order reference by call this method with orderId and jsonData.
Note: Need call authorise order method before call Refunds

param mandatory: orderId
jsonData: use library convert class Refund to json (Gson)

```
TamaraPayment.refunds(orderId, jsonData)
```
Example:
Response:
Refund refund = Refund(totalAmount: totalAmount, comment: comment);
final result = await TamaraSdk.refunds(orderId, jsonEncode(refund));
## Render widget cart page
Render widget cart page reference by call this method with language, country, publicKey, amount.
param mandatory: language, country, publicKey, amount

```
TamaraPayment.renderWidgetCartPage(language, country, publicKey, amount)
```
Example:
Response:
String result =
        await TamaraSdk.renderCartPage(language, country, publicKey, amount);
final cartPage = CartPage.fromJson(jsonDecode(result));

CartPage.fromJson(Map<String, dynamic> json) {
    script = json['script'];
    url = json['url'];
  }

## Render widget product
Render widget product reference by call this method with language, country, publicKey, amount.
param mandatory: language, country, publicKey, amount

```
TamaraPayment.renderWidgetProduct(language, country, publicKey, amount)
```
Example:
Response:
String result =
        await TamaraSdk.renderProduct(language, country, publicKey, amount);
Product product = Product.fromJson(jsonDecode(result));

Product.fromJson(Map<String, dynamic> json) {
    script = json['script'];
    url = json['url'];
  }