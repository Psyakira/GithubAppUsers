package com.submission.fundamental.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.fundamental.data.response.DetailUserResponse
import com.submission.fundamental.data.retrofit.ApiConfig
import com.submission.fundamental.database.FavoriteUser
import com.submission.fundamental.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (application: Application) : ViewModel() {

    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteUserRepository : FavoriteUserRepository = FavoriteUserRepository(application)

    companion object{
        private const val TAG = "DetailViewModel"
    }

    init {
        findDetailGithubUser(DetailUserActivity.username)
    }

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun getFavoriteUser(username: String): LiveData<FavoriteUser> = mFavoriteUserRepository.getFavoriteByUsername(username)

    fun findDetailGithubUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userDetail.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
}