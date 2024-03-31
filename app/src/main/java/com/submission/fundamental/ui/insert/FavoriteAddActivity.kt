package com.submission.fundamental.ui.insert

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.fundamental.data.response.ItemsItem
import com.submission.fundamental.databinding.ActivityFavoriteAddBinding
import com.submission.fundamental.ui.adapter.ItemAdapter
import com.submission.fundamental.ui.ViewModelFactory

class FavoriteAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteAddBinding

    private val favoriteViewModel by viewModels<FavoriteAddViewModel>() {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteAddBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteUser.addItemDecoration(itemDecoration)

        favoriteViewModel.getAllFavoriteUser().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            setGithubUser(items)
        }
        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setGithubUser(githubUser: List<ItemsItem>) {
        val adapter = ItemAdapter()
        adapter.submitList(githubUser)
        binding.rvFavoriteUser.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}