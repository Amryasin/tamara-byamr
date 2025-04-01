/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.tamara.sdk.di

import co.tamara.sdk.BuildConfig
import co.tamara.sdk.TamaraPayment
import co.tamara.sdk.api.Service
import co.tamara.sdk.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module//(includes = [ViewModelModule::class])
internal class AppModule {
    @Singleton
    @Provides
    fun provideTamaraService(): Service {
        var builder = OkHttpClient.Builder()
        builder.addInterceptor { chain ->
            var original = chain.request()
            var requestBuilder = original.newBuilder()
            requestBuilder.apply {
                header("Content-Type","application/json")
                header("Authorization", "Bearer ${TamaraPayment.getInstance().token}")
                method(original.method, original.body)
            }
            chain.proceed(requestBuilder.build())
        }
        if(BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(logging)
        }
        var client = builder.build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(TamaraPayment.getInstance().apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(Service::class.java)
    }
}
