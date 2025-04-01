package co.tamara.sdk

import co.tamara.sdk.di.AppComponent
import co.tamara.sdk.di.AppModule
import co.tamara.sdk.di.DaggerAppComponent
import co.tamara.sdk.ui.TamaraInformationViewModel
import co.tamara.sdk.ui.TamaraPaymentViewModel

internal object DIHelper {
    private lateinit var appComponent: AppComponent
    private fun intComponent() {
        if (!::appComponent.isInitialized) {
            appComponent = DaggerAppComponent.builder()
                .appModule(AppModule())
                .build()
        }
    }

    @JvmStatic
    fun initAppComponent() {
        intComponent()
    }

     fun inject(tamaraPaymentViewModel: TamaraPaymentViewModel) {
        appComponent.inject(tamaraPaymentViewModel)
    }

    fun inject(tamaraInformationViewModel: TamaraInformationViewModel) {
        appComponent.inject(tamaraInformationViewModel)
    }

}