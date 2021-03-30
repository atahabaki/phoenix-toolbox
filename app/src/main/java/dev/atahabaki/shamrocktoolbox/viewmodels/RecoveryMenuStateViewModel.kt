package dev.atahabaki.shamrocktoolbox.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecoveryMenuStateViewModel: ViewModel() {
    private val _isMenuActive = MutableLiveData<Boolean>()
    val isMenuActive get() = _isMenuActive

    fun setState(isActive: Boolean) {
        _isMenuActive.value = isActive
    }
}