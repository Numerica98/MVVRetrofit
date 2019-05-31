package com.petrlr14.mvvm.database.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.petrlr14.mvvm.database.RoomDB
import com.petrlr14.mvvm.database.entities.GitHubRepo
import com.petrlr14.mvvm.database.repositories.GitHubRepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitHubRepoViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository: GitHubRepoRepository

    init {
        val repoDao=RoomDB.getInstance(app).repoDao()
        repository= GitHubRepoRepository(repoDao)
    }

    private suspend fun insert(repo:GitHubRepo)=repository.insert(repo)

    fun getAll():LiveData<List<GitHubRepo>>{
        return repository.getAll()
    }

    private suspend fun nuke()= repository.nuke()

    //funcion original antes de pasarla a repository, lo cual no se hizo. Asi que queda normal
    fun retrieveRepos(user: String)= viewModelScope.launch{
        this@GitHubRepoViewModel.nuke()

        //Obtener la respuesta
        val response = repository.retrieveReposAsync(user).await() //.await Devuelve un response de la lista de githubrepos

        if (response.isSuccessful) with(response){
            //with devuelve un contexto de esa variable
            this.body()?.forEach{
                this@GitHubRepoViewModel.insert(it)
            }
        }else with(response){
            //when es similar a switch
            when(this.code()){
                404 ->{
                    Toast.makeText(app, "User not found", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}