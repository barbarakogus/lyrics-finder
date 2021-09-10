package com.example.vagalumeapi

//oq melhorar no app:
//tratar erros da entrada do usuario
//dar focus no singer name ou tirar focus
//qdo apertar o botao fechar o teclado
//mudar a cor do cursor
//no final da pagina criar um link para direcionar ao vagalume

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPesquisarMusica = findViewById<Button>(R.id.button_pesquisar_musica)
        val letraMusica = findViewById<TextView>(R.id.letra_musica)
        val barraProgresso = findViewById<ProgressBar>(R.id.barra_progresso)

        btnPesquisarMusica.setOnClickListener {
            barraProgresso.visibility = View.VISIBLE
            val vagalumeApi = configurarRetrofit()
            val parametrosApi= pegarEntradaUsuario()

            CoroutineScope(Dispatchers.IO).launch {

                val resultadoLetraMusicas = vagalumeApi.bucarLetraMusica(parametrosApi.first, parametrosApi.second)

                withContext(Dispatchers.Main) {
                    Log.d("apiVagalume", resultadoLetraMusicas.toString())
                    val pegarMusica = resultadoLetraMusicas.mus
                    //Quando você usa o first(), caso o campo esteja vazio
                    //Você irá receber uma exceção. Neste caso você pode tentar
                    //utilizar o pegarMusica.firstOrNull()
                    //O especial dessa função é que ela retorna null nestes casos
                    //Então é fácil tratar tanto com ?.let {} quanto com if! :)
                    //O que você acha?
                    val pegarLetraMusica = pegarMusica.first().text
                    Log.d("apiVagalume2", pegarLetraMusica)
                    letraMusica.text = pegarLetraMusica
                    barraProgresso.visibility = View.INVISIBLE
                }
            }
            //O que você acha de as vezes manter o campo preenchido até ter
            //erro ou sucesso no busca e de travar os campos para edição durante
            //isto? Você pode tentar usar nos campos de texto e botão:
            //isEnabled = false
            //ex: inputNomeCantor.editText?.isEnabled = false
            limparCamposInput()

            //Sobre fechar o teclado, vou deixar um link para você dar uma olhada:
            //É um jeito muito "complexo" então vou olhar se não temos melhor alternativas atualmente
            //Mas este funciona e é bem utilizado atualmente, haha
            //Você vai precisar do InputMethodManager e de executar o hideSoftInputFromWindow
            //O código está em java mas é muito similar em Kotlin
            //https://www.geeksforgeeks.org/how-to-programmatically-hide-android-soft-keyboard/

            //Para dar foco em um campo você pode sempre executar o requestFocus()
            //Exemplo: inputNomeCantor.editText?.requestFocus()
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

    //Considerando que os campos inputNomeCantos e inputNomeMusica
    //O que você acha de mover os findViewById para a classe?
    //Assim você pode remover a função pegarEntradaUsuario e acessar
    //O text diretamente onde você precisar
    //Para limpar os campos você pode usar a mesma variável para
    //dar o clear() na função limparCamposInput() :)
    //O que você acha?
    fun pegarEntradaUsuario() : Pair<String, String>{
        val inputNomeCantor = findViewById<TextInputLayout>(R.id.input_nome_cantor)
        val inputNomeMusica = findViewById<TextInputLayout>(R.id.input_nome_musica)


        val nomeCantor = inputNomeCantor.editText?.text.toString()
        val nomeMusica = inputNomeMusica.editText?.text.toString()

        return nomeCantor to nomeMusica
    }

    fun limparCamposInput() {
        val inputNomeCantor = findViewById<TextInputLayout>(R.id.input_nome_cantor)
        val inputNomeMusica = findViewById<TextInputLayout>(R.id.input_nome_musica)

        inputNomeCantor?.editText?.text?.clear()
        inputNomeMusica?.editText?.text?.clear()
    }
}