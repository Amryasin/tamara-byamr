package co.tamara.sdk.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.tamara.sdk.R
import co.tamara.sdk.const.PaymentStatus
import co.tamara.sdk.databinding.TamaraFragmentWebViewBinding
import co.tamara.sdk.model.MerchantUrl
import co.tamara.sdk.model.response.CheckoutSession
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
internal class WebViewFragment : Fragment() {
    private var _binding: TamaraFragmentWebViewBinding? = null
    private val binding get() = _binding!!
    private var merchantUrl: MerchantUrl? = null

    private var photoPath: String? = null
    private var valueCallback: ValueCallback<Uri>? = null
    private var valuesCallback: ValueCallback<Array<Uri>>? = null
    private var checkoutSession: CheckoutSession? = null
    private var permissionRequest: PermissionRequest? = null
    private val REQUEST_CAMERA_PERMISSION = 12345

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            checkoutSession = it.getParcelable(ARG_CHECK_OUT_SESSION)
            merchantUrl = it.getParcelable(ARG_MERCHANT_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = TamaraFragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    val bundle =
                        bundleOf(TamaraPaymentFragment.ARG_PAYMENT_STATUS to PaymentStatus.STATUS_CANCEL.name)
                    findNavController().navigate(R.id.action_webViewFragment_to_tamaraPaymentFragment, bundle)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.webView.setBackgroundColor(Color.parseColor("#ffffff"))
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.allowUniversalAccessFromFileURLs = true
        binding.webView.settings.allowFileAccessFromFileURLs = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.mediaPlaybackRequiresUserGesture = false
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedHttpAuthRequest(
                view: WebView?,
                handler: HttpAuthHandler?,
                host: String?,
                realm: String?
            ) {
                handler?.proceed("tamara", "tamarapay@2020")
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                merchantUrl?.let { merchantUrl ->
                    request?.url?.let {
                        when {
                            it.toString().contains(merchantUrl.success) -> {
                                returnSuccess()
                            }

                            it.toString().contains(merchantUrl.cancel) -> {
                                returnCancel()
                            }

                            it.toString().contains(merchantUrl.failure) -> {
                                returnFailure()
                            }
                            else -> {
                                view?.loadUrl(it.toString())
                            }
                        }
                    }
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                activity?.let {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (valuesCallback != null) {
                    valuesCallback!!.onReceiveValue(null)
                }
                valuesCallback = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(requireActivity().getPackageManager()) != null) {
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", photoPath)
                    } catch (ex: IOException) {
                        Log.e("Webview", "Image file creation failed", ex)
                    }
                    if (photoFile != null) {
                        photoPath = "file:" + photoFile.getAbsolutePath()
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "*/*"
                val intentArray: Array<Intent>
                intentArray = takePictureIntent?.let { arrayOf(it) } ?: arrayOf<Intent>()
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, FCR)
                return true
            }

            override fun onPermissionRequest(request: PermissionRequest) {
                permissionRequest = request
                if (request.resources?.contains("android.permission.CAMERA") == true || request.resources?.contains("android.webkit.resource.VIDEO_CAPTURE") == true) {
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, so request it
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                    } else {
                        request.grant(request.resources)
                    }
                } else {
                    super.onPermissionRequest(request)
                }
            }
        }

        checkoutSession?.checkout_url?.let {
            binding.webView.loadUrl(it)
        }
//        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionRequest?.grant(permissionRequest?.resources)
            } else {
                Toast.makeText(requireActivity(), "Permission is not granted", Toast.LENGTH_SHORT).show()
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
    }

    // Create an image file
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        @SuppressLint("SimpleDateFormat") val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "img_" + timeStamp + "_"
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    fun openFileChooser(uploadMsg: ValueCallback<Uri?>?) {
        this.openFileChooser(uploadMsg, "*/*")
    }

    private fun openFileChooser(
        uploadMsg: ValueCallback<Uri?>?,
        acceptType: String?
    ) {
        this.openFileChooser(uploadMsg, acceptType, null)
    }

    private fun openFileChooser(
        uploadMsg: ValueCallback<Uri?>?,
        acceptType: String?,
        capture: String?
    ) {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "*/*"
        startActivityForResult(
            Intent.createChooser(i, "File Browser"),
            FILECHOOSER_RESULTCODE
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (Build.VERSION.SDK_INT >= 21) {
            var results: Array<Uri>? = null
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == valuesCallback) {
                        return
                    }
                    if (intent == null) { //Capture Photo if no image available
                        if (photoPath != null) {
                            results = arrayOf(Uri.parse(photoPath))
                        }
                    } else {
                        val dataString = intent.dataString
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }
            }
            valuesCallback!!.onReceiveValue(results)
            valuesCallback = null
        } else {
            if (requestCode == FCR) {
                if (null == valueCallback) return
                val result =
                    if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
                valueCallback!!.onReceiveValue(result)
                valueCallback = null
            }
        }
    }


    private fun returnFailure() {
        activity?.let {
            val bundle = Bundle()
            bundle.putString(
                TamaraPaymentFragment.ARG_PAYMENT_STATUS,
                PaymentStatus.STATUS_ERROR.name
            )
            bundle.putParcelable(ARG_CHECK_OUT_SESSION, checkoutSession)
            findNavController().navigate(R.id.action_webViewFragment_to_tamaraPaymentFragment, bundle)
        }
    }

    private fun returnSuccess() {
        activity?.let {
            val bundle = Bundle()
            bundle.putString(
                TamaraPaymentFragment.ARG_PAYMENT_STATUS,
                PaymentStatus.STATUS_SUCCESS.name
            )
            bundle.putParcelable(ARG_CHECK_OUT_SESSION, checkoutSession)
            findNavController().navigate(R.id.action_webViewFragment_to_tamaraPaymentFragment, bundle)
        }
    }

    private fun returnCancel() {
        activity?.let {
            val bundle = Bundle()
            bundle.putString(
                TamaraPaymentFragment.ARG_PAYMENT_STATUS,
                PaymentStatus.STATUS_CANCEL.name
            )
            bundle.putParcelable(ARG_CHECK_OUT_SESSION, checkoutSession)
            findNavController().navigate(R.id.action_webViewFragment_to_tamaraPaymentFragment, bundle)
        }
    }

    companion object {
        const val ARG_CHECK_OUT_SESSION = "CHECK_OUT_SESSION"
        const val ARG_MERCHANT_URL = "merchant_url"
        const val FILECHOOSER_RESULTCODE = 101
        const val FCR = 1
    }
}
