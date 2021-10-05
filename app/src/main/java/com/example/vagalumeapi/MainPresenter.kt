package com.example.vagalumeapi

class MainPresenter(private val view : MainContract.View) : MainContract.Presenter {

    private val model = MainModel()

    override fun buscarLetraMusica(nomeCantor: String, nomeMusica: String) {
        view.exibirBarraProgresso()
        view.closeKeyboard()

        model.buscarLetraMusicaAPI(
            nomeCantor,
            nomeMusica,
            onSuccess = {
                val pegarMusica = it.mus
                val pegarLetraMusica = pegarMusica.firstOrNull()?.lyrics
                if (pegarLetraMusica != null) {
                    view.exibirLetraMusica(pegarLetraMusica)
                }else {
                    view.exibirMensagemErro()
                }
                view.esconderBarraProgresso()
            },
            onError = {
                view.esconderBarraProgresso()
                view.exibirMensagemErro()
            }
        )
    }
}

