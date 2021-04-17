package dev.atahabaki.phoenixtoolbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FabStateViewModel(): ViewModel() {
    private val _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean> get() = _isVisible

    fun setVisibility(isVisible: Boolean) {
        _isVisible.value = isVisible
    }

    private val _isClicked = MutableLiveData<Boolean>()
    val isClicked: LiveData<Boolean> get() = _isClicked

    fun setClickState(isClicked: Boolean) {
        _isClicked.value = isClicked
    }
}