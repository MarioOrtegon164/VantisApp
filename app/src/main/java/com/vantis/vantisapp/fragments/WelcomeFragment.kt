package com.vantis.vantisapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.vantis.vantisapp.R
import com.vantis.vantisapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {


    lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Se oculta la toolbar
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding.btnShowHeroesList.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.listHeroesFragment2)
        }
    }
}