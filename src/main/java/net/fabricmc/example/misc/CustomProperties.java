package net.fabricmc.example.misc;

import net.minecraft.data.client.model.BlockStateVariantMap;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.Direction;

public class CustomProperties {

    public static final IntProperty VISCOSITY;

    static {
        VISCOSITY = IntProperty.of("viscosity", 0, 9999);
    }

}
