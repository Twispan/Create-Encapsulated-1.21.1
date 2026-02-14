package com.twispan.create_encapsulated.crafting;

import com.twispan.create_encapsulated.item.Paint;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaintDyesArmor extends CustomRecipe {

    public PaintDyesArmor (CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, @NotNull Level level) {
        ItemStack armorStack = ItemStack.EMPTY;
        List<ItemStack> paintStacks = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof Paint) {
                paintStacks.add(stack);
            } else if (stack.has(DataComponents.DYED_COLOR) || isDyeableArmor(stack)) {
                if (!armorStack.isEmpty()) return false;
                armorStack = stack;
            } else {
                return false;
            }
        }

        return !armorStack.isEmpty() && !paintStacks.isEmpty();
    }

    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.@NotNull Provider provider) {
        ItemStack armorStack = ItemStack.EMPTY;
        List<ItemStack> paintStacks = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof Paint) {
                paintStacks.add(stack);
            } else if (stack.has(DataComponents.DYED_COLOR) || isDyeableArmor(stack)) {
                armorStack = stack;
            }
        }

        if (armorStack.isEmpty() || paintStacks.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = armorStack.copy();
        int mixedColor = blendColors(armorStack, paintStacks);
        result.set(DataComponents.DYED_COLOR, new DyedItemColor(mixedColor, true));
        return result;
    }

    private int blendColors(ItemStack armorStack, List<ItemStack> paintStacks) {
        int totalR = 0;
        int totalG = 0;
        int totalB = 0;
        int count = 0;

        // Include existing armor color in the blend if it already has one
        if (armorStack.has(DataComponents.DYED_COLOR)) {
            int existingColor = Objects.requireNonNull(armorStack.get(DataComponents.DYED_COLOR)).rgb();
            totalR += (existingColor >> 16) & 0xFF;
            totalG += (existingColor >> 8) & 0xFF;
            totalB += existingColor & 0xFF;
            count++;
        }

        // Add each paint color to the blend
        for (ItemStack paintStack : paintStacks) {
            if (paintStack.getItem() instanceof Paint paintItem) {
                if (paintItem.getColor() != null) {
                    int color = paintItem.getColor().getColor();
                    totalR += (color >> 16) & 0xFF;
                    totalG += (color >> 8) & 0xFF;
                    totalB += color & 0xFF;
                    count++;
                }
            }
        }

        if (count == 0) return 0xFFFFFF;

        // Average the colors
        int avgR = totalR / count;
        int avgG = totalG / count;
        int avgB = totalB / count;

        return (avgR << 16) | (avgG << 8) | avgB;
    }


    private boolean isDyeableArmor(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        return item == Items.LEATHER_HELMET
                || item == Items.LEATHER_CHESTPLATE
                || item == Items.LEATHER_LEGGINGS
                || item == Items.LEATHER_BOOTS
                || item == Items.LEATHER_HORSE_ARMOR
                || stack.has(DataComponents.DYED_COLOR);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2; // At least 2 slots needed
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.PAINT_ARMOR_DYE.get();
    }
}
