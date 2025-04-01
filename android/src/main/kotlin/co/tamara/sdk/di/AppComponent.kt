package co.tamara.sdk.di

import androidx.fragment.app.Fragment
import co.tamara.sdk.ui.TamaraInformationViewModel
import co.tamara.sdk.ui.TamaraPaymentViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class])
internal interface AppComponent {
    @Component.Builder
    interface Builder {

        fun appModule(appModule: AppModule): Builder

        fun build(): AppComponent
    }

    fun inject(fragment: Fragment)

    fun inject(tamaraPaymentViewModel: TamaraPaymentViewModel)

    fun inject(tamaraInformationViewModel: TamaraInformationViewModel)
}
