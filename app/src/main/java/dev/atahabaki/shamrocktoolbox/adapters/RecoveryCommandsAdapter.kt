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

    private fun gen_exp(holder: ViewHolder, command: Command): String {
        return when (command.command.trim()) {
            "install" -> holder.itemView.context.getString(R.string.rec_cmd_install_tip, command.parameters?.joinToString(" "))
            "wipe" -> holder.itemView.context.getString(R.string.rec_cmd_wipe_tip, command.parameters?.joinToString(" "))
            "format" -> holder.itemView.context.getString(R.string.rec_cmd_format_tip)
            "backup" -> holder.itemView.context.getString(R.string.rec_cmd_backup_tip, command.parameters?.joinToString(" "))
            "restore" -> holder.itemView.context.getString(R.string.rec_cmd_restore_tip, command.parameters?.joinToString(" "))
            "remountrw" -> holder.itemView.context.getString(R.string.rec_cmd_remountrw_tip)
            "mount" -> holder.itemView.context.getString(R.string.rec_cmd_mount_tip, command.parameters?.joinToString(" "))
            "set" -> holder.itemView.context.getString(R.string.rec_cmd_set_tip)
            "mkdir" -> holder.itemView.context.getString(R.string.rec_cmd_mkdir_tip, command.parameters?.joinToString(" "))
            "reboot" -> holder.itemView.context.getString(R.string.rec_cmd_reboot_tip, command.parameters?.joinToString(" "))
            "sideload" -> holder.itemView.context.getString(R.string.rec_cmd_sideload_tip)
            "fixperms" -> holder.itemView.context.getString(R.string.rec_cmd_fixperms_tip)
            "decrypt" -> holder.itemView.context.getString(R.string.rec_cmd_decrypt_tip, command.parameters?.joinToString(" "))
            else -> holder.itemView.context.getString(R.string.dont_know)
        }
    }

    fun setCommands(commands: List<Command>) {
        this.commands = commands
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.command_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currCommand = commands[position]
        holder.itemView.findViewById<MaterialTextView>(R.id.command_text).text = currCommand.command
        holder.itemView.findViewById<MaterialTextView>(R.id.command_parameter).text = currCommand.parameters?.joinToString(" ")
        holder.itemView.findViewById<MaterialTextView>(R.id.command_exp).text = gen_exp(holder, currCommand)
    }

    override fun getItemCount(): Int {
        return commands.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}