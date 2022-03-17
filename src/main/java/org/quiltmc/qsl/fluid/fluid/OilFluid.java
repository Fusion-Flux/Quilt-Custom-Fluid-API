package org.quiltmc.qsl.fluid.fluid;

import org.quiltmc.qsl.fluid.QuiltFluidAPI;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OilFluid extends QuiltFluid implements FlowableFluidExtensions {
	@Override
	public Fluid getStill() {
		return QuiltFluidAPI.STILL_OIL;
	}
	
	@Override
	public int getLevel(FluidState state) {
		return state.get(LEVEL);
	}
	
	@Override
	public Fluid getFlowing() {
		return QuiltFluidAPI.FLOWING_OIL;
	}
	
	@Override
	public Item getBucketItem() {
		return QuiltFluidAPI.OIL_BUCKET;
	}
	
	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return QuiltFluidAPI.OIL.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}
	
	@Override
	public boolean isStill(FluidState state) {
		return false;
	}
	
	@Override
	public int getColor(FluidState state, World world, BlockPos pos) {
		return 0x99ff33;
	}

	@Override
	public float getHorizontalViscosity(FluidState state, Entity effected) {
		return 0.5f;
	}

	@Override
	public float getVerticalViscosity(FluidState state, Entity affected) {
		return 0.5f;
	}

	@Override
	public boolean canSprintSwim(FluidState state, Entity effected) {
		return true;
	}
	
	@Override
	public int getFogColor(FluidState state, Entity effected) {
		//Set the fog color to #99ff33 for a light green acid.
		return 0x99ff33;
	}

	@Override
	public boolean canExtinguish(FluidState state, Entity entity) {
		return false;
	}

	@Override
	public boolean canIgnite(FluidState state, Entity affected) {
		return false;
	}

	@Override
	public float modifyHorizontalViscosity(LivingEntity entity, float horizVisc) {
		if (entity.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
			horizVisc = 0.96F;
		}
		return horizVisc;
	}

	
	public Direction getFlowAxis() {
		return Direction.NORTH;
	}
	
	@Nullable
	public ParticleEffect getParticle() {
		return ParticleTypes.DRIPPING_HONEY;
	}
	
	public static class Flowing extends OilFluid {
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}
	}
	
	public static class Still extends OilFluid {
		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}
		
	}
}
