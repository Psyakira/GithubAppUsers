package com.submission.fundamental.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.fundamental.data.response.GithubResponse
import com.submission.fundamental.data.retrofit.ApiConfig
import com.submission.fundamental.data.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listGithubUser = MutableLiveData<List<ItemsItem?>?>()
    val listGithubUser: LiveData<List<ItemsItem?>?> = _listGithubUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
        private const val DEFAULT_USERNAME = "Syakira"
    }

    init {
        findGithubUser(DEFAULT_USERNAME)
    }

    fun findGithubUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listGithubUser.value = response.body()?.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}