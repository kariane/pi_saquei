package br.senac.pi.tabs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.senac.pi.R
import br.senac.pi.databinding.ActivityTabBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabActivity : AppCompatActivity() {
    lateinit var binding: ActivityTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = TabPageAdapter(this)

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { aba, posicao ->
            when(posicao) {
                1 -> aba.icon = getDrawable(R.drawable.person)
                2 -> aba.icon = getDrawable(R.drawable.album)
                else -> aba.icon = getDrawable(R.drawable.history)
            }
        }.attach()
    }
}