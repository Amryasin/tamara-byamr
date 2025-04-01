
Integrate Tamara Checkout Flow into Flutter App

## Features

- Add callback for checkout flows:
    * onPaymentSuccess
    * onPaymentFailed
    * onPaymentCanceled

## Getting started

Add sdk to your pubspec.yaml file
```dart
dependencies:
  flutter:
    sdk: flutter
  tamara_sdk: ^0.9.1
```

## Get URLs

Your Back End must call our Api for generating:
* Checkout URL
* Success URL
* Failed URL
* Canceled URL

## Usage

Include TamaraCheckout in your Widget builder

```dart
Expanded(
  child: TamaraCheckout(
      checkoutUrl
      successUrl,
      failedUrl,
      canceledUrl,
    onPaymentSuccess: () {
      _onPaymentSuccess();
    },
    onPaymentFailed: () {
      _onPaymentFailed();
    },
    onPaymentCanceled: () {
      _onPaymentCanceled();
    },
  )
);
```
