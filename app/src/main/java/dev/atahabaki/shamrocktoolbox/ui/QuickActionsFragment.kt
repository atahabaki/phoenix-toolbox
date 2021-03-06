package dev.atahabaki.shamrocktoolbox.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.databinding.FragmentQuickActionsBinding
import dev.atahabaki.shamrocktoolbox.execRoot
import java.io.BufferedReader
import java.io.InputStreamReader

class QuickActionsFragment : Fragment(R.layout.fragment_quick_actions) {
    private var _binding: FragmentQuickActionsBinding? = null
    private val binding get() = _binding!!

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

    fun toggleGcam() {
        val prop = "persist.camera.HAL3.enabled"
        try {
            val p = java.lang.Runtime.getRuntime().exec("getprop $prop")
            p.waitFor()
            val stdOut = BufferedReader(InputStreamReader(p.inputStream))
            val line = stdOut.readLine()
            if (line.trim() == "1") {
                execRoot("setprop $prop 0", "${activity?.packageName}.setProp")
            } else {
                execRoot("setprop $prop 1", "${activity?.packageName}.setProp")
            }
        } catch (e: Exception) {
            Log.d("${activity?.packageName}.toggleGcam", "${e.message}")
        }
    }
}