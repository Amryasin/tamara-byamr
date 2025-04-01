library tamara_checkout;

import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:webview_flutter_wkwebview/webview_flutter_wkwebview.dart';

class TamaraCheckout extends StatefulWidget {
  final String checkoutUrl;
  final String successUrl;
  final String failUrl;
  final String cancelUrl;

  const TamaraCheckout(
      this.checkoutUrl, this.successUrl, this.failUrl, this.cancelUrl,
      {Key? key,
      this.onPaymentSuccess,
      this.onPaymentFailed,
      this.onPaymentCanceled})
      : super(key: key);

  final void Function()? onPaymentSuccess;
  final void Function()? onPaymentFailed;
  final void Function()? onPaymentCanceled;

  @override
  _TamaraCheckoutState createState() => _TamaraCheckoutState();
}

class _TamaraCheckoutState extends State<TamaraCheckout> {
  late PlatformWebViewControllerCreationParams _params;
  WebViewController? _controller;
  WebViewPermissionRequest? requestPermission;

  @override
  void initState() {
    super.initState();
    if (Platform.isIOS) {
      _params = WebKitWebViewControllerCreationParams(
          allowsInlineMediaPlayback: true);
    } else {
      _params = const PlatformWebViewControllerCreationParams();
    }
    _controller = WebViewController.fromPlatformCreationParams(
      _params,
      onPermissionRequest: Platform.isAndroid
          ? (WebViewPermissionRequest request) async {
              requestPermission = request;
              await requestCameraPermission();
            }
          : null,
    );

    _controller!
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setBackgroundColor(const Color(0x00000000))
      ..setNavigationDelegate(
        NavigationDelegate(
          onProgress: (int progress) {
            // ignore: avoid_print
            print('WebView is loading (progress : $progress%)');
          },
          onPageStarted: (String url) {},
          onPageFinished: (String url) {},
          onWebResourceError: (WebResourceError error) {},
          onNavigationRequest: (NavigationRequest request) {
            String url = request.url;
            if (url.startsWith(widget.successUrl)) {
              if (widget.onPaymentSuccess != null) {
                widget.onPaymentSuccess!();
                return NavigationDecision.prevent;
              }
            } else if (url.startsWith(widget.failUrl)) {
              if (widget.onPaymentFailed != null) {
                widget.onPaymentFailed!();
                return NavigationDecision.prevent;
              }
            } else if (url.startsWith(widget.cancelUrl)) {
              if (widget.onPaymentCanceled != null) {
                widget.onPaymentCanceled!();
                return NavigationDecision.prevent;
              }
            }
            return NavigationDecision.navigate;
          },
        ),
      )
      ..loadRequest(Uri.parse(widget.checkoutUrl));
  }

  Future<void> requestCameraPermission() async {
    var status = await Permission.camera.status;
    if (status.isDenied) {
      status = await Permission.camera.request();
      if (status.isGranted) {
        requestPermission?.grant();
      } else {}
    } else if (status.isGranted) {
      requestPermission?.grant();
    } else if (status.isPermanentlyDenied) {
      openAppSettings();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Builder(builder: (BuildContext context) {
      return WebViewWidget(controller: _controller!);
    });
  }
}
