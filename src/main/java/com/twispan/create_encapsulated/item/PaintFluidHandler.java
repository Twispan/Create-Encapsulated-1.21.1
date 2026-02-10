package com.twispan.create_encapsulated.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public class PaintFluidHandler implements IFluidHandlerItem {
    private final ItemStack container;
    private final FluidStack fluidStack;
    private static final int CAPACITY = 250; // 250mb = 1/4 bucket

    public PaintFluidHandler(ItemStack container, FluidStack fluidStack) {
        this.container = container;
        this.fluidStack = fluidStack;
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return container;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return tank == 0 ? fluidStack.copy() : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return CAPACITY;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false; // Cannot fill this item
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0; // Cannot fill this item
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || FluidStack.isSameFluid(resource, resource)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        int drained = Math.min(CAPACITY, maxDrain);
        FluidStack result = fluidStack.copy();
        result.setAmount(drained);

        if (action.execute()) {
            // Replace the paint item with an empty bottle
            container.shrink(1);
            // Note: The basin/tank will handle giving back the bottle
        }

        return result;
    }
}