package M6FGR.dualgreatswords.api.animation.types;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.LinkAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class KnockUpAnimation extends AttackAnimation {
    public KnockUpAnimation(float convertTime, float antic, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends KnockUpAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        this(convertTime, antic, antic, contact, recovery, collider, colliderJoint, accessor, armature);
    }

    public KnockUpAnimation(float ConvertTime, float StopMovement, float Start, float End, float Recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends KnockUpAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        this(ConvertTime, accessor, armature, new AttackAnimation.Phase(0.0F, StopMovement, Start, End, Recovery, Float.MAX_VALUE, colliderJoint, collider));
    }

    public KnockUpAnimation(float convertTime, AnimationManager.AnimationAccessor<? extends KnockUpAnimation> accessor, AssetAccessor<? extends Armature> armature, AttackAnimation.Phase... phases) {
        super(convertTime, accessor, armature, phases);
        this.newTimePair(0.0F, Float.MAX_VALUE);
        this.addStateRemoveOld(EntityState.TURNING_LOCKED, true);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_TARGET_LOCATION_ROTATION);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
            LivingEntity attackTarget = entitypatch.getTarget();
            if (!self.getProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false) && attackTarget != null) {
                TransformSheet transform = self.getTransfroms().get("Root").copyAll();
                Keyframe[] keyframes = transform.getKeyframes();
                int startFrame = 0;
                int endFrame = transform.getKeyframes().length - 1;
                Vec3f keyLast = keyframes[endFrame].transform().translation();
                Vec3 pos = entitypatch.getOriginal().getEyePosition();
                Vec3 targetpos = attackTarget.position().add(attackTarget.getDeltaMovement().scale(8.0));
                float horizontalDistance = Math.max((float) targetpos.subtract(pos).horizontalDistance() * 1.3F - (attackTarget.getBbWidth() + entitypatch.getOriginal().getBbWidth()), 0.0F);
                Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
                float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);

                for (int i = startFrame; i <= endFrame; ++i) {
                    Vec3f translation = keyframes[i].transform().translation();
                    translation.z *= scale;
                }

                transformSheet.readFrom(transform);
            } else {
                transformSheet.readFrom(self.getTransfroms().get("Root"));
            }

        });
    }

    public void begin(LivingEntityPatch<?> entitypatch) {
        super.begin(entitypatch);
        entitypatch.setLastAttackSuccess(false);
    }

    protected void hurtCollidingEntities(LivingEntityPatch<?> entitypatch, float prevElapsedTime, float elapsedTime, EntityState prevState, EntityState state, AttackAnimation.Phase phase) {
        LivingEntity entity = entitypatch.getOriginal();
        float prevPoseTime = prevState.attacking() ? prevElapsedTime : phase.preDelay;
        float poseTime = state.attacking() ? elapsedTime : phase.contact;
        List<Entity> list = this.getPhaseByTime(elapsedTime).getCollidingEntities(entitypatch, this, prevPoseTime, poseTime, this.getPlaySpeed(entitypatch, this));
        if (!list.isEmpty()) {
            HitEntityList hitEntities = new HitEntityList(entitypatch, list, phase.getProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY).orElse(HitEntityList.Priority.DISTANCE));
            int maxStrikes = this.getMaxStrikes(entitypatch, phase);

            while (true) {
                Entity hitten;
                LivingEntity truehittenEntity;
                do {
                    do {
                        do {
                            do {
                                do {
                                    do {
                                        if (entitypatch.getCurrentlyActuallyHitEntities().size() >= maxStrikes || !hitEntities.next()) {
                                            return;
                                        }

                                        hitten = hitEntities.getEntity();
                                        truehittenEntity = this.getTrueEntity(hitten);
                                    } while (truehittenEntity == null);
                                } while (!truehittenEntity.isAlive());
                            } while (entitypatch.getCurrentlyActuallyHitEntities().contains(truehittenEntity));
                        } while (entitypatch.isTargetInvulnerable(hitten));
                    } while (!(hitten instanceof LivingEntity) && !(hitten instanceof PartEntity));
                } while (!entity.hasLineOfSight(hitten));

                HurtableEntityPatch<?> hitHurtableEntityPatch = (HurtableEntityPatch) EpicFightCapabilities.getEntityPatch(hitten, HurtableEntityPatch.class);
                EpicFightDamageSource source = this.getEpicFightDamageSource(entitypatch, hitten, phase);
                float anti_stunlock = 1.0F;
                if (hitHurtableEntityPatch != null) {
                    if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).isPresent()) {
                        if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.NONE) {
                            if (truehittenEntity instanceof Player) {
                                source.setStunType(StunType.LONG);
                                source.setBaseImpact((float) ((double) (source.getBaseImpact() * 4.0F) / (1.0 - truehittenEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))));
                            } else {
                                source.setStunType(StunType.NONE);
                            }
                        } else if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.HOLD && hitHurtableEntityPatch.getOriginal().hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())) {
                            source.setStunType(StunType.NONE);
                        } else if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.FALL && hitHurtableEntityPatch.getOriginal().hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())) {
                            source.setStunType(StunType.NONE);
                        } else if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.KNOCKDOWN && hitHurtableEntityPatch.getOriginal().hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())) {
                            source.setStunType(StunType.NONE);
                        } else {
                            source = this.getEpicFightDamageSource(entitypatch, hitten, phase);
                        }
                    } else {
                        source = this.getEpicFightDamageSource(entitypatch, hitten, phase);
                    }

                    Iterator var18;
                    String tag;
                    if (hitHurtableEntityPatch.isStunned()) {
                        var18 = hitten.getTags().iterator();

                        while (var18.hasNext()) {
                            tag = (String) var18.next();
                        }
                    } else {
                        boolean firstAttack = true;
                        Iterator var32 = hitten.getTags().iterator();

                        while (var32.hasNext()) {
                            String tag2 = (String) var32.next();
                            if (tag2.contains("anti_stunlock:")) {
                                if ((float) hitten.tickCount - Float.valueOf(tag2.split(":")[2]) <= 20.0F) {
                                    firstAttack = false;
                                }
                                break;
                            }
                        }

                        if (firstAttack) {
                            int i = 0;

                            while (i < hitten.getTags().size()) {
                                if (hitten.getTags().toArray()[i] != null) {
                                    if (((String) hitten.getTags().toArray()[i]).contains("anti_stunlock:")) {
                                        hitten.getTags().remove(hitten.getTags().toArray()[i]);
                                    } else {
                                        ++i;
                                    }
                                }
                            }
                        }
                    }

                    if (anti_stunlock <= 0.0F) {
                        var18 = hitten.getTags().iterator();

                        while (var18.hasNext()) {
                            tag = (String) var18.next();
                            if (tag.contains("anti_stunlock:")) {
                                hitten.removeTag(tag);
                                LivingEntityPatch<?> livingEntityPatch = (LivingEntityPatch) EpicFightCapabilities.getEntityPatch(hitten, LivingEntityPatch.class);
                                if (livingEntityPatch != null) {
                                    livingEntityPatch.setStunShield(0.0F);
                                    livingEntityPatch.setMaxStunShield(0.0F);
                                }
                                break;
                            }
                        }

                        source.setStunType(StunType.KNOCKDOWN);
                    }
                }

                int prevInvulTime = hitten.invulnerableTime;
                hitten.invulnerableTime = 0;
                AttackResult attackResult = entitypatch.attack(source, hitten, phase.hand);
                hitten.invulnerableTime = prevInvulTime;
                if (attackResult.resultType.dealtDamage()) {
                    if (source.getStunType() == StunType.KNOCKDOWN) {
                        truehittenEntity.addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 60, 0, true, false, false));
                        if (truehittenEntity.hasEffect(MobEffects.SLOW_FALLING)) {
                            truehittenEntity.removeEffect(MobEffects.SLOW_FALLING);
                        }

                        if (truehittenEntity.hasEffect(MobEffects.SLOW_FALLING)) {
                            truehittenEntity.removeEffect(MobEffects.SLOW_FALLING);
                        }
                    }

                    hitten.level().playSound(null, hitten.getX(), hitten.getY(), hitten.getZ(), this.getHitSound(entitypatch, phase), hitten.getSoundSource(), 1.0F, 1.0F);
                    this.spawnHitParticle((ServerLevel) hitten.level(), entitypatch, hitten, phase);
                    if (hitHurtableEntityPatch != null && phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).isPresent() && !hitHurtableEntityPatch.getOriginal().hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())) {
                        float stunTime;
                        if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.NONE && !(truehittenEntity instanceof Player)) {
                            stunTime = (float) ((double) (source.getBaseImpact() * 0.4F) * (1.0 - truehittenEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                            if (hitHurtableEntityPatch.getOriginal().isAlive()) {
                                hitHurtableEntityPatch.applyStun(source.getStunType() == StunType.KNOCKDOWN ? StunType.KNOCKDOWN : StunType.LONG, stunTime);
                                float power = source.getBaseImpact() * 0.25F;
                                double d1 = entity.getX() - hitten.getX();

                                double d0;
                                for (d0 = entity.getZ() - hitten.getZ(); d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
                                    d1 = (Math.random() - Math.random()) * 0.01;
                                }

                                power = (float) ((double) power * (1.0 - truehittenEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                                if ((double) power > 0.0) {
                                    hitten.hasImpulse = true;
                                    Vec3 vec3 = hitten.getDeltaMovement();
                                    Vec3 vec31 = (new Vec3(d1, 0.0, d0)).normalize().scale(power);
                                    hitten.lookAt(EntityAnchorArgument.Anchor.FEET, entity.position());
                                    hitten.setDeltaMovement(vec3.x / 2.0 - vec31.x, hitten.onGround() ? Math.min(0.4, vec3.y / 2.0) : 0.0, vec3.z / 2.0 - vec31.z);
                                }
                            }
                        }

                        if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.FALL) {
                            stunTime = (float) ((double) (source.getBaseImpact() * 0.4F) * (1.0 - truehittenEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                            if (hitHurtableEntityPatch.getOriginal().isAlive()) {
                                hitHurtableEntityPatch.applyStun(source.getStunType() == StunType.KNOCKDOWN ? StunType.KNOCKDOWN : StunType.SHORT, stunTime);
                                double power = source.getBaseImpact() * 0.25F;
                                double d1 = entity.getX() - hitten.getX();
                                double d2 = entity.getY() - 8.0 - hitten.getY();

                                double d0;
                                for (d0 = entity.getZ() - hitten.getZ(); d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
                                    d1 = (Math.random() - Math.random()) * 0.01;
                                }

                                if (!(truehittenEntity instanceof Player)) {
                                    power *= 1.0 - truehittenEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                                }

                                if (power > 0.0) {
                                    hitten.hasImpulse = true;
                                    Vec3 vec3 = entity.getDeltaMovement();
                                    Vec3 vec31 = (new Vec3(d1, d2, d0)).normalize().scale(power);
                                    if (!(truehittenEntity instanceof Player) || !(entitypatch instanceof PlayerPatch)) {
                                        hitten.setDeltaMovement(vec3.x / 2.0 - vec31.x, vec3.y / 2.0 - vec31.y, vec3.z / 2.0 - vec31.z);
                                    }
                                }

                                if (truehittenEntity instanceof Player && entitypatch instanceof PlayerPatch) {
                                    truehittenEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 5, (int) (power * 4.0 * 6.0), true, false, false));
                                }

                                truehittenEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, (int) (power * 3.0 * 6.0), 10, false, false, false));
                                truehittenEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (int) (power * 7.0 * 8.0), 60, false, false, true));
                            }
                        }
                    }
                }

                entitypatch.getCurrentlyActuallyHitEntities().add(truehittenEntity);
                if (attackResult.resultType.shouldCount()) {
                    entitypatch.getCurrentlyActuallyHitEntities().add(truehittenEntity);
                }
            }
        }
    }

    public void loadAnimation() {
        super.loadAnimation();
        if (!this.properties.containsKey(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED)) {
            float basisSpeed = Float.parseFloat(String.format(Locale.US, "%.2f", 1.0F / this.getTotalTime()));
            this.addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, basisSpeed);
        }

    }

    protected Vec3 getCoordVector(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> dynamicAnimation) {
        Vec3 vec3 = super.getCoordVector(entitypatch, dynamicAnimation);
        if (entitypatch.shouldBlockMoving() && this.getProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE).orElse(false)) {
            vec3 = vec3.scale(0.0);
        }

        return vec3;
    }


    public boolean isBasicAttackAnimation() {
        return true;
    }
}
