package com.eerussianguy.firmalife;


import net.dries007.tfc.objects.items.ItemMisc;
import net.dries007.tfc.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.eerussianguy.firmalife.gui.FLGuiHandler;
import com.eerussianguy.firmalife.player.CapPlayerDataFL;
import com.eerussianguy.firmalife.player.PlayerDataFL;
import com.eerussianguy.firmalife.registry.BlocksFL;
import com.eerussianguy.firmalife.registry.ItemsFL;
import com.eerussianguy.firmalife.util.HelpersFL;
import net.dries007.tfc.Constants;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.objects.blocks.agriculture.BlockFruitTreeLeaves;
import net.dries007.tfc.objects.blocks.agriculture.BlockFruitTreeTrunk;
import net.dries007.tfc.api.types.IFruitTree;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.util.Helpers;

import java.util.Objects;

import static com.eerussianguy.firmalife.FirmaLife.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonEventHandlerFL
{
    @SubscribeEvent
    public static void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event)
    {
        final EntityPlayer player = event.getHarvester();
        final ItemStack held = player == null ? ItemStack.EMPTY : player.getHeldItemMainhand();
        final IBlockState state = event.getState();
        final Block block = state.getBlock();

        if (block instanceof BlockFruitTreeLeaves)
        {
            event.getDrops().add(new ItemStack(ItemsFL.FRUIT_LEAF, 2 + Constants.RNG.nextInt(4)));
        }
        else if (block instanceof BlockFruitTreeTrunk) //todo: implement this without strings
        {
            IFruitTree tree = ((BlockFruitTreeTrunk) block).getTree();
            String poleName = MOD_ID + tree.getName().toLowerCase() + "_pole";
            Item pole = ItemMisc.getByNameOrId(poleName);
            if (pole != null)
                event.getDrops().add(new ItemStack(pole));
        }
    }

    // this doesn't work right now -- needs to use ItemRightClick or something to that effect
    @SubscribeEvent
    public static void onFillBucketEvent(FluidEvent.FluidFillingEvent event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Entity entity = world.getEntitiesWithinAABBExcludingEntity(world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 3, false), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)).get(0);
        String name = entity.getName();
        Fluid fluid = FluidsTFC.MILK.get();
        boolean foundMilkable = false;
        switch (name)
        {
            case "yaktfc":
                foundMilkable = true;
                fluid = FluidsTFC.FRESH_WATER.get();
        }
        if (foundMilkable)
        {
            IFluidTank tank = event.getTank();
            event.setCanceled(true);
            tank.fill(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true);
        }
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getObject();
            if (!player.hasCapability(CapPlayerDataFL.CAPABILITY, null))
            {
                event.addCapability(CapPlayerDataFL.NAMESPACE, new PlayerDataFL());
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        if (event.getItemStack().getItem() == Item.getItemFromBlock(BlocksFL.PUMPKIN_FRUIT) &&
            Helpers.playerHasItemMatchingOre(event.getEntityPlayer().inventory, "knife") &&
            !event.getWorld().isRemote)
        {
            FLGuiHandler.openGui(event.getWorld(), event.getPos(), event.getEntityPlayer(), FLGuiHandler.Type.KNAPPING_PUMPKIN);
        }
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(TerraFirmaCraft.MOD_ID))
        {
            HelpersFL.insertWhitelist();
        }
    }
}
