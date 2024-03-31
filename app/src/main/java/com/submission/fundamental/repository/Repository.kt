//package com.example.githubusers.data
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import com.submission.fundamental.data.response.DetailUserResponse
//import com.submission.fundamental.data.response.FavRespons
//import com.submission.fundamental.data.response.GithubResponse
//import com.submission.fundamental.data.response.ItemsItem
//import com.submission.fundamental.data.retrofit.ApiService
//import com.submission.fundamental.database.FavoriteUsers
//import com.submission.fundamental.database.FavoriteUsersDao
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//class Repository private constructor(
//    private val baseApi: ApiService,
//    private val daoUser: FavoriteUsersDao
//) {
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val detailCache: MutableMap<String, MutableLiveData<DetailUserResponse>> = mutableMapOf()
//
//    private val followerCache: MutableMap<String, MutableLiveData<List<FavRespons>>> = mutableMapOf()
//    private val followingCache: MutableMap<String, MutableLiveData<List<FavRespons>>> = mutableMapOf()
//
//    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
//
//    fun getAllFavorite(): LiveData<List<FavoriteUsers>> = daoUser.getAllFavorite()
//
//    fun getFavUserByUsername(username: String): LiveData<FavoriteUsers> =
//        daoUser.getFavUserByUsername(username)
//    fun insertUsers(gitUser: FavoriteUsers) = executorService.execute {
//        daoUser.insert(gitUser)
//    }
//    fun deleteUsers(gitUser: FavoriteUsers) = executorService.execute {
//        daoUser.delete(gitUser)
//    }
//
//    fun getListUsers(query: String): LiveData<List<ItemsItem>> {
//        _isLoading.value = true
//
//        val listUsers = MutableLiveData<List<ItemsItem>>()
//        val user = baseApi.searchUsers(query)
//
//        user.enqueue(object : Callback<GithubResponse> {
//            override fun onResponse(
//                call: Call<GithubResponse>,
//                response: Response<GithubResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    listUsers.value = response.body()!!.items as List<ItemsItem>?
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
//                _isLoading.value = false
//                t.message
//            }
//        })
//        return listUsers
//    }
//
//
//    fun getDetailUser(username: String): LiveData<DetailUserResponse> {
//        val liveData = MutableLiveData<DetailUserResponse>()
//
//
//        if (detailCache.containsKey(username)) {
//            return detailCache[username]!!
//        }
//
//        _isLoading.postValue(true)
//
//        baseApi.getDetailUser(username).enqueue(object : Callback<DetailUserResponse> {
//            override fun onResponse(
//                call: Call<DetailUserResponse>,
//                response: Response<DetailUserResponse>
//            ) {
//                _isLoading.postValue(false)
//                if (response.isSuccessful) {
//                    val detailResponse = response.body()
//                    detailResponse?.let {
//                        val data = MutableLiveData<DetailUserResponse>()
//                        data.value = it
//                        detailCache[username] = data
//                        liveData.postValue(it)
//                    }
//                } else {
//                    Log.e(TAG, "onResponse: ${response.code()} ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
//                _isLoading.postValue(false)
//                Log.e(TAG, "onFailure: ${t.message}", t)
//            }
//        })
//
//        return liveData
//    }
//
//    fun getFollowerUser(username: String): LiveData<List<FavRespons>> {
//        followerCache.remove(username)
//
//        val liveData = MutableLiveData<List<FavRespons>>()
//
//        _isLoading.postValue(true)
//        baseApi.getFollowers(username).enqueue(object : Callback<List<FavRespons>> {
//            override fun onResponse(
//                call: Call<List<FavRespons>>,
//                response: Response<List<FavRespons>>
//            ) {
//                _isLoading.postValue(false)
//                if (response.isSuccessful) {
//                    val followerList = response.body() ?: emptyList()
//                    followerCache[username] = MutableLiveData(followerList)
//                    liveData.postValue(followerList)
//                } else {
//                    Log.e(TAG, "onResponse: ${response.code()} ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<FavRespons>>, t: Throwable) {
//                _isLoading.postValue(false)
//                Log.e(TAG, "onFailure: ${t.message}", t)
//            }
//        })
//
//        return liveData
//    }
//
//    fun getFollowingUser(username: String): LiveData<List<FavRespons>> {
//        followingCache.remove(username)
//
//        val liveData = MutableLiveData<List<FavRespons>>()
//
//        _isLoading.postValue(true)
//        baseApi.getFollowing(username).enqueue(object : Callback<List<FavRespons>> {
//            override fun onResponse(
//                call: Call<List<FavRespons>>,
//                response: Response<List<FavRespons>>
//            ) {
//                _isLoading.postValue(false)
//                if (response.isSuccessful) {
//                    val followingList = response.body() ?: emptyList()
//                    followingCache[username] = MutableLiveData(followingList)
//                    liveData.postValue(followingList)
//                } else {
//                    Log.e(TAG, "onResponse: ${response.code()} ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<FavRespons>>, t: Throwable) {
//                _isLoading.postValue(false)
//                Log.e(TAG, "onFailure: ${t.message}", t)
//            }
//        })
//
//        return liveData
//    }
//
//    companion object {
//        private const val TAG = "MainViewModel"
//
//        @Volatile
//        var INSTANCE: Repository? = null
//        fun getInstance(
//            apiService: ApiService,
//            daoUser: FavoriteUsersDao
//        ): Repository =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Repository(apiService,daoUser)
//            }.also { INSTANCE = it }
//    }
//}