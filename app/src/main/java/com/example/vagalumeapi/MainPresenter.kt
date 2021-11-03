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
                    view.exibirLinkPaginaVagalume(true)
                }else {
                    view.exibirMensagemErro()
                    view.exibirLinkPaginaVagalume(false)
                }
                view.esconderBarraProgresso()
            },
            onError = {
                view.esconderBarraProgresso()
                view.exibirMensagemErro()
                view.exibirLinkPaginaVagalume(false)
            }
        )
    }
}

