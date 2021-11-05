package net.fabricmc.example.fluid;

import net.fabricmc.example.misc.CustomProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.model.BlockStateVariantMap;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.HashMap;
import java.util.Map;

public abstract class FlowableFluidExtension extends FlowableFluid {

    public abstract float getViscosity(FluidState state);

    public abstract float getVerticalViscosity(FluidState state);

    public abstract float getPushStrength(FluidState state);

    public abstract boolean canSwimIn(FluidState state);
}
