package net.fabricmc.example.mixin;

import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.fluid.FlowableFluidExtensions;
import net.fabricmc.example.interfaces.CustomFluidInterface;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract boolean canMoveVoluntarily();

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    protected abstract boolean shouldSwimInFluids();

    @Shadow
    public abstract boolean canWalkOnFluid(Fluid fluid);

    @Shadow
    protected abstract float getBaseMovementSpeedMultiplier();

    @Shadow
    public abstract float getMovementSpeed();

    @Shadow
    public abstract boolean isClimbing();

    @Shadow
    public abstract Vec3d method_26317(double d, boolean bl, Vec3d vec3d);

    @Shadow
    public abstract boolean isFallFlying();

    @Shadow
    protected abstract SoundEvent getFallSound(int distance);


    @Shadow
    @Nullable
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow
    public abstract boolean hasNoDrag();

    @Shadow
    public abstract void updateLimbs(LivingEntity entity, boolean flutter);

    @Shadow
    public abstract Vec3d applyMovementInput(Vec3d movementInput, float slipperiness);

    @Shadow
    private int jumpingCooldown;

    @Shadow
    protected int bodyTrackingIncrements;

    @Shadow
    protected double serverX;

    @Shadow
    protected double serverY;

    @Shadow
    protected double serverZ;

    @Shadow
    protected double serverYaw;

    @Shadow
    protected double serverPitch;

    @Shadow
    protected int headTrackingIncrements;

    @Shadow
    public float headYaw;

    @Shadow
    protected double serverHeadYaw;

    @Shadow
    protected abstract boolean isImmobile();

    @Shadow
    protected boolean jumping;

    @Shadow
    public float sidewaysSpeed;

    @Shadow
    public float forwardSpeed;

    @Shadow
    protected abstract void tickNewAi();

    @Shadow
    protected abstract void swimUpward(Tag<Fluid> fluid);

    @Shadow
    protected abstract void jump();

    @Shadow
    protected abstract void tickFallFlying();

    @Shadow
    public float upwardSpeed;

    @Shadow
    public abstract boolean isDead();

    @Shadow
    protected abstract void removePowderSnowSlow();

    @Shadow
    protected abstract void addPowderSnowSlowIfNeeded();

    @Shadow
    protected int riptideTicks;

    @Shadow
    protected abstract void tickRiptide(Box a, Box b);

    @Shadow
    protected abstract void tickCramming();

    @Shadow
    public abstract boolean hurtByWater();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * @author
     */
    @Overwrite
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() || this.isLogicalSideForUpdatingMovement()) {
            double d = 0.08D;
            boolean bl = this.getVelocity().y <= 0.0D;
            if (bl && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                d = 0.01D;
                this.fallDistance = 0.0F;
            }

            FluidState fluidState = this.world.getFluidState(this.getBlockPos());
            float j;
            double e;
            if (this.isTouchingWater() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState.getFluid())) {
                e = this.getY();
                j = this.isSprinting() ? 0.9F : this.getBaseMovementSpeedMultiplier();

                float g = 0.02F;
                float h = (float) EnchantmentHelper.getDepthStrider(((LivingEntity) (Object) this));
                if (h > 3.0F) {
                    h = 3.0F;
                }

                if (!this.onGround) {
                    h *= 0.5F;
                }

                if (h > 0.0F) {
                    j += (0.54600006F - j) * h / 3.0F;
                    g += (this.getMovementSpeed() - g) * h / 3.0F;
                }

                if (this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                    j = 0.96F;
                }

                this.updateVelocity(g, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d = this.getVelocity();
                if (this.horizontalCollision && this.isClimbing()) {
                    vec3d = new Vec3d(vec3d.x, 0.2D, vec3d.z);
                }

                this.setVelocity(vec3d.multiply((double) j, 0.800000011920929D, (double) j));
                Vec3d vec3d2 = this.method_26317(d, bl, this.getVelocity());
                this.setVelocity(vec3d2);
                if (this.horizontalCollision && this.doesNotCollide(vec3d2.x, vec3d2.y + 0.6000000238418579D - this.getY() + e, vec3d2.z)) {
                    this.setVelocity(vec3d2.x, 0.30000001192092896D, vec3d2.z);
                }
            } else if (this.isInLava() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState.getFluid())) {
                e = this.getY();
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d4;
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getSwimHeight()) {
                    this.setVelocity(this.getVelocity().multiply(0.5D, 0.800000011920929D, 0.5D));
                    vec3d4 = this.method_26317(d, bl, this.getVelocity());
                    this.setVelocity(vec3d4);
                } else {
                    this.setVelocity(this.getVelocity().multiply(0.5D));
                }

                if (!this.hasNoGravity()) {
                    this.setVelocity(this.getVelocity().add(0.0D, -d / 4.0D, 0.0D));
                }

                vec3d4 = this.getVelocity();
                if (this.horizontalCollision && this.doesNotCollide(vec3d4.x, vec3d4.y + 0.6000000238418579D - this.getY() + e, vec3d4.z)) {
                    this.setVelocity(vec3d4.x, 0.30000001192092896D, vec3d4.z);
                }
            } else if (((CustomFluidInterface) this).isInCustomFluid() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState.getFluid())) { //custom code here
                e = this.getY();
                float horizVisc = 0.8F;
                float vertVisc = 0.800000011920929F;

                if ((fluidState.getFluid() instanceof FlowableFluidExtensions fluid)) {

                    horizVisc = this.isSprinting() ? 0.9f : fluid.getHorizontalViscosity(this);

                    vertVisc = fluid.getVerticalViscosity(this);
                }
                float g = 0.02F;
                //

                if ((fluidState.getFluid() instanceof FlowableFluidExtensions fluid)) {
                    if (fluid.enableDepthStrider(this)) {
                        float h = (float) EnchantmentHelper.getDepthStrider(((LivingEntity) (Object) this));
                        if (h > 3.0F) {
                            h = 3.0F;
                        }

                        if (!this.onGround) {
                            h *= 0.5F;
                        }

                        if (h > 0.0F) {
                            horizVisc += (0.54600006F - horizVisc) * h / 3.0F;
                            g += (this.getMovementSpeed() - g) * h / 3.0F;
                        }
                    }
                    if (fluid.enableDolphinsGrace(this)) {
                        if (this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                            horizVisc = 0.96F;
                        }
                    }
                }
                //
                this.updateVelocity(g, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d = this.getVelocity();
                if (this.horizontalCollision && this.isClimbing()) {
                    vec3d = new Vec3d(vec3d.x, 0.2D, vec3d.z);
                }

                this.setVelocity(vec3d.multiply((double) horizVisc, vertVisc, (double) horizVisc));
                Vec3d vec3d2 = this.method_26317(d, bl, this.getVelocity());
                this.setVelocity(vec3d2);
                if (this.horizontalCollision && this.doesNotCollide(vec3d2.x, vec3d2.y + 0.6000000238418579D - this.getY() + e, vec3d2.z)) {
                    this.setVelocity(vec3d2.x, 0.30000001192092896D, vec3d2.z);
                }
            } else if (this.isFallFlying()) {
                Vec3d vec3d5 = this.getVelocity();
                if (vec3d5.y > -0.5D) {
                    this.fallDistance = 1.0F;
                }

                Vec3d vec3d6 = this.getRotationVector();
                j = this.getPitch() * 0.017453292F;
                double k = Math.sqrt(vec3d6.x * vec3d6.x + vec3d6.z * vec3d6.z);
                double l = vec3d5.horizontalLength();
                double m = vec3d6.length();
                float n = MathHelper.cos(j);
                n = (float) ((double) n * (double) n * Math.min(1.0D, m / 0.4D));
                vec3d5 = this.getVelocity().add(0.0D, d * (-1.0D + (double) n * 0.75D), 0.0D);
                double q;
                if (vec3d5.y < 0.0D && k > 0.0D) {
                    q = vec3d5.y * -0.1D * (double) n;
                    vec3d5 = vec3d5.add(vec3d6.x * q / k, q, vec3d6.z * q / k);
                }

                if (j < 0.0F && k > 0.0D) {
                    q = l * (double) (-MathHelper.sin(j)) * 0.04D;
                    vec3d5 = vec3d5.add(-vec3d6.x * q / k, q * 3.2D, -vec3d6.z * q / k);
                }

                if (k > 0.0D) {
                    vec3d5 = vec3d5.add((vec3d6.x / k * l - vec3d5.x) * 0.1D, 0.0D, (vec3d6.z / k * l - vec3d5.z) * 0.1D);
                }

                this.setVelocity(vec3d5.multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D));
                this.move(MovementType.SELF, this.getVelocity());
                if (this.horizontalCollision && !this.world.isClient) {
                    q = this.getVelocity().horizontalLength();
                    double r = l - q;
                    float s = (float) (r * 10.0D - 3.0D);
                    if (s > 0.0F) {
                        this.playSound(this.getFallSound((int) s), 1.0F, 1.0F);
                        this.damage(DamageSource.FLY_INTO_WALL, s);
                    }
                }

                if (this.onGround && !this.world.isClient) {
                    this.setFlag(7, false);
                }
            } else {
                BlockPos blockPos = this.getVelocityAffectingPos();
                float t = this.world.getBlockState(blockPos).getBlock().getSlipperiness();
                j = this.onGround ? t * 0.91F : 0.91F;
                Vec3d vec3d7 = this.applyMovementInput(movementInput, t);
                double v = vec3d7.y;
                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                    v += (0.05D * (double) (this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec3d7.y) * 0.2D;
                    this.fallDistance = 0.0F;
                } else if (this.world.isClient && !this.world.isChunkLoaded(blockPos)) {
                    if (this.getY() > (double) this.world.getBottomY()) {
                        v = -0.1D;
                    } else {
                        v = 0.0D;
                    }
                } else if (!this.hasNoGravity()) {
                    v -= d;
                }

                if (this.hasNoDrag()) {
                    this.setVelocity(vec3d7.x, v, vec3d7.z);
                } else {
                    this.setVelocity(vec3d7.x * (double) j, v * 0.9800000190734863D, vec3d7.z * (double) j);
                }
            }
        }

        this.updateLimbs(((LivingEntity) (Object) this), this instanceof Flutterer);
    }


    /**
     * @author
     */
    @Overwrite
    public void tickMovement() {
        if (this.jumpingCooldown > 0) {
            --this.jumpingCooldown;
        }

        if (this.isLogicalSideForUpdatingMovement()) {
            this.bodyTrackingIncrements = 0;
            this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
        }

        if (this.bodyTrackingIncrements > 0) {
            double d = this.getX() + (this.serverX - this.getX()) / (double) this.bodyTrackingIncrements;
            double e = this.getY() + (this.serverY - this.getY()) / (double) this.bodyTrackingIncrements;
            double f = this.getZ() + (this.serverZ - this.getZ()) / (double) this.bodyTrackingIncrements;
            double g = MathHelper.wrapDegrees(this.serverYaw - (double) this.getYaw());
            this.setYaw(this.getYaw() + (float) g / (float) this.bodyTrackingIncrements);
            this.setPitch(this.getPitch() + (float) (this.serverPitch - (double) this.getPitch()) / (float) this.bodyTrackingIncrements);
            --this.bodyTrackingIncrements;
            this.setPosition(d, e, f);
            this.setRotation(this.getYaw(), this.getPitch());
        } else if (!this.canMoveVoluntarily()) {
            this.setVelocity(this.getVelocity().multiply(0.98D));
        }

        if (this.headTrackingIncrements > 0) {
            this.headYaw = (float) ((double) this.headYaw + MathHelper.wrapDegrees(this.serverHeadYaw - (double) this.headYaw) / (double) this.headTrackingIncrements);
            --this.headTrackingIncrements;
        }

        Vec3d vec3d = this.getVelocity();
        double h = vec3d.x;
        double i = vec3d.y;
        double j = vec3d.z;
        if (Math.abs(vec3d.x) < 0.003D) {
            h = 0.0D;
        }

        if (Math.abs(vec3d.y) < 0.003D) {
            i = 0.0D;
        }

        if (Math.abs(vec3d.z) < 0.003D) {
            j = 0.0D;
        }

        this.setVelocity(h, i, j);
        this.world.getProfiler().push("ai");
        if (this.isImmobile()) {
            this.jumping = false;
            this.sidewaysSpeed = 0.0F;
            this.forwardSpeed = 0.0F;
        } else if (this.canMoveVoluntarily()) {
            this.world.getProfiler().push("newAi");
            this.tickNewAi();
            this.world.getProfiler().pop();
        }

        this.world.getProfiler().pop();
        this.world.getProfiler().push("jump");
        if (this.jumping && this.shouldSwimInFluids()) {
            double l;
            if (((CustomFluidInterface) this).isInCustomFluid()) {
                l = this.getFluidHeight(ExampleMod.FABRIC_FLUIDS);
            } else if (this.isInLava()) {
                l = this.getFluidHeight(FluidTags.LAVA);
            } else {
                l = this.getFluidHeight(FluidTags.WATER);
            }

            boolean bl = this.isTouchingWater() && l > 0.0D;
            boolean blc = (((CustomFluidInterface) this).isInCustomFluid()) && l > 0.0D;
            double m = this.getSwimHeight();

            if (bl && (!this.onGround || l > m)) {
                this.swimUpward(FluidTags.WATER);
            } else if (blc && (!this.onGround || l > m)) {
                this.swimUpward(ExampleMod.FABRIC_FLUIDS);
            } else if (!this.isInLava() || this.onGround && !(l > m)) {
                if ((this.onGround || bl && l <= m) && this.jumpingCooldown == 0) {
                    this.jump();
                    this.jumpingCooldown = 10;
                }
            } else {
                this.swimUpward(FluidTags.LAVA);
            }
        } else {
            this.jumpingCooldown = 0;
        }

        this.world.getProfiler().pop();
        this.world.getProfiler().push("travel");
        this.sidewaysSpeed *= 0.98F;
        this.forwardSpeed *= 0.98F;
        this.tickFallFlying();
        Box box = this.getBoundingBox();
        this.travel(new Vec3d((double) this.sidewaysSpeed, (double) this.upwardSpeed, (double) this.forwardSpeed));
        this.world.getProfiler().pop();
        this.world.getProfiler().push("freezing");
        boolean bl2 = this.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES);
        int o;
        if (!this.world.isClient && !this.isDead()) {
            o = this.getFrozenTicks();
            if (this.inPowderSnow && this.canFreeze()) {
                this.setFrozenTicks(Math.min(this.getMinFreezeDamageTicks(), o + 1));
            } else {
                this.setFrozenTicks(Math.max(0, o - 2));
            }
        }

        this.removePowderSnowSlow();
        this.addPowderSnowSlowIfNeeded();
        if (!this.world.isClient && this.age % 40 == 0 && this.isFreezing() && this.canFreeze()) {
            o = bl2 ? 5 : 1;
            this.damage(DamageSource.FREEZE, (float) o);
        }

        this.world.getProfiler().pop();
        this.world.getProfiler().push("push");
        if (this.riptideTicks > 0) {
            --this.riptideTicks;
            this.tickRiptide(box, this.getBoundingBox());
        }

        this.tickCramming();
        this.world.getProfiler().pop();
        if (!this.world.isClient && this.hurtByWater() && this.isWet()) {
            this.damage(DamageSource.DROWN, 1.0F);
        }

    }
}

