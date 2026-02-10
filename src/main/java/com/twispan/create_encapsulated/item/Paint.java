package com.twispan.create_encapsulated.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class Paint extends Item {
    private final Supplier<? extends Fluid> fluid;

    public Paint (Properties props, Supplier<? extends Fluid> fluid) {
        super(props);
        this.fluid = fluid;
    }

    public Supplier<? extends Fluid> getFluid() {
        return fluid;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) { return 32; }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public boolean hasCraftingRemainingItem(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(@NotNull ItemStack stack) {
        return new ItemStack(Items.GLASS_BOTTLE);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState block = level.getBlockState(pos);
        Player player = context.getPlayer();

        // Check if clicking on an empty cauldron
        if (block.is(Blocks.CAULDRON)) {
            if (!level.isClientSide && player != null) {
                // Fill cauldron by one stage and play bottle empty sound
                level.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1), 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                player.awardStat(Stats.ITEM_USED.get(this));

                if (!player.getAbilities().instabuild) {
                    ItemStack itemInHand = context.getItemInHand();
                    itemInHand.shrink(1);

                    // Give an empty bottle in return
                    ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                    if (!player.getInventory().add(bottle)) {
                       player.drop(bottle, false);
                    }
                }
            }
        }

        if (block.is(Blocks.WATER_CAULDRON)) {
            int currentCauldronLevel = block.getValue(LayeredCauldronBlock.LEVEL);
            if (currentCauldronLevel < 3) {
                if (!level.isClientSide() && player != null) {
                    level.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL,currentCauldronLevel + 1), 3);
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    player.awardStat(Stats.ITEM_USED.get(this));

                    if (!player.getAbilities().instabuild) {
                        ItemStack itemInHand = context.getItemInHand();
                        itemInHand.shrink(1);

                        // Give an empty bottle in return
                        ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                        if (!player.getInventory().add(bottle)) {
                            player.drop(bottle, false);
                        }
                    }
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity entity) {
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1800));
        }

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            stack.shrink(1);

            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
