package br.senac.pi.fragments

// Importando todos os arquivos utilizados
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.senac.pi.R
import br.senac.pi.databinding.PlacesListFragmentBinding
import br.senac.pi.databinding.PlacesCardBinding
import br.senac.pi.model.Local
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LocalFragment: Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: PlacesListFragmentBinding

    // Função criada para recuperar os dados do banco e colocá-los em uma lista de modo que possamos mandá-lo ao front-end
    fun handleData(snapshot: DataSnapshot, inflater: LayoutInflater) {
        // Inicializando uma variável vazia com uma lista de locais
        val itemList = arrayListOf<Local>()

        // Montando um item list com todos os locais
        snapshot.children.forEach { place ->
            val foundedPlaces = place.getValue(Local::class.java)

            foundedPlaces?.let { p ->
                itemList.add(p)
            }
        }

        refreshUi(itemList, inflater)
    }

    // Função criada para atualizar a tela de listagem
    fun refreshUi(list: List<Local>, inflater: LayoutInflater) {
        // Removendo todos os itens listagem de locais
        binding.container.removeAllViews()

        // Recebendo a lista com os locais e adicionamento novamente na tela
        list.forEach() { place ->
            val card = PlacesCardBinding.inflate(inflater)

            // Adicionando texto de cada campo do Firebase na tela
            card.surname.text = place.surname // Adicionado parametro (apelido)
            card.address.text = place.address // Adicionado parametro (endereço)
            card.CEP.text = place.CEP         // Adicionado parametro (cep)
            card.provincy.text = place.provincy // Adicionado parametro (text)

            // Adicionando listener no ícone de deletar local
            card.imageDelete.setOnClickListener {
                val id = database.child("places").child(place.id)
                id.removeValue()
            }

            // Adicionando o local atual na visualização da lista
            binding.container.addView(card.root)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inicializando banco de dados
        database = Firebase.database.reference

        // Recuperando fragmento que pode ser aberto no clique do botão "Adicionar o Local"
        binding = PlacesListFragmentBinding.inflate(layoutInflater)

        // Abrindo tela de cadastro dos locais
        binding.btnPlaces.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, LocalAddFragment()).commit()
        }

        databaseConf(inflater)

        return binding.root
    }

    // Função criada para se conectar ao Firebase, trazer os dados que preciso (neste caso, os locais)
    fun databaseConf(inflater: LayoutInflater) {
        // Montando listener para pegar todas as alterações do banco de dados em tempo real
        val databaseListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                handleData(snapshot, inflater)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "onCancelled", error.toException())
            }
        }

        // Buscando dados dos locais no Firebase
        database.child("places").addValueEventListener(databaseListener)
    }
}