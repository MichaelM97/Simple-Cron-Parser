package models

data class CurrentTime(
    val hour: Int,
    val minute: Int,
) {
    val nextHour: Int = if (hour == 23) 0 else hour + 1
}
