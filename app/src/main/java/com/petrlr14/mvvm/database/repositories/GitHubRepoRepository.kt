package com.petrlr14.mvvm.database.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.petrlr14.mvvm.database.daos.GitHubDAO
import com.petrlr14.mvvm.database.entities.GitHubRepo
import com.petrlr14.mvvm.services.retrofit.GithubService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class GitHubRepoRepository (private val repoDao:GitHubDAO){

    //funcion que estaba antes de mover del viewmodel al repository
    fun retrieveReposAsync(user: String): Deferred<Response<List<GitHubRepo>>> =
        GithubService.getGithubServices().getAllResposPerUser(user)


    /*funcion que estaba supuestamente para mover aqui en el repository, pero faltaba completar
    fun retrieveRepos(user:String): Int{
        GlobalScope.launch(Dispatchers.IO) {
        this@GitHubRepoRepository.nuke()

        val response = GithubService.getGithubServices().getAllResposPerUser(user).await()

        if (response.isSuccessful) with(response) {
            //with devuelve un contexto de esa variable
            this.body()?.forEach {
                this@GitHubRepoRepository.insert(it)
            }
        }
    }*/
    @WorkerThread
    suspend fun insert(repo:GitHubRepo){
        repoDao.insert(repo)
    }

    fun getAll():LiveData<List<GitHubRepo>>{
        return repoDao.getAllRepos()
    }

    @WorkerThread
    suspend fun nuke(){
        return repoDao.nukeTable()
    }

}