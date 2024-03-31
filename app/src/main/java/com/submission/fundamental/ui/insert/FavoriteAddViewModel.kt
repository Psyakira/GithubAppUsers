package com.submission.fundamental.ui.insert

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.fundamental.database.FavoriteUser
import com.submission.fundamental.database.FavoriteUserDao
import com.submission.fundamental.repository.FavoriteUserRepository

class FavoriteAddViewModel(mApplication: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteUserRepository : FavoriteUserRepository = FavoriteUserRepository(mApplication)

    fun getAllFavoriteUser() : LiveData<List<FavoriteUser>> {
        _isLoading.value = false
        return mFavoriteUserRepository.getAllFavoriteUser()
    }
}