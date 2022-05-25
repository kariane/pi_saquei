package br.senac.pi.data

data class Note(
    var title: String,
    var desc: String
)

object Notes {
    val listNotes = arrayListOf<Note>()
}