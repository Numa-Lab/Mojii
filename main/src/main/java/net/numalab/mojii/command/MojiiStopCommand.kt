package net.numalab.mojii.command

import dev.kotx.flylib.command.Command
import net.numalab.mojii.Mojii

class MojiiStopCommand() : Command("stop") {
    init {
        description("stop mojii game")
        usage {
            selectionArgument("really?", "really")
            executes {
                success("stopped mojii game")
                (plugin as Mojii).currentGame = null
            }
        }
    }
}