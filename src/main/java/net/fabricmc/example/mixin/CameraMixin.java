package net.fabricmc.example.mixin;

import net.fabricmc.example.interfaces.CameraInterface;
import net.fabricmc.example.interfaces.CustomFluidInterface;
import net.minecraft.client.render.Camera;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Camera.class)
public class CameraMixin implements CameraInterface {
    @Shadow
    private BlockView area;
    @Shadow @Final
    private BlockPos.Mutable blockPos;

    /**
     * Returns the fluid in which the camera is submerged.
     */
    @Override
    public FluidState getSubmergedFluidState() {
        return this.area.getFluidState(this.blockPos);
    }
}
