package dev.atahabaki.shamrocktoolbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FabStateViewModel(): ViewModel() {
    private val _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean> get() = _isVisible

    fun setVisibility(isVisible: Boolean) {
        _isVisible.value = isVisible
    }
}