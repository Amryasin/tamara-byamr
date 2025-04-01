package co.tamara.sdk.log

import android.util.Log
import co.tamara.sdk.BuildConfig

internal class Logging {
    companion object{
        fun d(tag: String, message: String){
            if(BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
        }
        fun w(tag: String, message: String){
            if(BuildConfig.DEBUG){
                Log.w(tag, message)
            }
        }
    }
}