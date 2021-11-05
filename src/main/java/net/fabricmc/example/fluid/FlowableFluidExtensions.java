package net.fabricmc.example.fluid;

import net.fabricmc.example.misc.CustomProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.model.BlockStateVariantMap;
import net.minecraft.entity.Entity;
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

public interface FlowableFluidExtensions {

    public float getHorizontalViscosity(FluidState state, Entity entity);

    public float getVerticalViscosity(FluidState state, Entity entity);

    public float getPushStrength(FluidState state, Entity entity);

    public boolean canSwimIn(FluidState state, Entity entity);

    public boolean enableDepthStrider(FluidState state, Entity entity);

    public boolean enableDolphinsGrace(FluidState state, Entity entity);

}