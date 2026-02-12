package com.twispan.create_encapsulated.fluid.other_medicine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;

public class OMedicineFluidType extends FluidType {
    private final OMedicines type;

    public OMedicineFluidType(Properties props, OMedicines type) {
        super(props);
        this.type = type;
    }

    public OMedicines getType() { return type; }

    @Override
    public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) { return false; }

    @Override
    public boolean canDrownIn(LivingEntity entity) { return true; }

    @Override
    public boolean canExtinguish(Entity entity) { return true; }

    @Override
    public boolean canHydrate(Entity entity) { return false; }
}
