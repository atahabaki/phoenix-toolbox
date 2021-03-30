package dev.atahabaki.shamrocktoolbox.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.adapters.RecoveryCommandsAdapter
import dev.atahabaki.shamrocktoolbox.databinding.FragmentOpenRecoveryBinding
import dev.atahabaki.shamrocktoolbox.viewmodels.FabStateViewModel
import dev.atahabaki.shamrocktoolbox.viewmodels.RecoveryCommandViewModel
import dev.atahabaki.shamrocktoolbox.viewmodels.RecoveryMenuStateViewModel

class OpenRecoveryScriptingFragment: Fragment(R.layout.fragment_open_recovery) {
    private var _binding: FragmentOpenRecoveryBinding? = null
    private val binding get() = _binding!!

    private val fabViewModel: FabStateViewModel by activityViewModels()
    private val recViewModel: RecoveryCommandViewModel by activityViewModels()
    private val recMenuViewModel: RecoveryMenuStateViewModel by activityViewModels()

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
        val adapter = RecoveryCommandsAdapter(listOf()) //Read from /cache at startup but for now leave it like this...
        fabViewModel.setVisibility(true)
        binding.openRecoveryRecycler.layoutManager = LinearLayoutManager(activity)
        binding.openRecoveryRecycler.adapter = adapter
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                recViewModel.removeCommand(viewHolder.adapterPosition)
                adapter.notifyDataSetChanged()
            }
        }).attachToRecyclerView(binding.openRecoveryRecycler)
        recMenuViewModel.setState(true)
        recViewModel.isChanged.observe(viewLifecycleOwner, Observer {
            if (it) {
                adapter.notifyDataSetChanged()
                recViewModel.setDataChanged(false) // To reset normal...
            }
        })
        activity?.let {
            recViewModel.commands.observe(it, Observer{
                adapter.setCommands(it)
                adapter.notifyDataSetChanged()
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
