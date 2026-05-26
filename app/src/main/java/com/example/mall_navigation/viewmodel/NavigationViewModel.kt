package com.example.mall_navigation.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mall_navigation.models.Point
import com.example.mall_navigation.models.RouteRequest
import com.example.mall_navigation.models.RouteResponse
import com.example.mall_navigation.models.Shop
import com.example.mall_navigation.network.RetrofitClient
import kotlinx.coroutines.launch

class NavigationViewModel : ViewModel() {
    private val api = RetrofitClient.api

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    private val _route = MutableLiveData<RouteResponse?>()
    val route: LiveData<RouteResponse?> = _route

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadShops() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _shops.value = api.getAllShops()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadShopsByFloor(floor: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _shops.value = api.getShopsByFloor(floor)
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchShops(query: String) {
        if (query.isEmpty()) {
            loadShops()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _shops.value = api.searchShops(query)
            } catch (e: Exception) {
                _error.value = "Ошибка поиска: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRoute(start: Point, end: Point) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = RouteRequest(start.x, start.y, end.x, end.y, start.floor)
                _route.value = api.getRoute(request)
            } catch (e: Exception) {
                _error.value = "Ошибка маршрута: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearRoute() {
        _route.value = null
    }
}