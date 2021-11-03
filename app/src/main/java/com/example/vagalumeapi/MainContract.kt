package com.example.vagalumeapi

interface MainContract {

    interface Model { //responsavel por fazer a conexao com a API
        fun buscarLetraMusicaAPI(nomeCantor : String, nomeMusica : String, onSuccess : (ResultadoPesquisa) -> Unit, onError : () -> Unit)
    }

    interface View { //responsavel por controlar a tela e tudo q aparece nela
        fun exibirBarraProgresso()
        fun exibirLetraMusica(letra : String)
        fun limparCamposInput()
        fun exibirMensagemErro()
        fun closeKeyboard()
        fun esconderBarraProgresso()
        fun exibirLinkPaginaVagalume(valor : Boolean)
    }

    interface Presenter { //é a conexao entre view e model. pegar os dados do model e encaminha para view
        fun buscarLetraMusica(nomeCantor: String, nomeMusica: String)
    }
}