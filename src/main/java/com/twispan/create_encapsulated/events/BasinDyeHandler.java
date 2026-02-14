package com.twispan.create_encapsulated.events;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.twispan.create_encapsulated.fluid.paint.PaintFluidType;
import com.twispan.create_encapsulated.util.PaintColorMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BasinDyeHandler {

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (heldStack.isEmpty()) return;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof BasinBlockEntity)) return;

        var fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
        if (fluidHandler == null) return;

        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack fluidInTank = fluidHandler.getFluidInTank(i);
            if (fluidInTank.isEmpty()) continue;
            if (!(fluidInTank.getFluidType() instanceof PaintFluidType paintFluidType)) continue;

            var paintColor = paintFluidType.getPaintColor();

            // Try armor dyeing first, this doesn't work like crafting, it only replaces the color of the armor
            // by the color of the paint that is in the basin
            if (isLeatherArmor(heldStack)) {
                if(!level.isClientSide) {
                    FluidStack drained = fluidHandler.drain(
                            new FluidStack(fluidInTank.getFluid(), 40),
                            IFluidHandler.FluidAction.EXECUTE
                    );
                    if (!drained.isEmpty()) {
                        int color = paintColor.getColor();
                        heldStack.set(DataComponents.DYED_COLOR, new DyedItemColor(color, true));
                        level.playSound(null, pos, SoundEvents.PLAYER_SPLASH, SoundSource.AMBIENT, 1.0F, 1.0F);
                    }
                }
                event.setCanceled(true);
                return;
            }

            if(PaintColorMapper.isRecolorable(heldStack)) {
                var recolored = PaintColorMapper.recolor(heldStack, paintColor);
                if (recolored.isPresent()) {
                    if(!level.isClientSide()) {
                        FluidStack drained = fluidHandler.drain(
                                new FluidStack(fluidInTank.getFluid(), 40),
                                IFluidHandler.FluidAction.EXECUTE
                        );
                        if (!drained.isEmpty()) {
                            player.setItemInHand(InteractionHand.MAIN_HAND, recolored.get());
                            level.playSound(null, pos, SoundEvents.PLAYER_SPLASH, SoundSource.AMBIENT, 1.0F, 1.0F);
                        }
                    }
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    private static boolean isLeatherArmor(ItemStack stack) {
        if (stack.isEmpty()) return false;
        var item = stack.getItem();
        return item == Items.LEATHER_HELMET
                || item == Items.LEATHER_CHESTPLATE
                || item == Items.LEATHER_LEGGINGS
                || item == Items.LEATHER_BOOTS
                || item == Items.LEATHER_HORSE_ARMOR
                || stack.has(DataComponents.DYED_COLOR);
    }
}
