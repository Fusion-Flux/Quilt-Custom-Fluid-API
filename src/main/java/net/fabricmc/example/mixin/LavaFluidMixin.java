package net.fabricmc.example.mixin;

import net.fabricmc.example.fluid.FlowableFluidExtensions;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin extends FlowableFluid implements FlowableFluidExtensions {
    @Override
    public float defaultTemperature(World world, BlockPos blockPos) {
        return world.getDimension().isUltrawarm() ? 1500 : 1200;
    }
    @Override
    public float defaultDensity(World world, BlockPos blockPos) {
        return 1300;
    }

    @Override
    public float getHorizontalViscosity(Entity entity) {
        return  0.5f;
    }

    @Override
    public float getVerticalViscosity(Entity entity) {
        return 0.800000011920929f;
    }

    @Override
    public float getPushStrength(Entity entity) {
        return entity.world.getDimension().isUltrawarm() ? 0.0023333333333333335f : .007f;
    }

}
