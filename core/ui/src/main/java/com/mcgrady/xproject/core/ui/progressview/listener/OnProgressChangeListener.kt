package com.mcgrady.xproject.core.ui.progressview.listener

/**  OnProgressChangeListener is an interface for listening to the progress is changed. */
fun interface OnProgressChangeListener {

    /** invoked when progress value is changed. */
    fun onChange(progress: Float)
}

