package br.senac.pi.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.senac.pi.R
import br.senac.pi.databinding.CardNoteMateriaBinding
import br.senac.pi.databinding.FragmentMateriaBinding
import br.senac.pi.model.Materia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MateriaFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentMateriaBinding

    // Função criada para se conectar ao Firebase, trazer os dados que preciso (neste caso, as materias)
    fun configurarBase(inflater: LayoutInflater){

        val usuario = FirebaseAuth.getInstance().currentUser

        if(usuario != null){
            database = FirebaseDatabase.getInstance().reference
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

        // Buscando dados dos locais no Firebase
        database.child("materias").addValueEventListener(databaseListener)
    }

    // Função criada para recuperar os dados do banco e colocá-los em uma lista de modo que possamos mandá-lo ao front-end
    private fun handleData(snapshot: DataSnapshot, inflater: LayoutInflater) {
        // Inicializando uma variável vazia com uma lista de locais
        val itemList = arrayListOf<Materia>()

        // Montando um item list com todos os locais
        snapshot.children.forEach { materia ->
            val materia = materia.getValue(Materia::class.java)

            materia?.let { m ->
                itemList.add(m)
            }

        }
        refreshUi(itemList, inflater)
    }

    // Função criada para atualizar a tela de listagem
    fun refreshUi(list: List<Materia>, inflater: LayoutInflater) {
        // Removendo listagem de materias
        binding.container.removeAllViews()

        // Recebendo a lista com as materias e adicionamento novamente na tela
        list.forEach() { materia ->
            val card = CardNoteMateriaBinding.inflate(inflater)

            // Adicionando texto de cada campo do Firebase na tela
            card.textTitleNome.text = materia.nome

            // Adicionando listener no ícone de deletar materia
            card.imageDelete.setOnClickListener { imageView ->
                val noMateria = database.child("materias").child(materia.id)
                noMateria.removeValue()
            }

            // Adicionando o local atual na visualização da lista
            binding.container.addView(card.root)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inicializando banco de dados
        database = Firebase.database.reference

        // Recuperando fragmento que pode ser aberto no clique do botão "Adicionar a Materia"
        binding = FragmentMateriaBinding.inflate(layoutInflater)

        // Abrindo tela de cadastro de materias
        binding.buttonMatch.setOnClickListener {
            val frag = AddMateriaFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
        }

        configurarBase(inflater)

        return binding.root
    }
}
