package br.senac.pi.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.senac.pi.databinding.FragmentAddMatchBinding
import br.senac.pi.databinding.FragmentMateriaBinding
import br.senac.pi.model.Encontro
import br.senac.pi.model.Local
import br.senac.pi.model.Materia
import br.senac.pi.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class MateriaFragment : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var binding: FragmentMateriaBinding
    var spinner = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = FragmentMateriaBinding.inflate(layoutInflater)
        database = Firebase.database.reference
        configurarBase()


        //botao continuar
        binding.buttonAdicionar.setOnClickListener {
            val usuario = FirebaseAuth.getInstance().currentUser
            if (usuario != null) {
                database = FirebaseDatabase.getInstance().reference.child(usuario.uid)
            }
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

    private fun handleData(snapshot: DataSnapshot) {


        if(spinner=="materias"){
            val itemList = arrayListOf<Materia>()
            snapshot.children.forEach {
                val match = it.getValue(Materia::class.java)
                match?.let {
                    itemList.add(it)
                }
                refreshUiMateria(itemList)
            }
        }else if(spinner=="locais"){
            val itemList = arrayListOf<Local>()
            snapshot.children.forEach {
                val match = it.getValue(Local::class.java)
                match?.let {
                    itemList.add(it)
                }
                refreshUiLocal(itemList)
            }

        } else if(spinner=="usuarios"){
            val itemList = arrayListOf<Usuario>()
            snapshot.children.forEach {
                val match = it.getValue(Usuario::class.java)
                match?.let {
                    itemList.add(it)
                }
                refreshUiUsuario(itemList)
            }
        }


    }

    fun refreshUiMateria(list: List<Materia>) {
        val itensSpinner = arrayOfNulls<String>(list.size+1)
        var i = 0
        itensSpinner.set(i,"Selecione")
        list.forEach(){
            i++
            itensSpinner.set(i,it.nome)
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



    }
    fun refreshUiLocal(list: List<Local>) {
        val itensSpinner = arrayOfNulls<String>(list.size+1)
        var i = 0
        itensSpinner.set(i,"Selecione")
        list.forEach(){
            i++
            itensSpinner.set(i,it.nome)
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

    }

    fun refreshUiUsuario(list: List<Usuario>) {
        val itensSpinner = arrayOfNulls<String>(list.size+1)
        var i = 0
        itensSpinner.set(i,"Selecione")
        list.forEach(){
            i++
            itensSpinner.set(i,it.nome)
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

    }


    fun configurarBase(){

        val usuario = FirebaseAuth.getInstance().currentUser


        if(usuario != null){
            database = FirebaseDatabase.getInstance().reference.child("configuracoes")
        }


        var databaseListenerSubject = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                spinner="materias"
                handleData(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "onCancelled", error.toException())
            }

        }

        var databaseListenerPlace = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                spinner="locais"
                handleData(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "onCancelled", error.toException())
            }

        }

        var databaseListenerUser = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                spinner="usuarios"
                handleData(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "onCancelled", error.toException())
            }

        }

        database.child("materias").addValueEventListener(databaseListenerSubject)
        database.child("locais").addValueEventListener(databaseListenerPlace)
        database.child("usuarios").addValueEventListener(databaseListenerUser)


    }

}


