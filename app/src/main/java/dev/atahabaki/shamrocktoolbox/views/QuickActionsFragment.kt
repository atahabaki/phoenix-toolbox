package dev.atahabaki.shamrocktoolbox.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.databinding.FragmentQuickActionsBinding
import dev.atahabaki.shamrocktoolbox.execRoot
import dev.atahabaki.shamrocktoolbox.viewmodels.ToggleGcamViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

class QuickActionsFragment : Fragment(R.layout.fragment_quick_actions) {
    private var _binding: FragmentQuickActionsBinding? = null
    private val binding get() = _binding!!
    private val gcamProp = "persist.camera.HAL3.enabled"

    private val viewModel: ToggleGcamViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuickActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quickAccessToggleGcam.setOnClickListener {
            toggleGcam()
        }
    }

    private fun getGcamStatus(): Boolean {
        try {
            val p = java.lang.Runtime.getRuntime().exec("getprop $gcamProp")
            p.waitFor()
            val stdOut = BufferedReader(InputStreamReader(p.inputStream))
            val line = stdOut.readLine().trim()
            return line == "1"
        } catch (e: Exception) {
            Log.d("${activity?.packageName}.toggleGcam", "${e.message}")
        }
    }

    fun toggleGcam() {
        if (getGcamStatus()) {
            disableGcam()
        } else {
            enableGcam()
        }
    }

    fun disableGcam() {
        execRoot("setprop $gcamProp 0", "${activity?.packageName}.setProp")
        viewModel.selectGcamState(false)
    }

    fun enableGcam() {
        execRoot("setprop $gcamProp 1", "${activity?.packageName}.setProp")
        viewModel.selectGcamState(true)
    }
}