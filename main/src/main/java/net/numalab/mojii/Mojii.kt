package net.numalab.mojii

import dev.kotx.flylib.flyLib
import net.numalab.mojii.command.MojiiCommand
import org.bukkit.plugin.java.JavaPlugin

class Mojii : JavaPlugin() {
    init {
        flyLib {
            command(MojiiCommand())
        }
    }
}