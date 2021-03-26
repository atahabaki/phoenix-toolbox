package dev.atahabaki.shamrocktoolbox.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.models.Command

class RecoveryCommandsAdapter(
    private var commands: List<Command>
): RecyclerView.Adapter<RecoveryCommandsAdapter.ViewHolder>() {

    private fun gen_exp(command: Command): String {
        return when (command.command.trim()) {
            "install" -> "flashes the zip file located on your device at <b>${command.parameters}</b>"
            else -> "I don't what this does, stay tuned..."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.command_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currCommand = commands[position]
        holder.itemView.findViewById<MaterialTextView>(R.id.command_text).text = currCommand.command
        holder.itemView.findViewById<MaterialTextView>(R.id.command_parameter).text = currCommand.parameters?.joinToString(" ")
        holder.itemView.findViewById<MaterialTextView>(R.id.command_exp).text = gen_exp(currCommand)
    }

    override fun getItemCount(): Int {
        return commands.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}