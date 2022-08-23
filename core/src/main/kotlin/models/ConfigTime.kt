package models

sealed class ConfigTime {
    object All : ConfigTime()
    data class Value(val value: Int) : ConfigTime()
}
