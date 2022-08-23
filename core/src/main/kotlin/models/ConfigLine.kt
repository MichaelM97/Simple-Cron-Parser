package models

data class ConfigLine(
    val minutes: ConfigTime,
    val hour: ConfigTime,
    val command: String,
)
