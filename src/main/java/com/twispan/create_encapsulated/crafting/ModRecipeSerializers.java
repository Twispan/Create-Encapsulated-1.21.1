package com.twispan.create_encapsulated.crafting;

import com.twispan.create_encapsulated.CreateEncapsulated;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, CreateEncapsulated.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<PaintDyesArmor>> PAINT_ARMOR_DYE =
            RECIPE_SERIALIZERS.register("paint_armor_dye",
                    () -> new SimpleCraftingRecipeSerializer<>(PaintDyesArmor::new));
}
