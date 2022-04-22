package com.vantis.vantisapp.holders

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vantis.vantisapp.R
import com.vantis.vantisapp.databinding.HeroRecyclerViewModelBinding
import com.vantis.vantisapp.models.Hero
import jp.wasabeef.picasso.transformations.CropCircleTransformation


/**
 * Created by LTI.Mario Josue Ortegon Chan on 4/8/2022.
 */
class HeroViewHolder (view: View): RecyclerView.ViewHolder(view) {

    private val binding = HeroRecyclerViewModelBinding.bind(view)

    fun bind(hero: Hero, context: Context?, viewFragment: View){

//        Log.d("HeroViewHolder",hero.toString())

        Picasso.get().load(hero.image.url) .resize(220, 220)
            .transform(CropCircleTransformation())
            .error(R.drawable.no_image)
            .into(binding.ivHeroImage)

        binding.tvHeroName.text = hero.name

        binding.cvHeroe.setOnClickListener {
            Log.d("HeroViewHolder",hero.id)
            Log.d("HeroViewHolder",hero.name)

            val bundle = Bundle()
            bundle.putString("id", hero.id)
            //bundle.putSerializable("USER", user) // Serializable Object
            Navigation.findNavController(viewFragment).navigate(R.id.infoHeroFragment,bundle)
        }

    }
}