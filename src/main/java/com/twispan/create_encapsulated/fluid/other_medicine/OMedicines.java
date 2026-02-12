package com.twispan.create_encapsulated.fluid.other_medicine;

public enum OMedicines {
    ANTIDOTE(0xEFBF37),
    PARALYZE_HEAL(0xC1D72F),
    AWAKENING(0x5DC5D5),
    FULL_HEAL(0xB55400),
    BURN_HEAL(0x53CB77),
    ICE_HEAL(0xED847A),
    ETHER(0xD7A7DF),
    MAX_ETHER(0xB8F096),
    ELIXIR(0xF0AF96),
    MAX_ELIXIR(0x96F0E3);

    private final int type;

    OMedicines (int type) { this.type = type; }

    public int getMedicineType() { return type; }
}
