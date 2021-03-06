package br.senac.pi.bottomnavigation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.senac.pi.R
import br.senac.pi.databinding.ActivityBottomNavigationBinding
import br.senac.pi.fragments.LocalFragment
import br.senac.pi.fragments.MatchFragment
import br.senac.pi.fragments.MateriaFragment
import br.senac.pi.model.Usuario
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BottomNavigationActivity : AppCompatActivity() {

    lateinit var binding: ActivityBottomNavigationBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tratarLogin()

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.Match -> {
                    val frag = MatchFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
                }
                R.id.Lugares -> {
                    val frag = LocalFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
                }
                else -> {
                    val frag = MateriaFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()
                }
            }
            true
        }
    }

    fun tratarLogin(){

        if(FirebaseAuth.getInstance().currentUser != null){
            Toast.makeText(this, "Entrou", Toast.LENGTH_LONG).show()
            configurarBase()

        } else {
            //Cria um array list com os providers de autenticação
            //suportados
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

            //Solicita a abertura da atividade de autenticação
            //do Firebase auth ui
            var i =  AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build()
            startActivityForResult(i,1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            Toast.makeText(this,"Autenticado", Toast.LENGTH_LONG).show()
            configurarBase()
        } else {
            finishAffinity()
        }
    }

    fun configurarBase(){

        val usuario = FirebaseAuth.getInstance().currentUser

        if(usuario != null){
            database = FirebaseDatabase.getInstance().reference.child("configuracoes")

            val insertUser = Usuario(id = usuario.uid, nome = usuario.displayName.toString())

            val newUser = database.child("usuarios").child(usuario.uid)
            newUser.key?.let {
                insertUser.id=it
            }
            newUser.setValue(insertUser)

            database = FirebaseDatabase.getInstance().reference.child(usuario.uid)
        }
    }
}
