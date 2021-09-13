package com.example.vagalumeapi

//oq melhorar no app:
//mudar a cor do cursor
//aumentar fonte do editText
//no final da pagina criar um link para direcionar ao vagalume

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var inputNomeCantor : TextInputLayout? = null
    var inputNomeMusica : TextInputLayout? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPesquisarMusica = findViewById<Button>(R.id.button_pesquisar_musica)
        val letraMusica = findViewById<TextView>(R.id.letra_musica)
        val menssagemErro = findViewById<TextView>(R.id.menssagem_erro)
        val barraProgresso = findViewById<ProgressBar>(R.id.barra_progresso)
        inputNomeCantor = findViewById(R.id.input_nome_cantor)
        inputNomeMusica = findViewById(R.id.input_nome_musica)

        btnPesquisarMusica.setOnClickListener {
            barraProgresso.visibility = View.VISIBLE
            val vagalumeApi = configurarRetrofit()
            val nomeCantor = inputNomeCantor?.editText?.text.toString()
            val nomeMusica = inputNomeMusica?.editText?.text.toString()

            CoroutineScope(Dispatchers.IO).launch {

                val resultadoLetraMusicas = vagalumeApi.bucarLetraMusica(nomeCantor, nomeMusica)

                withContext(Dispatchers.Main) {
                    Log.d("apiVagalume", resultadoLetraMusicas.toString())

                    val pegarMusica = resultadoLetraMusicas?.mus
                    val pegarLetraMusica = pegarMusica?.firstOrNull()?.lyrics

                    if(pegarLetraMusica != null) {
                        Log.d("apiVagalume2", pegarLetraMusica)
                        menssagemErro.text = ""
                        letraMusica.text = pegarLetraMusica
                        limparCamposInput()
                    }else {
                        letraMusica.text = ""
                        menssagemErro.text = getString(R.string.mensagem_erro_requisicao)
                        Toast.makeText(this@MainActivity, "Dados incorretos", Toast.LENGTH_SHORT).show()
                    }
                    closeKeyboard()
                    barraProgresso.visibility = View.INVISIBLE
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

    fun limparCamposInput() {
        inputNomeCantor?.editText?.text?.clear()
        inputNomeMusica?.editText?.text?.clear()
        inputNomeCantor?.editText?.requestFocus()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun closeKeyboard() {
        // this will give us the view which is currently focus in this layout
        val view = this.currentFocus

        // if nothing is currently focus then this will protect the app from crash
        if (view != null) {
            // now assign the system service to InputMethodManager
            val manager: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }
}