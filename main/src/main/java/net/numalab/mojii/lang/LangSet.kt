package net.numalab.mojii.lang

import org.apache.commons.lang.CharSet

interface LangSet {
    fun langName(): String
    fun chars(): CharSet
}