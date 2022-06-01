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
    lateinit var binding: FragmentMateriaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMateriaBinding.inflate(layoutInflater)
        database = Firebase.database.reference

        binding.buttonMatch.setOnClickListener {
            val frag = AddMateriaFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()

        }
        configurarBase(inflater)
        return binding.root
    }

     private fun handleData(snapshot: DataSnapshot, inflater: LayoutInflater) {
         val itemList = arrayListOf<Materia>()
         snapshot.children.forEach {
             val materia = it.getValue(Materia::class.java)
             materia?.let {
                 itemList.add(it)
             }

         }
         refreshUi(itemList, inflater)
     }

     fun refreshUi(list: List<Materia>, inflater: LayoutInflater) {
         binding.container.removeAllViews()
         list.forEach() {
             val card = CardNoteMateriaBinding.inflate(inflater)

             card.textTitleNome.text = it.nome

            card.imageDelete.setOnClickListener { imageView ->
                 val noMateria = database.child("materias").child(it.id)
                 noMateria.removeValue()
             }

             binding.container.addView(card.root)
         }
     }

     fun configurarBase(inflater: LayoutInflater){

         val usuario = FirebaseAuth.getInstance().currentUser

         if(usuario != null){
             database = FirebaseDatabase.getInstance().reference.child("configuracoes")
         }

         var databaseListener = object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 handleData(snapshot, inflater)
             }

             override fun onCancelled(error: DatabaseError) {
                 Log.w("MainActivity", "onCancelled", error.toException())
             }
         }

         database.child("materias").addValueEventListener(databaseListener)
     }
}
