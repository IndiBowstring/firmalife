package com.eerussianguy.firmalife.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.SideOnly;

import com.eerussianguy.firmalife.items.ItemFruitDoor;
import com.eerussianguy.firmalife.registry.BlocksFL;
import com.eerussianguy.firmalife.te.TECompostBin;
import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;

public class BlockCompostBin extends Block implements IItemSize
{
    public static final PropertyBool CONNECTED_NORTH = PropertyBool.create("north");
    public static final PropertyBool CONNECTED_EAST = PropertyBool.create("east");
    public static final PropertyBool CONNECTED_WEST = PropertyBool.create("west");
    public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create("south");
    /**
    public static final PropertyBool CONNECTED_NORTHWEST = PropertyBool.create("northwest");
    public static final PropertyBool CONNECTED_NORTHEAST = PropertyBool.create("northeast");
    public static final PropertyBool CONNECTED_SOUTHWEST = PropertyBool.create("southwest");
    public static final PropertyBool CONNECTED_SOUTHEAST = PropertyBool.create("southeast");
     **/
    public static final AxisAlignedBB COMPOSTBIN_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockCompostBin()
    {
        super(Material.WOOD, MapColor.WOOD);
        this.setHardness(2.0F);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState()
            .withProperty(CONNECTED_NORTH, false)
            .withProperty(CONNECTED_EAST, false)
            .withProperty(CONNECTED_SOUTH, false)
            .withProperty(CONNECTED_WEST, false));
            /**
            .withProperty(CONNECTED_NORTHWEST, false)
            .withProperty(CONNECTED_NORTHEAST, false)
            .withProperty(CONNECTED_SOUTHWEST, false)
            .withProperty(CONNECTED_SOUTHEAST, false));
             **/
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        /**
        if(!world.isRemote)
        {
            TECompostBin current = (TECompostBin) world.getTileEntity(pos);
            if(current.getFillAmountOrganic() == 0)
            {
                if(!player.getHeldItem(hand).isEmpty())
                {
                    ItemStack inHand = player.getHeldItem(hand);
                    if(inHand.getItem() instanceof ItemFruitDoor)
                    {
                        return true;
                    }
                }
            }
        }
        **/
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {CONNECTED_NORTH, CONNECTED_EAST, CONNECTED_WEST, CONNECTED_SOUTH});
    }

    @Override
    public int getMetaFromState(IBlockState state) { return 0; }

    @Override
    @SuppressWarnings("deprecation")
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        for (EnumFacing face : EnumFacing.HORIZONTALS)
        {
            if (worldIn.getBlockState(pos.offset(face)).getBlock() == BlocksFL.COMPOST_BIN)
            {
                if (face == EnumFacing.NORTH)
                {
                    state = state.withProperty(CONNECTED_NORTH, true);
                }
                else if (face == EnumFacing.SOUTH)
                {
                    state = state.withProperty(CONNECTED_SOUTH, true);
                }
                else if (face == EnumFacing.EAST)
                {
                    state = state.withProperty(CONNECTED_EAST, true);
                }
                else
                {
                    state = state.withProperty(CONNECTED_WEST, true);
                }
            }
        }
        return state;
    }

    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack stack) { return Size.LARGE; }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack stack) { return Weight.MEDIUM; }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    @SuppressWarnings("deprecation")
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) { return COMPOSTBIN_BB; }

    @Override
    public boolean hasTileEntity(IBlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TECompostBin(); }
}
