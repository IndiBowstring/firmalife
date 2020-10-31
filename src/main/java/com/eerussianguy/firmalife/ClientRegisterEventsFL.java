package com.eerussianguy.firmalife;

import com.eerussianguy.firmalife.blocks.*;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropDead;
import net.minecraft.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockStem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.eerussianguy.firmalife.blocks.BlockPlanter;
import com.eerussianguy.firmalife.blocks.BlockStemCrop;
import com.eerussianguy.firmalife.init.StatePropertiesFL;
import com.eerussianguy.firmalife.registry.BlocksFL;
import com.eerussianguy.firmalife.registry.ItemsFL;
import com.eerussianguy.firmalife.render.TESRLeafMat;
import com.eerussianguy.firmalife.render.TESROven;
import com.eerussianguy.firmalife.render.TESRQuadPlanter;
import com.eerussianguy.firmalife.render.VanillaStemStateMapper;
import com.eerussianguy.firmalife.te.TELeafMat;
import com.eerussianguy.firmalife.te.TEOven;
import com.eerussianguy.firmalife.te.TEQuadPlanter;
import net.dries007.tfc.client.GrassColorHandler;
import net.dries007.tfc.objects.blocks.agriculture.BlockFruitTreeLeaves;
import net.dries007.tfc.objects.blocks.wood.BlockSaplingTFC;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = {Side.CLIENT}, modid = FirmaLife.MOD_ID)
public class ClientRegisterEventsFL
{
    public ClientRegisterEventsFL() {}

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        //Setting the model resource location for items
        for (Item i : ItemsFL.getAllEasyItems())
            ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName().toString()));
        for (ItemBlock ib : BlocksFL.getAllIBs())
            ModelLoader.setCustomModelResourceLocation(ib, 0, new ModelResourceLocation(ib.getRegistryName().toString()));
        for (BlockFruitTreeLeaves leaves : BlocksFL.getAllFruitLeaves())
            ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockFruitTreeLeaves.DECAYABLE).ignore(BlockFruitTreeLeaves.HARVESTABLE).build());
        ModelLoader.setCustomModelResourceLocation(ItemsFL.CRACKED_COCONUT, 0, new ModelResourceLocation(ItemsFL.CRACKED_COCONUT.getRegistryName(), "inventory"));

        //Configuring block states to ignore certain properties / use others
        //use vanilla stem rendering for StemCrops
        for (BlockStemCrop block : BlocksFL.getAllCropBlocks())
            ModelLoader.setCustomStateMapper(block, new VanillaStemStateMapper());
        for (BlockPlanter planter : BlocksFL.getAllPlanters())
            ModelLoader.setCustomStateMapper(planter, new StateMap.Builder().ignore(StatePropertiesFL.CAN_GROW).build());
        for (BlockFruitDoor door : BlocksFL.getAllFruitDoors())
            ModelLoader.setCustomStateMapper(door, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
        for (BlockFruitFenceGate gate : BlocksFL.getAllFruitFenceGates())
            ModelLoader.setCustomStateMapper(gate, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());

        ModelLoader.setCustomStateMapper(BlocksFL.CINNAMON_LOG, new StateMap.Builder().ignore(StatePropertiesFL.CAN_GROW).build());
        ModelLoader.setCustomStateMapper(BlocksFL.CINNAMON_LEAVES, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
        ModelLoader.setCustomStateMapper(BlocksFL.CINNAMON_SAPLING, new StateMap.Builder().ignore(BlockSaplingTFC.STAGE).build());

        for (Block block : BlocksFL.getAllFluidBlocks())
            ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
        ModelLoader.setCustomStateMapper(BlocksFL.BLOCK_GREENHOUSE_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
        ModelLoader.setCustomStateMapper(BlocksFL.CINNAMON_LOG, new StateMap.Builder().ignore(StatePropertiesFL.CAN_GROW).build());
        ModelLoader.setCustomStateMapper(BlocksFL.CINNAMON_LEAVES, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
        ModelLoader.setCustomStateMapper(BlocksFL.CINNAMON_SAPLING, new StateMap.Builder().ignore(BlockSaplingTFC.STAGE).build());

        ClientRegistry.bindTileEntitySpecialRenderer(TEOven.class, new TESROven());
        ClientRegistry.bindTileEntitySpecialRenderer(TELeafMat.class, new TESRLeafMat());
        ClientRegistry.bindTileEntitySpecialRenderer(TEQuadPlanter.class, new TESRQuadPlanter());
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerColorHandlerItems(ColorHandlerEvent.Item event)
    {
        ItemColors itemColors = event.getItemColors();

        itemColors.registerItemColorHandler((stack, tintIndex) ->
                event.getBlockColors().colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
            BlocksFL.getAllFruitLeaves().toArray(new BlockFruitTreeLeaves[0])
        );

        itemColors.registerItemColorHandler((stack, tintIndex) ->
                event.getBlockColors().colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
            BlocksFL.CINNAMON_LEAVES);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerColorHandlerBlocks(ColorHandlerEvent.Block event)
    {
        BlockColors blockColors = event.getBlockColors();
        IBlockColor foliageColor = GrassColorHandler::computeGrassColor;

        blockColors.registerBlockColorHandler(foliageColor, BlocksFL.getAllFruitLeaves().toArray(new Block[0]));

        //use vanilla stem coloring for stemcrops
        for (BlockStemCrop block : BlocksFL.getAllCropBlocks())
        {
            blockColors.registerBlockColorHandler((state, world, pos, tintIndex) ->
            {
                int vanillaAge = VanillaStemStateMapper.getVanillaAge(state);
                if (vanillaAge == -1)
                    vanillaAge = 7; //for fully grown, we color it like stage 7
                return blockColors.colorMultiplier(Blocks.MELON_STEM.getDefaultState().withProperty(BlockStem.AGE, vanillaAge), world, pos, tintIndex);
            }, block);
        }

        for (BlockCropDead block : BlocksFL.getAllDeadCrops())
        {
            blockColors.registerBlockColorHandler((state, world, os, tintIndex) -> 0xCC7400, block);
        }
        blockColors.registerBlockColorHandler(foliageColor, BlocksFL.CINNAMON_LEAVES);
    }
}
