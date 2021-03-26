package dev.atahabaki.shamrocktoolbox.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.adapters.RecoveryCommandsAdapter
import dev.atahabaki.shamrocktoolbox.databinding.FragmentOpenRecoveryBinding
import dev.atahabaki.shamrocktoolbox.models.Command

class OpenRecoveryScriptingFragment: Fragment(R.layout.fragment_open_recovery) {
    private var _binding: FragmentOpenRecoveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecoveryCommandsAdapter(listOf(
            Command("install", listOf("/sdcard/flash.zip")),
            Command("wipe", listOf("cache"))
        ))
        binding.openRecoveryRecycler.layoutManager = LinearLayoutManager(activity)
        binding.openRecoveryRecycler.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
