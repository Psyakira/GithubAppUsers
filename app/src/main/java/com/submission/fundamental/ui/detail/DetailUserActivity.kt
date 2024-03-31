package com.submission.fundamental.ui.detail

import android.os.Bundle
import android.view.MenuItem

import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.submission.fundamental.R
import com.submission.fundamental.data.response.DetailUserResponse
import com.submission.fundamental.database.FavoriteUser
import com.submission.fundamental.databinding.ActivityDetailUserBinding
import com.submission.fundamental.ui.ViewModelFactory
import com.submission.fundamental.ui.adapter.SectionPagerAdapter

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    private val detailViewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val username = "username"
        const val avatarURL = "url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val usernameData = intent.getStringExtra(username)
        val avatarUrlData = intent.getStringExtra(avatarURL)

        if (usernameData != null) {
            detailViewModel.findDetailGithubUser(usernameData)
        }
        detailViewModel.userDetail.observe(this) { userDetail ->
            setData(userDetail, usernameData!!, avatarUrlData)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        if (usernameData != null) {
            sectionsPagerAdapter.username = usernameData
        }
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.getFavoriteUser(usernameData!!).observe(this) { user ->
            if (user == null) {
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.floatingActionButton.context,
                        R.drawable.ic_favorite_border
                    )
                )
                binding.floatingActionButton.setOnClickListener {
                    detailViewModel.insertFavoriteUser(FavoriteUser(usernameData, avatarUrlData))
                }
            } else {
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.floatingActionButton.context,
                        R.drawable.ic_favorite
                    )
                )
                binding.floatingActionButton.setOnClickListener {
                    detailViewModel.deleteFavoriteUser(user)
                }
            }
        }
    }

    private fun setData(user: DetailUserResponse, usernameData: String, avatarUrlData: String?) {
        Glide.with(this).load(avatarUrlData).into(binding.imgAva)
        binding.tvName.text = usernameData
        binding.tvUser.text = user.login
        binding.tvBio.text = user.bio as CharSequence?
        binding.tvLocation.text = user.location
        val newFollowers = this.resources.getString(R.string.followers, user.followers)
        val newFollowing = this.resources.getString(R.string.following, user.following)
        binding.tvFollowers.text = newFollowers
        binding.tvFollowing.text = newFollowing
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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