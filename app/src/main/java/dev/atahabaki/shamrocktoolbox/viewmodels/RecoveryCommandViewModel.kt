package dev.atahabaki.shamrocktoolbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.atahabaki.shamrocktoolbox.models.Command

class RecoveryCommandViewModel: ViewModel() {
    private val mutableCommands = MutableLiveData<List<Command>>()
    val commands: LiveData<List<Command>> get() = mutableCommands

    fun addCommand(command: Command) {
        commands.value!!.plus(command)
    }

    fun delAllCommands() {
        for (i in commands.value!!.indices) {
            commands.value!!.drop(i)
        }
    }

    fun removeCommand(index: Int) {
        commands.value!!.drop(index)
    }
}