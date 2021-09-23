package com.example.vagalumeapi

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainModel : MainContract.model {

    val vagalumeApi = configurarRetrofit()

    override fun buscarLetraMusicaAPI(
        nomeCantor : String,
        nomeMusica : String,
        onSuccess : (ResultadoPesquisa) -> Unit,
        onError : () -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resultadoLetraMusicas = vagalumeApi.buscarLetraMusica(nomeCantor, nomeMusica)
                withContext(Dispatchers.Main) {
                    if (resultadoLetraMusicas == null) {
                        Log.d("erroNull", "com conexao")
                        onError()
                    }else {
                        Log.d("apiVagalume", resultadoLetraMusicas.toString())
                        onSuccess(resultadoLetraMusicas)
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main){
                    Log.d("erroCatch", "modoAviao")
                    Log.d("catch", ex.toString())
                    onError()
                }
            }
        }
    }

    fun configurarRetrofit() : VagalumeAPI {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.vagalume.com.br/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(VagalumeAPI::class.java)
        return api
    }
}