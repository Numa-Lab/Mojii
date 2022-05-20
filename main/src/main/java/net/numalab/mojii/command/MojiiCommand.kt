package net.numalab.mojii.command

import dev.kotx.flylib.command.Command

class MojiiCommand : Command("mojii") {
    init {
        description("The root command of mojii")
        children(MojiiStartCommand(), MojiiSearchCommand())
    }
}