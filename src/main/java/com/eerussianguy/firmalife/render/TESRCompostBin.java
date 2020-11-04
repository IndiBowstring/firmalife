package com.eerussianguy.firmalife.render;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.eerussianguy.firmalife.blocks.BlockCompostBin;
import com.eerussianguy.firmalife.te.TECompostBin;

public class TESRCompostBin extends TileEntitySpecialRenderer<TECompostBin>
{
    @Override
    public void render(TECompostBin current, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        World world = current.getWorld();
        float fill = current.getFillForRender();
        // This all crashes.
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y, z + 0.5);
        GlStateManager.scale(0, fill, 0);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        IBlockState state = world.getBlockState(current.getPos());
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, current.getPos(), bufferBuilder,true);
        tessellator.draw();

        GlStateManager.popMatrix();
    }
}
