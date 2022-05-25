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
    lateinit var binding: FragmentMatchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMatchBinding.inflate(layoutInflater)
        database = Firebase.database.reference

        binding.buttonMatch.setOnClickListener {
            val frag = AddMatchFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()

        }
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        configurarBase()
    }

     private fun handleData(snapshot: DataSnapshot) {
         val itemList = arrayListOf<Encontro>()
         snapshot.children.forEach {
                val match = it.getValue(Encontro::class.java)
                match?.let {
                    itemList.add(it)
                }

         }
         refreshUi(itemList)
     }

     fun refreshUi(list: List<Encontro>) {
         binding.container.removeAllViews()
         list.forEach() {
             val card = CardNoteBinding.inflate(layoutInflater)

             card.textTitleMateria.text = it.materia
             card.textTitleData.text = it.data
             card.textTitleLocal.text = it.local
             card.textTitleUsuarioMatch.text = it.usuario
             binding.container.addView(card.root)
         }
     }
     fun configurarBase(){

         val usuario = FirebaseAuth.getInstance().currentUser

         if(usuario != null){
             database = FirebaseDatabase.getInstance().reference.child(usuario.uid)
         }

         var databaseListener = object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 handleData(snapshot)
             }

             override fun onCancelled(error: DatabaseError) {
                 Log.w("MainActivity", "onCancelled", error.toException())
             }

         }

         database.child("match").addValueEventListener(databaseListener)


     }


}