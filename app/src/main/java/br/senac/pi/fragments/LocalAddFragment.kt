package br.senac.pi.fragments

// Importando todos os arquivos utilizados
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.senac.pi.R
import br.senac.pi.databinding.PlacesAddFragmentBinding
import br.senac.pi.model.Local
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LocalAddFragment: Fragment() {
    private lateinit var database: DatabaseReference
    lateinit var binding: PlacesAddFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PlacesAddFragmentBinding.inflate(layoutInflater) // Acionando o layout

        // Identificando qual banco de dados estamos utilizando (no caso, o Firebase)
        database = Firebase.database.reference

        // Adicionando listener no botão de adicionar locais para enviar os dados do formulário para o Firebase
        binding.btnAdd.setOnClickListener {
            val place = Local(
                surname = binding.edtSurname.text.toString(),
                address = binding.edtAdress.text.toString(),
                CEP = binding.edtCEP.text.toString(),
                provincy = binding.edtProvincy.text.toString(),
            )

            //Criando novo documento para salvar as informações preenchidas no formulário
            val newPlace = database.child("places").push()

            //Criando novo id
            newPlace.key?.let {
                place.id = it
            }

            //Inserindo os valores da variavel place que setamos acima
            newPlace.setValue(place)

            //Mostrando na tela a mensagem "Local adicionado"
            Toast.makeText(activity,"Local adicionado!", Toast.LENGTH_LONG).show()

            //retornando a tela de listagem dos locais automaticamente
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, LocalFragment()).commit()
        }

        return binding.root
    }
}