//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package M6FGR.dualgreatswords.gameassets;

import M6FGR.dualgreatswords.api.animation.types.AirAttackAnimation;
import M6FGR.dualgreatswords.api.animation.types.KnockUpAnimation;
import M6FGR.dualgreatswords.api.animation.types.SimpleAttackAnimation;
import M6FGR.dualgreatswords.api.animation.types.SimpleNoRotAttackAnimation;
import M6FGR.dualgreatswords.main.DualGreatSwords;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationEvent.Side;
import yesman.epicfight.api.animation.property.AnimationProperty.ActionAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.animation.types.AttackAnimation.JointColliderPair;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Random;

@EventBusSubscriber(
        modid = DualGreatSwords.MOD_ID,
        bus = Bus.MOD
)
public class DualGreatSwordsAnimations {
    public static AnimationManager.AnimationAccessor<? extends DashAttackAnimation> GREATSWORD_DUAL_DASH;
    public static AnimationManager.AnimationAccessor<? extends AirAttackAnimation> GREATSWORD_DUAL_AIRSLASH;
    public static AnimationManager.AnimationAccessor<? extends StaticAnimation> GREATSWORD_DUAL_IDLE;
    public static AnimationManager.AnimationAccessor<? extends MovementAnimation> GREATSWORD_DUAL_WALK;
    public static AnimationManager.AnimationAccessor<? extends MovementAnimation> GREATSWORD_DUAL_RUN;
    public static AnimationManager.AnimationAccessor<? extends SimpleAttackAnimation> GREATSWORD_DUAL_AUTO_1;
    public static AnimationManager.AnimationAccessor<? extends KnockUpAnimation> GREATSWORD_DUAL_AUTO_2;
    public static AnimationManager.AnimationAccessor<? extends AttackAnimation> GREATSWORD_DUAL_AUTO_3;
    public static AnimationManager.AnimationAccessor<? extends SimpleAttackAnimation> GREATSWORD_DUAL_AUTO_4;
    public static AnimationManager.AnimationAccessor<? extends AttackAnimation> GREATSWORD_EARTH_QUAKE;

