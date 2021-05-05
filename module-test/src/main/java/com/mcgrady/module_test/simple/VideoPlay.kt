package com.mcgrady.module_test.simple

/**
 * Created by mcgrady on 5/2/21.
 */

fun main(args: Array<String>) {
    PlayerUI.get().showPlayer(PlayerUser(1, "mcgrady"))
    PlayerUI.get().showPlayer(PlayerUser(2, "tom", PlayerSkin.YELLOW))
    PlayerUI.get().showPlayer(PlayerUser(3, "jack", PlayerSkin.VIP()))
    PlayerUI.get().showPlayer(PlayerUser(4, "tony", PlayerSkin.VIP(null, "VIP消息")))
    PlayerUI.get().showPlayer(PlayerUser(5, "alen", PlayerSkin.VIP("VIP标题", "VIP消息")))
}

fun printDrivider() = println("-------------------------------")

class PlayerUI private constructor() {
    companion object {
        fun get(): PlayerUI {
            return Holder.instance
        }
    }

    private object Holder {
        val instance = PlayerUI()
    }

    fun showPlayer(user: PlayerUser) {
        MediaPlayer(getPlayerView(user.playerSkin)).show()
        printDrivider()
    }
}

sealed class PlayerSkin {
    object YELLOW: PlayerSkin()
    object BLUE: PlayerSkin()
    class VIP(var title: String? = null, var message: String? = null): PlayerSkin()
}

fun getPlayerView(playerSkin: PlayerSkin) = when(playerSkin) {
    PlayerSkin.YELLOW -> YellowPlayerView()
    PlayerSkin.BLUE -> BluePlayerView()
    is PlayerSkin.VIP -> VIPPlayerView(playerSkin.title, playerSkin.message)
}

class YellowPlayerView: IPlayerView {
    override fun show() {
        println("YellowPlayerView show, button = ${button()}")
    }

    override fun button(): String = "YellowPlayerView button"
}

class BluePlayerView: IPlayerView {
    override fun show() {
        println("BluePlayerView show, button = ${button()}")
    }

    override fun button() = "BluePlayerView button"
}

val TITLE = "播放器标题"
val MESSAGE = "播放器消息"

class VIPPlayerView(var title: String?, var message: String?): IPlayerView {

    init {
        println("VIPPlayerView 主构造函数")
        title = title ?: TITLE
        message = message ?: MESSAGE
    }

    constructor() : this(TITLE, MESSAGE) {
        println("无参构造方法")
    }

    constructor(message: String?) : this(TITLE, message) {
        println("message 参数的构造方法")
    }

    override fun show() {
        println("VIPPlayerView show, title = ${title} message = ${message} button = ${button()}")
    }

    override fun button() = "VIPPlayerView button"
}

interface IPlayerView {

    fun show()

    fun button(): String
}

class MediaPlayer(playerSkin: IPlayerView): IPlayerView by playerSkin

data class PlayerUser(var id: Long, var name: String, var playerSkin: PlayerSkin = PlayerSkin.BLUE) {
    init {
        println("PlayerUser 主构造方法")
        println("id = $id name = $name playerSkin = $playerSkin")
    }
}