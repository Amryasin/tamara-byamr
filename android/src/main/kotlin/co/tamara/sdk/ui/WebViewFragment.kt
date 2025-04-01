package co.tamara.sdk.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import co.tamara.sdk.R
import co.tamara.sdk.const.PaymentStatus
import co.tamara.sdk.databinding.TamaraFragmentWebViewBinding
import co.tamara.sdk.model.MerchantUrl
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
internal class WebViewFragment : Fragment() {

    private var url: String? = null
    private var merchantUrl: MerchantUrl? = null

     private var photoPath: String? = null
     private var valueCallback: ValueCallback<Uri>? = null
     private var valuesCallback: ValueCallback<Array<Uri>>? = null
    private lateinit var binding: TamaraFragmentWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
            merchantUrl = it.getParcelable(ARG_MERCHANT_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = TamaraFragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    val bundle = bundleOf(TamaraPaymentFragment.ARG_PAYMENT_STATUS to PaymentStatus.STATUS_CANCEL.name)
                    findNavController(this@WebViewFragment).navigate(R.id.tamaraPaymentFragment, bundle)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        val settings: WebSettings = binding.webView.settings
        settings.javaScriptEnabled = true
        binding.webView.webViewClient = object: WebViewClient(){
            override fun onReceivedHttpAuthRequest(
                view: WebView?,
                handler: HttpAuthHandler?,
                host: String?,
                realm: String?
            ) {
                handler?.proceed("tamara","tamarapay@2020")
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String?
            ): Boolean {
                Log.d("Tamara", "url: $url")
                merchantUrl?.let {merchantUrl ->
                    when {
                        url?.contains(merchantUrl.success) == true -> {
                            returnSuccess()
                        }
                        url?.contains(merchantUrl.cancel) == true -> {
                            returnCancel()
                        }
                        url?.contains(merchantUrl.failure) == true -> {
                            returnFailure()
                        }
                        else -> {
                            url?.let {
                                view.loadUrl(url)
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
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.allowFileAccess=true
        binding.webView.settings.allowContentAccess=true
        binding.webView.settings.allowUniversalAccessFromFileURLs=true
        binding.webView.settings.allowFileAccessFromFileURLs=true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically=true
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
        }

        url?.let {
            binding.webView.loadUrl(it)
        }
        binding.progressBar.visibility = View.VISIBLE
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
            bundle.putString(TamaraPaymentFragment.ARG_PAYMENT_STATUS, PaymentStatus.STATUS_ERROR.name)
            findNavController(this).navigate(R.id.tamaraPaymentFragment, bundle)
        }
    }

    private fun returnSuccess() {
        activity?.let {
            val bundle = Bundle()
            bundle.putString(TamaraPaymentFragment.ARG_PAYMENT_STATUS, PaymentStatus.STATUS_SUCCESS.name)
            findNavController(this).navigate(R.id.tamaraPaymentFragment, bundle)
        }
    }

    private fun returnCancel() {
        activity?.let {
            val bundle = Bundle()
            bundle.putString(TamaraPaymentFragment.ARG_PAYMENT_STATUS, PaymentStatus.STATUS_CANCEL.name)
            findNavController(this).navigate(R.id.tamaraPaymentFragment, bundle)
        }
    }

    companion object {
        const val ARG_URL = "url"
        const val ARG_MERCHANT_URL = "merchant_url"
        const val FILECHOOSER_RESULTCODE = 101
        const val FCR = 1
    }
}
