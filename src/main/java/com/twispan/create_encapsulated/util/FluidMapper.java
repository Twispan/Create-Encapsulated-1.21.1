package com.twispan.create_encapsulated.util;

import com.twispan.create_encapsulated.fluid.other_medicine.OMedicineFluidType;
import com.twispan.create_encapsulated.fluid.other_medicine.OMedicines;
import com.twispan.create_encapsulated.fluid.potions.PotionFluidType;
import com.twispan.create_encapsulated.fluid.potions.PotionType;
import com.twispan.create_encapsulated.fluid.vitamins.VitaminFluidType;
import com.twispan.create_encapsulated.fluid.vitamins.VitaminType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import com.cobblemon.mod.common.CobblemonItems;

public class FluidMapper {
    public static @NotNull ItemStack getPotionItem(PotionFluidType potionFluidType) {
        PotionType type = potionFluidType.getType();

        // Transform the empty bottle into the corresponding potion item
        return switch (type) {
            case POTION -> new ItemStack(CobblemonItems.POTION);
            case SUPER -> new ItemStack(CobblemonItems.SUPER_POTION);
            case HYPER -> new ItemStack(CobblemonItems.HYPER_POTION);
            case MAX -> new ItemStack(CobblemonItems.MAX_POTION);
            case FULL_RESTORE -> new ItemStack(CobblemonItems.FULL_RESTORE);
        };
    }

    public static @NotNull ItemStack getVitaminItem(VitaminFluidType vitaminFluidType) {
        VitaminType type = vitaminFluidType.getType();

        // Transform the empty bottle into the corresponding vitamin item
        return switch (type) {
            case CARBOS -> new ItemStack(CobblemonItems.CARBOS);
            case CALCIUM -> new ItemStack(CobblemonItems.CALCIUM);
            case HP_UP -> new ItemStack(CobblemonItems.HP_UP);
            case PROTEIN -> new ItemStack(CobblemonItems.PROTEIN);
            case PP_UP -> new ItemStack(CobblemonItems.PP_UP);
            case PP_MAX -> new ItemStack(CobblemonItems.PP_MAX);
            case IRON -> new ItemStack(CobblemonItems.IRON);
            case ZINC -> new ItemStack(CobblemonItems.ZINC);
        };
    }

    public static @NotNull ItemStack getOMedicineItem(OMedicineFluidType oMedicineFluidType) {
        OMedicines type = oMedicineFluidType.getType();

        // Transform the empty bottle into the corresponding medicine item
        return switch (type) {
            case ANTIDOTE -> new ItemStack(CobblemonItems.ANTIDOTE);
            case PARALYZE_HEAL -> new ItemStack(CobblemonItems.PARALYZE_HEAL);
            case AWAKENING -> new ItemStack(CobblemonItems.AWAKENING);
            case FULL_HEAL -> new ItemStack(CobblemonItems.FULL_HEAL);
            case BURN_HEAL -> new ItemStack(CobblemonItems.BURN_HEAL);
            case ICE_HEAL -> new ItemStack(CobblemonItems.ICE_HEAL);
            case ETHER -> new ItemStack(CobblemonItems.ETHER);
            case MAX_ETHER -> new ItemStack(CobblemonItems.MAX_ETHER);
            case ELIXIR -> new ItemStack(CobblemonItems.ELIXIR);
            case MAX_ELIXIR -> new ItemStack(CobblemonItems.MAX_ELIXIR);
        };
    }
}