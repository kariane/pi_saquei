package br.senac.pi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.senac.pi.databinding.FragmentAddMateriaBinding
import br.senac.pi.model.Materia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddMateriaFragment : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var binding: FragmentAddMateriaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAddMateriaBinding.inflate(layoutInflater)

        // Identificando qual banco de dados estamos utilizando (no caso, o Firebase)
        database = Firebase.database.reference

        // Adicionando listener no botão de adicionar locais para enviar os dados do formulário para o Firebase
        binding.buttonAdicionar.setOnClickListener {
            val materia = Materia(nome = binding.editTextMateria.text.toString())

            val newMateria = database.child("materias").push()
            newMateria.key?.let {
                materia.id=it
            }
            newMateria.setValue(materia)
            Toast.makeText(activity,"Materia adicionado!", Toast.LENGTH_LONG).show()
        }
        return binding.root
    }
}


