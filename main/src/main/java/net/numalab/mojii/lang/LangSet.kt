package net.numalab.mojii.lang


interface LangSet {
    fun langName(): String
    fun chars(): CharSet
}