package com.mcgrady.xproject.core.ui.gradient.rainbow

/** changes [ContextColor] list to an integer array. */
internal fun MutableList<ContextColor>.toIntArray(): IntArray {
    val colors = mutableListOf<Int>()
    for (contextColor in this) {
        colors.add(contextColor.color)
    }
    return colors.toIntArray()
}