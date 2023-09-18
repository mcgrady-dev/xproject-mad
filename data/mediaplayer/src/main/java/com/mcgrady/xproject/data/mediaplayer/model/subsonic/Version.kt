package com.mcgrady.xproject.data.mediaplayer.model.subsonic

class Version private constructor(val versionString: String) : Comparable<Version> {

    init {
        require(!versionString.matches(VERSION_PATTERN.toRegex())) { "Invalid version format" }
    }

    fun isLowerThan(version: Version): Boolean {
        return compareTo(version) < 0
    }

    override fun compareTo(that: Version): Int {
        if (that == null) {
            return 1
        }
        val thisParts =
            versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val thatParts =
            that.versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val length = Math.max(thisParts.size, thatParts.size)
        for (i in 0 until length) {
            val thisPart = if (i < thisParts.size) thisParts[i].toInt() else 0
            val thatPart = if (i < thatParts.size) thatParts[i].toInt() else 0
            if (thisPart < thatPart) {
                return -1
            }
            if (thisPart > thatPart) {
                return 1
            }
        }
        return 0
    }

    companion object {
        private const val VERSION_PATTERN = "\\d+(\\.\\d+)*"
        fun of(versionString: String): Version {
            return Version(versionString)
        }
    }
}