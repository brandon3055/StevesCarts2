package vswe.stevescarts.modules.workers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.api.modules.ModuleBase;

public abstract class ModuleWorker extends ModuleBase
{
    private boolean preWork;
    private boolean shouldDie;

    public ModuleWorker(final EntityMinecartModular cart)
    {
        super(cart);
        preWork = true;
    }

    public abstract byte getWorkPriority();

    public abstract boolean work();

    public void startWorking(final int time)
    {
        getCart().setWorkingTime(time);
        preWork = false;
        getCart().setWorker(this);
    }

    public void stopWorking()
    {
        if (getCart().getWorker() == this)
        {
            preWork = true;
            getCart().setWorker(null);
        }
    }

    public boolean preventAutoShutdown()
    {
        return false;
    }

    public void kill()
    {
        shouldDie = true;
    }

    public boolean isDead()
    {
        return shouldDie;
    }

    public void revive()
    {
        shouldDie = false;
    }

    protected boolean doPreWork()
    {
        return preWork;
    }

    public BlockPos getLastblock()
    {
        return getNextblock(false);
    }

    public BlockPos getNextblock()
    {
        return getNextblock(true);
    }

    private BlockPos getNextblock(final boolean flag)
    {
        BlockPos pos = getCart().getOnPos();
        if (BaseRailBlock.isRail(getCart().level, pos.below()))
        {
            pos = pos.below();
        }
        BlockState blockState = getCart().level.getBlockState(pos);
        if (BaseRailBlock.isRail(blockState))
        {
            RailShape direction = ((BaseRailBlock) blockState.getBlock()).getRailDirection(blockState, getCart().level, pos, getCart());
            if (direction.isAscending())
            {
                pos = pos.above();
            }

            int[][] aint = DismountHelper.offsetsForDirection(getCart().getMotionDirection());
            BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
            for (int[] aint1 : aint)
            {
                blockpos$mutable.set(pos.getX() + aint1[0], pos.getY() - 1, pos.getZ() + aint1[1]);
            }

            return blockpos$mutable;
        }
        return getCart().blockPosition();
    }

    @Override
    public float getMaxSpeed()
    {
        if (!doPreWork())
        {
            return 0.0f;
        }
        return super.getMaxSpeed();
    }

    protected boolean isValidForTrack(BlockPos pos, boolean flag)
    {
        boolean result = RailBlock.canSupportCenter(getCart().level, pos.below(), Direction.NORTH);
        if (result)
        {
            int coordX = pos.getX() - (getCart().x() - pos.getX());
            int coordZ = pos.getZ() - (getCart().z() - pos.getZ());
            Block block = getCart().level.getBlockState(new BlockPos(coordX, pos.getY(), coordZ)).getBlock();
            boolean isWater = block == Blocks.WATER || block == Blocks.ICE;
            boolean isLava = block == Blocks.LAVA;
            boolean isOther = block instanceof IFluidBlock;
            boolean isLiquid = isWater || isLava || isOther;
            result = !isLiquid;
        }
        return result;
    }
}
