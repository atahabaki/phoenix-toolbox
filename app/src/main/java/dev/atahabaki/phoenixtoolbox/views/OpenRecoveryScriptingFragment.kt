package dev.atahabaki.phoenixtoolbox.views

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
import dev.atahabaki.phoenixtoolbox.R
import dev.atahabaki.phoenixtoolbox.adapters.RecoveryCommandsAdapter
import dev.atahabaki.phoenixtoolbox.databinding.FragmentOpenRecoveryBinding
import dev.atahabaki.phoenixtoolbox.execRoot
import dev.atahabaki.phoenixtoolbox.models.Command
import dev.atahabaki.phoenixtoolbox.viewmodels.FabStateViewModel
import dev.atahabaki.phoenixtoolbox.viewmodels.RecoveryCommandViewModel
import dev.atahabaki.phoenixtoolbox.viewmodels.RecoveryMenuStateViewModel
import java.io.FileReader
import java.lang.Exception

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
        val commands = loadCommands()
        val adapter = RecoveryCommandsAdapter(listOf())
        recViewModel.setCommands(commands)
        adapter.setCommands(commands)
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

    private fun loadCommands(): MutableList<Command> {
        val from = "/cache/recovery/command"
        val to = "${activity?.cacheDir?.absolutePath}/command"
        execRoot("su -c '[ -e $from ] && cp $from $to || rm $to'", "${activity?.packageName}.applyCommands")
        val commands = mutableListOf<Command>()
        try {
            val reader = FileReader(to)
            reader.forEachLine {
                val line = it.trim()
                if (line != "boot-recovery" && line.startsWith("--update_package=")) {
                    val param = line.substring(17)
                    commands.add(Command(command = "install", parameters = listOf(param)))
                }
                else if (line != "boot-recovery"){
                    val seperated = line.split(" ")
                    if (seperated.size > 1) {
                        val params = seperated.subList(1,seperated.size-1)
                        commands.add(Command(seperated[0], params))
                    }
                    else {
                        commands.add(Command(seperated[0], listOf()))
                    }
                }
            }
        }
        catch (e: Exception) {

        }
        return commands
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
