package com.zaghy.githubuser.ui.detailuser


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaghy.githubuser.data.local.entity.FavoriteUser
import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.data.remote.response.UserDetailResponse
import com.zaghy.githubuser.data.repository.GithubUserRepository
import kotlinx.coroutines.launch

class DetailUserViewModel(private val githubUserRepository: GithubUserRepository) : ViewModel() {

    private val _detailUser = MutableLiveData<UserDetailResponse>()
    val detailUser  = _detailUser

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following = _following

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite = _isFavorite

    private val _isActivated = MutableLiveData<Boolean>()
    val isActivated = _isActivated

    companion object {
        private const val TAG = "detail_user_view_model"
    }

    fun setFavorite(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
    }

    fun setActivated(isActivated: Boolean) {
        _isActivated.value = isActivated
    }

    fun setDetailUser(data:UserDetailResponse){
        _detailUser.value = data
    }
    fun setFollowers(data:List<ItemsItem>){
        _followers.value = data
    }
    fun setFollowing(data:List<ItemsItem>){
        _following.value = data
    }

    fun getDetailUser(username: String) = githubUserRepository.findDetailUserGithub(username)
    fun getFollowers(username: String) = githubUserRepository.getFollowers(username)
    fun getFollowing(username: String) = githubUserRepository.getFollowing(username)

    fun addFavoriteUser(data: FavoriteUser) {
        viewModelScope.launch {
            githubUserRepository.addFavoriteGithubUser(data)
        }
    }

    fun deleteFavoriteUser(data: FavoriteUser) {
        viewModelScope.launch {
            githubUserRepository.deleteFavoriteUser(data)
        }
    }

    fun checkFavoriteUser(username: String) =
        githubUserRepository.getFavoriteGithubUserByUsername(username)


}