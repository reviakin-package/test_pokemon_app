package com.reviakin_package.pokemon_app.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reviakin_package.pokemon_app.database.entity.PokemonEntity
import com.reviakin_package.pokemon_app.helper.LoadingState
import com.reviakin_package.pokemon_app.mvvm.PokemonRepository
import com.reviakin_package.pokemon_app.pojo.PokemonUnit
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FindViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _loadingState = MutableLiveData<LoadingState>()

    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    val dataExist = repository.dataCurrentExist

    val dataLastFind = repository.dataLastFind

    fun fetchFindData(name: String){
        viewModelScope.launch {
            try{
                _loadingState.value = LoadingState.LOADING
                repository.refreshFindData(name)
                _loadingState.value = LoadingState.LOADED
            }catch (e: HttpException){
                _loadingState.value = LoadingState.error(e.stackTraceToString())
            }
        }
    }

    fun savePokemon(pokemonEntity: PokemonEntity){
        viewModelScope.launch {
            repository.addPokemonDataEntity(pokemonEntity)
        }
    }

    fun deletePokemon(name: String){
        viewModelScope.launch {
            repository.deletePokemonData(name)
        }
    }

    fun cleanCache(){
        viewModelScope.launch {
            repository.cleanCache()
        }
    }
}