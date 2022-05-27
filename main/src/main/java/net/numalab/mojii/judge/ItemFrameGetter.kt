package net.numalab.mojii.judge

import org.bukkit.Location
import org.bukkit.entity.ItemFrame


class ItemFrameGetter {
    companion object {
        /**
         * 指定Locationから全方向にItemFrameをたどっていき、
         * そのItemFrameのLocationをListで返す
         */
        fun getFrom(from: Location): List<List<ItemFrame>> {
            return Direction.values().map { getFrom(from, it) }
        }

        private fun getFrom(from: Location, direction: Direction): List<ItemFrame> {
            val list = mutableListOf<ItemFrame>()
            var loc = from.clone()
            while (true) {
                val frame = loc.getNearbyEntitiesByType(ItemFrame::class.java, 0.5 - 0.0625 + 0.01).firstOrNull()
                if (frame == null) {
                    break
                } else {
                    list.add(frame)
                }
                loc = direction.addLocation(loc)
            }
            return list
        }
    }

    private enum class Direction(val x: Int, val z: Int) {
        PX(1, 0),
        NX(-1, 0),
        PZ(0, 1),
        NZ(0, -1),
        PXPZ(1, 1),
        PXNZ(-1, -1),
        NXNZ(1, -1),
        NXPZ(-1, 1);

        fun addLocation(loc: Location): Location {
            return loc.clone().add(x.toDouble(), 0.0, z.toDouble())
        }
    }
}