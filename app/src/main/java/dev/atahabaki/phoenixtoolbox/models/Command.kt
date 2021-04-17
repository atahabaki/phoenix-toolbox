package dev.atahabaki.phoenixtoolbox.models

data class Command(var command: String, var parameters: List<String>?) {
    override fun toString(): String {
        return "$command ${parameters?.joinToString(" ")}"
    }
}