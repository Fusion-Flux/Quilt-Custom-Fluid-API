package net.fabricmc.example.interfaces;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public interface CustomFluidInterface {
	boolean isInCustomFluid();
	boolean isSubmergedInCustomFluid();
	boolean isSubmergedInCustomFluid(Tag<Fluid> fluidTag);
}
