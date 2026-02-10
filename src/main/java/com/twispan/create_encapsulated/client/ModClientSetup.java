package com.twispan.create_encapsulated.client;

import com.twispan.create_encapsulated.CreateEncapsulated;
import com.twispan.create_encapsulated.fluid.PaintFluidType;
import com.twispan.create_encapsulated.registries.ModFluids;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;

public class ModClientSetup {

    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        ResourceLocation PAINT_STILL = ResourceLocation.fromNamespaceAndPath(CreateEncapsulated.MODID, "block/paint_still");
        ResourceLocation PAINT_FLOW = ResourceLocation.fromNamespaceAndPath(CreateEncapsulated.MODID, "block/paint_flow");

        registerPaintFluidRendering(event, ModFluids.RED_PAINT_TYPE.get(), PAINT_STILL, PAINT_FLOW);
        registerPaintFluidRendering(event, ModFluids.BLUE_PAINT_TYPE.get(), PAINT_STILL, PAINT_FLOW);
        registerPaintFluidRendering(event, ModFluids.GREEN_PAINT_TYPE.get(), PAINT_STILL, PAINT_FLOW);
        registerPaintFluidRendering(event, ModFluids.YELLOW_PAINT_TYPE.get(), PAINT_STILL, PAINT_FLOW);
        registerPaintFluidRendering(event, ModFluids.PINK_PAINT_TYPE.get(), PAINT_STILL, PAINT_FLOW);
        registerPaintFluidRendering(event, ModFluids.BLACK_PAINT_TYPE.get(), PAINT_STILL, PAINT_FLOW);
        registerPaintFluidRendering(event, ModFluids.WHITE_PAINT_TYPE.get(), PAINT_STILL, PAINT_FLOW);
    }

    private static void registerPaintFluidRendering(
        RegisterClientExtensionsEvent event,
        PaintFluidType fluidType,
        ResourceLocation still,
        ResourceLocation flowing) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return still;
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return flowing;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return still;
            }

            @Override
            public int getTintColor() {
                return 0xFF000000 | fluidType.getPaintColor().getColor();
            }
        }, fluidType);
    }
}