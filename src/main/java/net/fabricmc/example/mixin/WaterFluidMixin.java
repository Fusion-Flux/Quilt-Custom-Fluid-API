package net.fabricmc.example.mixin;

import net.fabricmc.example.fluid.FlowableFluidExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin extends FlowableFluid implements FlowableFluidExtensions {
    @Override
    public float defaultTemperature(World world, BlockPos blockPos) {
        return 300;
    }
    @Override
    public float defaultDensity(World world, BlockPos blockPos) {
        return 1000;
    }

    @Override
    public float getHorizontalViscosity(Entity entity) {
        return 0.8f;
    }

    @Override
    public float getVerticalViscosity(Entity entity) {
        return 0.800000011920929f;
    }

    @Override
    public float getPushStrength(Entity entity) {
        return 0.014f;
    }

}
