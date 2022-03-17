package org.quiltmc.qsl.fluid;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ModInitializer;
import org.quiltmc.qsl.fluid.fluid.OilFluid;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuiltFluidAPI implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("quilt_fluid_api");
	public static final TagKey<Fluid> QUILT_FLUIDS = TagKey.of(Registry.FLUID_KEY,new Identifier("quilt_fluid_api", "quilt_fluid"));
	public static FlowableFluid STILL_OIL;
	public static FlowableFluid FLOWING_OIL;
	public static Item OIL_BUCKET;
	public static Block OIL;
	//public static Tag<Fluid> FABRIC_FLUIDS = TagRegistry.fluid(new Identifier("modid", "fabric_fluid"));
	
	@Override
	public void onInitialize() {
		STILL_OIL = Registry.register(Registry.FLUID, new Identifier("quilt_fluid_api", "oil"), new OilFluid.Still());
		FLOWING_OIL = Registry.register(Registry.FLUID, new Identifier("quilt_fluid_api", "flowing_oil"), new OilFluid.Flowing());
		OIL_BUCKET = Registry.register(Registry.ITEM, new Identifier("quilt_fluid_api", "oil_bucket"),
				new BucketItem(STILL_OIL, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));
		
		OIL = Registry.register(Registry.BLOCK, new Identifier("quilt_fluid_api", "oil"), new FluidBlock(STILL_OIL, FabricBlockSettings.copy(Blocks.WATER)) {
		});
		
		
		LOGGER.info("Hello Fabric world!");
	}
}
