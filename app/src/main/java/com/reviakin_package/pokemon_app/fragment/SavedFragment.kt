package com.reviakin_package.pokemon_app.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reviakin_package.pokemon_app.R
import com.reviakin_package.pokemon_app.adapter.PokemonRecycler
import com.reviakin_package.pokemon_app.app.App
import com.reviakin_package.pokemon_app.component.AppComponent
import com.reviakin_package.pokemon_app.database.entity.PokemonEntity
import com.reviakin_package.pokemon_app.fragment.callbacks.FragmentCallback
import com.reviakin_package.pokemon_app.mvvm.viewmodel.FindViewModel
import com.reviakin_package.pokemon_app.mvvm.viewmodel.SavedViewModel
import com.squareup.picasso.Picasso
import java.io.File
import java.lang.ClassCastException

class SavedFragment : Fragment(), Observer<List<PokemonEntity>>, PokemonRecycler.onLikeCLickListener, View.OnClickListener{

    private lateinit var viewModel : SavedViewModel
    private lateinit var mPokemonList : RecyclerView
    private lateinit var mBtnBack: Button
    private lateinit var mMainView: View


    private lateinit var mAppComponent: AppComponent

    private lateinit var mNavigationListener: FragmentCallback.OnNavigateFromSavedFragment

    companion object {
        fun newInstance() = SavedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mMainView = inflater.inflate(R.layout.saved_fragment, container, false)
        return mMainView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mNavigationListener = context as FragmentCallback.OnNavigateFromSavedFragment
        }catch (e: ClassCastException){
            throw ClassCastException(activity.toString() + " must implement OnNavigateFromSavedFragment");
        }
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init(){
        mBtnBack = mMainView.findViewById(R.id.btn_back)
        mPokemonList = mMainView.findViewById(R.id.pokemon_list)
        mPokemonList.layoutManager = LinearLayoutManager(requireActivity())

        mAppComponent = (requireActivity().applicationContext as App).appComponent

        mBtnBack.setOnClickListener(this)

        viewModel = mAppComponent.getSavedViewModel()
        viewModel.dataSave.observe(this, this)
    }

    override fun onClick(v: View?) {
        mNavigationListener.onSavedFragmentBackClick()
    }

    override fun onChanged(t: List<PokemonEntity>?) {
        if(t != null){
            mPokemonList.adapter = PokemonRecyclerAdapter(t, this)
        }
    }

    override fun likeClick(pokemonEntity: PokemonEntity) {
        viewModel.deletePokemon(pokemonEntity.name)
    }

    class PokemonRecyclerAdapter(
        private val pokemons: List<PokemonEntity>,
        private val likeListener: PokemonRecycler.onLikeCLickListener
        ) :
        RecyclerView.Adapter<PokemonRecyclerAdapter.PokemonViewHolder>() {

        class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            var iconPokemon: ImageView
            var namePokemon: TextView
            var btnLiked: ImageButton
            var tvHeight: TextView
            var tvWeight: TextView
            var tvBaseExp: TextView

            init {
                iconPokemon = itemView.findViewById(R.id.icon_item)
                namePokemon = itemView.findViewById(R.id.name_item)
                btnLiked = itemView.findViewById(R.id.btn_item)
                tvHeight = itemView.findViewById(R.id.tv_height)
                tvWeight = itemView.findViewById(R.id.tv_weight)
                tvBaseExp = itemView.findViewById(R.id.tv_base_exp)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
            var itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list, parent, false)
            return PokemonViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
            Picasso.get().load(File(pokemons[position].icon)).into(holder.iconPokemon)
            holder.namePokemon.text = pokemons[position].name
            holder.tvHeight.text = pokemons[position].height.toString()
            holder.tvWeight.text = pokemons[position].weight.toString()
            holder.tvBaseExp.text = pokemons[position].baseExperience.toString()
            holder.btnLiked.setOnClickListener { likeListener.likeClick(pokemons[position]) }

        }

        override fun getItemCount() = pokemons.size

    }

}