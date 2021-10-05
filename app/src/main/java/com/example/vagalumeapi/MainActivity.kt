package com.example.vagalumeapi

//oq melhorar no app:
//mudar a cor do cursor
//aumentar fonte do editText
//no final da pagina criar um link para direcionar ao vagalume

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.vagalumeapi.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity(), MainContract.View {

    private val presenter = MainPresenter(this)

    //var que vai receber a instancia do binding
    private lateinit var binding : ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //podemos remover este setContentView pois a responsabilidade agora é do View Binding
        //setContentView(R.layout.activity_main)

        //Inflando a view através do View Binding utilizando o inflater da tela, que já existe
        //sempre = layoutInflater
        binding = ActivityMainBinding.inflate(layoutInflater)

        //Chamamos o setContentView para adicionar a view criada pelo View Binding
        //Esssa view é a representação do layout inflado ou seja o activity_main.xml inflado
        setContentView(binding.root)

        //setTitle("Find your song") ou apenas
        title = "Find your song"

        binding.buttonPesquisarMusica.setOnClickListener {
            presenter.buscarLetraMusica(pegarEntradaUsuario().first, pegarEntradaUsuario().second)
        }
    }

    fun pegarEntradaUsuario(): Pair<String, String> { //nao está no contrato,pois a responsavel pela chamada é a view
        val nomeCantor = binding.inputNomeCantor.editText?.text.toString()
        val nomeMusica = binding.inputNomeMusica.editText?.text.toString()
        return nomeCantor to nomeMusica
    }

    override fun exibirLetraMusica(letra : String) {
        Log.d("apiVagalume2", letra)
        binding.menssagemErro.text = ""
        binding.letraMusica.text = letra
        limparCamposInput()
    }

    override fun exibirMensagemErro() {
        binding.letraMusica.text = ""
        binding.menssagemErro.text = getString(R.string.mensagem_erro_requisicao)
        Toast.makeText(this@MainActivity, "Dados incorretos", Toast.LENGTH_SHORT).show()
    }

    override fun limparCamposInput() {
        binding.inputNomeMusica.editText?.text?.clear()
        binding.inputNomeCantor.editText?.text?.clear()
        binding.inputNomeCantor.requestFocus()
    }

    override fun exibirBarraProgresso() {
        binding.barraProgresso.visibility = View.VISIBLE
    }

    override fun esconderBarraProgresso() {
        binding.barraProgresso.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun closeKeyboard() {
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
