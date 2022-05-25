package br.senac.pi.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import br.senac.pi.R
import br.senac.pi.databinding.FragmentAddMatchBinding
import br.senac.pi.model.Encontro

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class AddMatchFragment : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var binding: FragmentAddMatchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = FragmentAddMatchBinding.inflate(layoutInflater)
        database = Firebase.database.reference
        configurarBase()

        database.child("usuarios").get().addOnSuccessListener {

            val str = it.value.toString().replace("[", "").replace("]", "")
            val delim = ","
            var i = 0
            val list = str.split(delim)
            val itensSpinner = arrayOfNulls<String>(list.size)

            list.forEach {
                if(it!="null"){
                    itensSpinner.set(i, it)
                    i = i + 1
                }else {
                    itensSpinner.set(i, "Selecione")
                    i = i + 1
                }
            }

            val adapter = activity?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    itensSpinner
                )
            }

            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerUsuario.adapter = adapter

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        //Preencher spinner de materias
        database.child("materias").get().addOnSuccessListener {

            val str = it.value.toString().replace("[", "").replace("]", "")
            val delim = ","
            var i = 0
            val list = str.split(delim)
            val itensSpinner = arrayOfNulls<String>(list.size)

            list.forEach {
                if(it!="null"){
                    itensSpinner.set(i, it)
                    i = i + 1
                }else {
                    itensSpinner.set(i, "Selecione")
                    i = i + 1
                }
            }

            val adapter = activity?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    itensSpinner
                )
            }

            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerMateria.adapter = adapter

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }


        database.child("lugares").get().addOnSuccessListener {

            val str = it.value.toString().replace("[", "").replace("]", "")
            val delim = ","
            var i = 0
            val list = str.split(delim)
            val itensSpinner = arrayOfNulls<String>(list.size)

            list.forEach {
                if(it!="null"){
                    itensSpinner.set(i, it)
                    i = i + 1
                }else {
                    itensSpinner.set(i, "Selecione")
                    i = i + 1
                }

            }

            val adapter = activity?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    itensSpinner
                )
            }

            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerLocal.adapter = adapter

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        //botao continuar
        binding.buttonNext.setOnClickListener {
            val usuario = FirebaseAuth.getInstance().currentUser

            val match = Encontro(usuario = binding.spinnerUsuario.selectedItem.toString(),
                                 data = binding.editDate.text.toString(),
                                 local = binding.spinnerLocal.selectedItem.toString(),
                                 materia = binding.spinnerMateria.selectedItem.toString())

            val newMatch = database.child("match").push()
            newMatch.key?.let {
                match.id=it
            }
            newMatch.setValue(match)
            Toast.makeText(activity,"Encontro adicionado!", Toast.LENGTH_LONG).show()
        }
        return binding.root
    }

    fun configurarBase(){

        val usuario = FirebaseAuth.getInstance().currentUser

        if(usuario != null){
            database = FirebaseDatabase.getInstance().reference.child(usuario.uid)
        }



    }

}


