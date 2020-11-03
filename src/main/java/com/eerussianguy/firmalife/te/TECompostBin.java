package com.eerussianguy.firmalife.te;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.lwjgl.Sys;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.eerussianguy.firmalife.registry.BlocksFL;
import net.dries007.tfc.objects.te.TEBase;
import net.dries007.tfc.objects.te.TEInventory;

public class TECompostBin extends TEBase implements ITickable
{
    private TECompostBin master;
    private boolean isMaster;
    private boolean hasRun = false;

    private int capacity = 0;
    private float fillAmountOrganic = 0;
    private float fillAmountCompost = 0;

    private long startTick;

    public boolean isMaster() { return isMaster; }

    @Override
    public void update()
    {
        if (!world.isRemote && !hasRun)
        {
            initializeMultiblock();
            hasRun = true;
        }
    }

    public TECompostBin getMaster() {
        initializeMultiblock();
        return master;
    }

    private void setMaster(TECompostBin master, int count)
    {
        this.master = master;
        isMaster = master == this;
        if(isMaster)
        {
            this.capacity = (count * 2000); //todo: Config for this value
        }
    }

    @Override
    public void invalidate()
    {
        List<TECompostBin> toUpdate = new ArrayList<>(); //todo: make this more efficient
        super.invalidate();
        for (EnumFacing d : EnumFacing.HORIZONTALS)
        {
            TileEntity current = world.getTileEntity(this.getPos().offset(d));
            if (current instanceof TECompostBin)
            {
                TECompostBin binCurrent = (TECompostBin) current;
                if(!toUpdate.contains(binCurrent))
                    toUpdate.add(binCurrent);
            }
        }
        for(TECompostBin current : toUpdate)
        {
            current.master = null;
            current.initializeMultiblock();
        }
    }

    private void initializeMultiblock()
    {
        /**
         *  if this doesn't have a master
         *      add this to stack of bins to search
         *      declare this as the master
         *      while we still have something to search
         *          pop a bin from the stack
         *          if that bin is a master
         *              declare it as the master
         *          add the popped bin to the list of bins searched
         *          for the four horizontal directions
         *              get the block at that position
         *              if its a bin and if it hasn't already been searched
         *                  add it to the stack of bins to search
         *      once all bins are searched, for every bin searched
         *          set that bins master to the declared master
         *
         **/

        if (master == null || master.isInvalid())
        {
            List<TECompostBin> valid = new ArrayList<>();
            Stack<TECompostBin> traversal = new Stack<>();
            TECompostBin master = this;
            traversal.add(this);
            while (!traversal.isEmpty())
            {
                TECompostBin current = traversal.pop();
                if (current.isMaster())
                    master = current;

                valid.add(current);
                for (EnumFacing face : EnumFacing.HORIZONTALS)
                {
                    TileEntity next = world.getTileEntity(current.getPos().offset(face));
                    if (next instanceof TECompostBin && !valid.contains(next) && !traversal.contains(next))
                        traversal.add((TECompostBin) next);
                }
            }
            //System.out.println("Master set to " + master.getPos().toString() + " for " + valid.size() + " blocks");
            for (TECompostBin current : valid)
                current.setMaster(master, valid.size());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        System.out.println("Reading NBT data");
        isMaster = nbt.getBoolean("isMaster");
        capacity = nbt.getInteger("capacity");
        fillAmountOrganic = nbt.getFloat("fillAmountOrganic");
        fillAmountCompost = nbt.getFloat("fillAmountCompost");
        startTick = nbt.getLong("startTick");
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        System.out.println("Writing NBT data");
        nbt.setBoolean("isMaster", isMaster);
        nbt.setInteger("capacity", capacity);
        nbt.setFloat("fillAmountOrganic", fillAmountOrganic);
        nbt.setFloat("fillAmountCompost", fillAmountCompost);
        nbt.setLong("startTick", startTick);
        return super.writeToNBT(nbt);
    }
}
