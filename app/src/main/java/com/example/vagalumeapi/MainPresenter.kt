package com.example.vagalumeapi

class MainPresenter(private val view : MainContract.view) : MainContract.presenter {

    private val model = MainModel()

    override fun buscarLetraMusica(nomeCantor: String, nomeMusica: String) {
        view.exibirBarraProgresso()

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
            },
            onError = {
                view.exibirMensagemErro()
            }
        )
    }
}

