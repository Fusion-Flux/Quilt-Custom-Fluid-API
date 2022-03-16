package org.quiltmc.qsl.fluid.mixin;

import org.quiltmc.qsl.fluid.fluid.FlowableFluidExtensions;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin extends FlowableFluid implements FlowableFluidExtensions {
	// just need to implement the interface, defaults are based on water

}
