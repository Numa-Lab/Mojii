package net.numalab.mojii.command

import dev.kotx.flylib.command.Command
import net.numalab.mojii.map.MojiiMap
import net.numalab.mojii.map.stacks

class MojiiMapForceRedraw : Command("redraw") {
    init {
        description("MojiiMapを強制再描画します")
        usage {
            selectionArgument("isForce?","force")
            executes {
                stacks.forEach {
                    it.redraw()
                }
            }
        }
    }
}