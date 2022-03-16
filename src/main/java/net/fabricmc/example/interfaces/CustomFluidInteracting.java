package net.fabricmc.example.interfaces;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;

public interface CustomFluidInteracting {
	boolean isInCustomFluid();
	boolean isSubmergedInCustomFluid();
	boolean isSubmergedInCustomFluid(TagKey<Fluid> fluidTag);
}
