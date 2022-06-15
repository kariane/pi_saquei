package br.senac.pi.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.senac.pi.R
import br.senac.pi.databinding.CardNoteBinding
import br.senac.pi.databinding.FragmentMatchBinding
import br.senac.pi.model.Encontro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MatchFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentMatchBinding

    // Função criada para se conectar ao Firebase, trazer os dados que preciso (neste caso, os encontros)
    fun configurarBase(inflater: LayoutInflater){

        val usuario = FirebaseAuth.getInstance().currentUser

        if(usuario != null){
            database = FirebaseDatabase.getInstance().reference.child(usuario.uid)
        }

        // Montando listener para pegar todas as alterações do banco de dados em tempo real
        var databaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                handleData(snapshot, inflater)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "onCancelled", error.toException())
            }
        }

        // Buscando dados dos encontros no Firebase
        database.child("match").addValueEventListener(databaseListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMatchBinding.inflate(layoutInflater)
        database = Firebase.database.reference

        binding.buttonMatch.setOnClickListener {
            val frag = AddMatchFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()

        }
        configurarBase(inflater)
        return binding.root
    }

    // Função criada para recuperar os dados do banco e colocá-los em uma lista de modo que possamos mandá-lo ao front-end
    private fun handleData(snapshot: DataSnapshot, inflater: LayoutInflater) {
        // Inicializando uma variável vazia com uma lista de encontros
        val itemList = arrayListOf<Encontro>()

        // Montando um item list com todos os encontros
        snapshot.children.forEach { encontro ->
            val match = encontro.getValue(Encontro::class.java)

            match?.let { e ->
                itemList.add(e)
            }
        }
        refreshUi(itemList, inflater)
    }

    // Função criada para atualizar a tela de listagem
    fun refreshUi(list: List<Encontro>, inflater: LayoutInflater) {
        // Removendo listagem de encontros
        binding.container.removeAllViews()

        // Recebendo a lista com os encontros e adicionamento novamente na tela
        list.forEach() {
            val card = CardNoteBinding.inflate(inflater)

            // Adicionando texto de cada campo do Firebase na tela
            card.textTitleNome.text = "Matéria: " + it.materia
            card.textTitleData.text = "Data: " + it.data
            card.textTitleLocal.text = "Local: " + it.local
            card.textTitleUsuarioMatch.text = "Usuário do encontro: " + it.usuario

            // Adicionando listener no ícone de deletar local
            card.imageDelete.setOnClickListener { imageView ->
                val noMatch = database.child("match").child(it.id)
                noMatch.removeValue()
            }

            // Adicionando o local atual na visualização da lista
            binding.container.addView(card.root)
        }
    }
}
