package net.numalab.mojii

import dev.kotx.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class Mojii : JavaPlugin() {
    init {
        flyLib {
            command()
        }
    }
}