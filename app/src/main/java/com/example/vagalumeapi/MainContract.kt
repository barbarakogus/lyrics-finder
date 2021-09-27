package com.example.vagalumeapi

interface MainContract {

    //É uma "boa prática" iniciar nome de classes com letras maíusculas
    //Neste caso poderíamos ter Model, View e Presenter, o que acha? :)
    interface model { //responsavel por fazer a conexao com a API
        fun buscarLetraMusicaAPI(nomeCantor : String, nomeMusica : String, onSuccess : (ResultadoPesquisa) -> Unit, onError : () -> Unit)
    }

    interface view { //responsavel por controlar a tela e tudo q aparece nela
        //Como o bindViews não será chamado pelo presenter
        //Não há necessidade dele ficar no contrato
        //Deixamos aqui geralmente o que será chamado entre as camadas :)
        fun bindViews()
        //Acredito que o mesmo se aplique para o pegarEntradaUsuario.
        //Como são funcões que só são chamadas na view, pela view
        //Podemos retirar elas daqui. O que acha? Concorda? :)
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