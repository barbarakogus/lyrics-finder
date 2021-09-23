package com.example.vagalumeapi

interface MainContract {

    interface model { //responsavel por fazer a conexao com a API
        fun buscarLetraMusicaAPI(nomeCantor : String, nomeMusica : String, onSuccess : (ResultadoPesquisa) -> Unit, onError : () -> Unit)
    }

    interface view { //responsavel por controlar a tela e tudo q aparece nela

        fun bindViews()
        fun pegarEntradaUsuario() : Pair<String, String>
        fun exibirBarraProgresso()
        fun exibirLetraMusica(letra : String)
        fun limparCamposInput()
        fun exibirMensagemErro()
        fun closeKeyboard()
        fun esconderBarraProgresso()
    }

    interface presenter { //é a conexao entre view e model. pegar os dados do model e emcaminha pra view
        fun buscarLetraMusica(nomeCantor: String, nomeMusica: String)
    }
}