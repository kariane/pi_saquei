package br.senac.pi.model

// Esta classe mapeia a informação para que ela tenha este mesmo formato no banco de dados (Firebase)
data class Local(
    var id: String = "",
    var surname: String = "",
    var address: String = "",
    var CEP: String = "",
    var provincy: String = ""
)