    public DualGreatSwordsAnimations() {
    }

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(DualGreatSwords.MOD_ID, DualGreatSwordsAnimations::build);
        DualGreatSwords.LOGGER.info("Registered animations");
    }

     private static void build(AnimationManager.AnimationBuilder builder) {
        Joint toolR = Armatures.BIPED.get().toolR;
        Joint toolL = Armatures.BIPED.get().toolL;
        Armatures.ArmatureAccessor<HumanoidArmature> biped = Armatures.BIPED;
        GREATSWORD_DUAL_AUTO_1 = builder.nextAccessor("biped/combat/greatsword_dual_auto_1", accessor -> {
            return new SimpleAttackAnimation(0.15F, accessor, biped,
                    new AttackAnimation.Phase(0.0F, 0.2F, 0.2F, 0.4F, 0.45F, 0.45F, InteractionHand.OFF_HAND, toolL, null)
                    .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT)
                    .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.32F)),
            new AttackAnimation.Phase(0.3F, 0.5F, 0.5F, 0.7F, 0.8F, 1.6F, InteractionHand.MAIN_HAND, toolR, null)
                    .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.32F))
                    .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT))
                    .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
        });
        GREATSWORD_DUAL_AUTO_2 = builder.nextAccessor("biped/combat/greatsword_dual_auto_2", accessor -> {
            return new KnockUpAnimation(0.15F, accessor, biped,
                    new AttackAnimation.Phase(0.0F, 0.18F, 0.35F, 0.85F, 1.05F, 1.4F, InteractionHand.MAIN_HAND,
                            JointColliderPair.of(toolR, null), JointColliderPair.of(toolL, null))
                            .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.31F))
                            .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.FALL))
                    .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F)
                    .addEvents(AnimationEvent.InTimeEvent.create(0.81F, AnimationEvents.GROUNDSLAM_SMALL, Side.BOTH));
        });
        GREATSWORD_DUAL_AUTO_3 = builder.nextAccessor("biped/combat/greatsword_dual_auto_3", accessor -> {
                    return new AttackAnimation(0.15F, accessor, biped,
                            new AttackAnimation.Phase(0.0F, 0.2F, 0.4F, 0.45F, 0.45F, toolR, null)
                                    .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT)
                                    .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.32F)),
                            new AttackAnimation.Phase(0.0F, 0.55F, 0.7F, 0.8F, 1.6F, InteractionHand.OFF_HAND, toolL, null)
                                    .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.333F)))
                            .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT)
                            .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F)
                            .addEvents(AnimationEvent.InTimeEvent.create(0.34F, AnimationEvents.GROUNDSLAM_SMALL, Side.BOTH));
                });
        GREATSWORD_DUAL_AUTO_4 = builder.nextAccessor("biped/combat/greatsword_dual_auto_4", accessor -> {
            return new SimpleAttackAnimation(0.1F, accessor, biped,
                    new AttackAnimation.Phase(0.0F, 0.5F, 0.75F, 0.85F, 1.4F, 1.9F, InteractionHand.MAIN_HAND,
                            JointColliderPair.of(toolR, null), JointColliderPair.of(toolL, null))
                            .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.32F))
                            .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG))
                            .removeProperty(StaticAnimationProperty.POSE_MODIFIER)
                            .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.12F);
        });
        GREATSWORD_EARTH_QUAKE = builder.nextAccessor("biped/skill/greatsword_dual_earthquake", accessor -> {
            return new AttackAnimation(0.1F, accessor, biped,
                    new AttackAnimation.Phase(0.0F, 1.1F, 1.1F, 1.25F, 1.75F, Float.MAX_VALUE, InteractionHand.MAIN_HAND,
                            JointColliderPair.of(toolR,  null), JointColliderPair.of(toolL, null))
                            .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG))
                            .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F)
                            .addEvents(AnimationEvent.InTimeEvent.create(1.2F, AnimationEvents.GROUNDSLAM_SMALL, Side.BOTH));
        });
        GREATSWORD_DUAL_AIRSLASH = builder.nextAccessor("biped/combat/greatsword_dual_airslash", accessor -> {
            return new AirAttackAnimation(0.1F, 0.26F, 0.4F, 1.3F, DualGreatSwordsColliderPresets.SLAM, biped.get().rootJoint, accessor, biped)
                    .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                    .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.32F))
                    .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F)
                    .addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
                    .addProperty(ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 0.2F))
                    .addProperty(StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
                        if (elapsedTime >= 0.2F && elapsedTime < 0.35F) {
                            float dpx = (float) entitypatch.getOriginal().getX();
                            float dpy = (float) entitypatch.getOriginal().getY();
                            float dpz = (float) entitypatch.getOriginal().getZ();

                            for (BlockState block = entitypatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz)); (block.getBlock() instanceof BushBlock || block.isAir()) && !block.is(Blocks.VOID_AIR); block = entitypatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz))) {
                                --dpy;
                            }

                            float distanceToGround = (float) Math.max(Math.abs(entitypatch.getOriginal().getY() - (double) dpy) - 1.0, 0.0);
                            return 1.0F - (1.0F / (-distanceToGround - 1.0F) + 1.0F);
                        } else {
                            return speed;
                        }
                    }).addEvents(AnimationEvent.InTimeEvent.create(0.35F, AnimationEvents.GROUNDSLAM_SMALL, Side.BOTH));
        });
        GREATSWORD_DUAL_DASH = builder.nextAccessor("biped/combat/greatsword_dual_dash", accessor -> {
            return new DashAttackAnimation(0.05F, 0.1F, 0.15F, 0.25F, 0.8F, DualGreatSwordsColliderPresets.SLASH, biped.get().rootJoint, accessor, biped)
                    .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT)
                    .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
                    .addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT.get())
                    .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.32F))
                    .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.1F);
        });
        GREATSWORD_DUAL_WALK = builder.nextAccessor("biped/living/greatsword_dual_walk", accessor -> {
            return new MovementAnimation(0.15F, true, accessor, biped);
        });
        GREATSWORD_DUAL_IDLE = builder.nextAccessor("biped/living/greatsword_dual_idle", accessor -> {
            return new StaticAnimation(0.15f,true, accessor, biped);
        });
        GREATSWORD_DUAL_RUN = builder.nextAccessor("biped/living/greatsword_dual_run", accessor -> {
            return new MovementAnimation(0.15F, true, accessor, biped);
        });
    }

    public static class AnimationEvents {
        static Joint toolR = Armatures.BIPED.get().toolR;
        @SuppressWarnings("removal")
        public static final AnimationEvent.Event<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> GROUNDSLAM_SMALL = (entitypatch, self, params) -> {
            Vec3 position = entitypatch.getOriginal().position();
            OpenMatrix4f modelTransform = entitypatch.getArmature().getBindedTransformFor(entitypatch.getAnimator().getPose(0.0F), toolR).mulFront(OpenMatrix4f.createTranslation((float) position.x, (float) position.y, (float) position.z).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(entitypatch.getModelMatrix(1.0F))));
            Vec3 weaponEdge = OpenMatrix4f.transform(modelTransform, (new Vec3f(0.0F, 0.0F, -1.4F)).toDoubleVector());
            Level level = entitypatch.getOriginal().level();
            Vec3 floorPos = Vec3S(entitypatch, self, new Vec3f(0.0F, 0.0F, -1.4F), toolR);
            BlockState blockState = entitypatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(floorPos.x, floorPos.y, floorPos.z));
            if (entitypatch instanceof PlayerPatch) {
                entitypatch.getOriginal().level().playSound((Player) entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.SLAM_HEAVY.get(), SoundSource.PLAYERS, 1.5F, 1.5F - ((new Random()).nextFloat() - 0.5F) * 0.2F);
            }

            weaponEdge = new Vec3(weaponEdge.x, floorPos.y, weaponEdge.z);
            LevelUtil.circleSlamFracture(entitypatch.getOriginal(), level, weaponEdge, 2.0, true, false, false);
        };

        @SuppressWarnings("removal")
        // for some reason it needs to be here too
        public static Vec3 Vec3S(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends StaticAnimation> self, Vec3f WeaponOffset, Joint joint) {
            float dpx = WeaponOffset.x + (float) entitypatch.getOriginal().getX();
            float dpy = WeaponOffset.y + (float) entitypatch.getOriginal().getY();
            float dpz = WeaponOffset.z + (float) entitypatch.getOriginal().getZ();
            if (joint != null) {
                OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getAnimator().getPose(1.0F), joint);
                transformMatrix.translate(WeaponOffset);
                OpenMatrix4f CORRECTION = (new OpenMatrix4f()).rotate(-((float) Math.toRadians(entitypatch.getOriginal().yRotO + 180.0F)), new Vec3f(0.0F, 1.0F, 0.0F));
                OpenMatrix4f.mul(CORRECTION, transformMatrix, transformMatrix);
                dpx = transformMatrix.m30 + (float) entitypatch.getOriginal().getX();
                dpy = transformMatrix.m31 + (float) entitypatch.getOriginal().getY();
                dpz = transformMatrix.m32 + (float) entitypatch.getOriginal().getZ();
            }

            for (BlockState block = entitypatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz)); (block.getBlock() instanceof BushBlock || block.isAir()) && !block.is(Blocks.VOID_AIR) && dpy > -64.0F; block = entitypatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz))) {
                --dpy;
            }

            return new Vec3(dpx, dpy, dpz);
        }
    }

}
