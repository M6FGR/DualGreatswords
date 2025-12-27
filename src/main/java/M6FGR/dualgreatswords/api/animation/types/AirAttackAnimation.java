//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package M6FGR.dualgreatswords.api.animation.types;

import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty.ActionAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations.ReusableSources;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class AirAttackAnimation extends AttackAnimation {
    public AirAttackAnimation(float transitionTime, float antic, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends AirAttackAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        this(transitionTime, antic, antic, contact, recovery, true, collider, colliderJoint, accessor, armature);
    }

    public AirAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, boolean directional, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends AirAttackAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        this(transitionTime, accessor, armature, new AttackAnimation.Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, colliderJoint, collider));
        if (directional) {
            this.addProperty(StaticAnimationProperty.POSE_MODIFIER, ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        }

    }

    public AirAttackAnimation(float transitionTime, AnimationManager.AnimationAccessor<? extends AirAttackAnimation> accessor, AssetAccessor<? extends Armature> armature, AttackAnimation.Phase... phases) {
        super(transitionTime, accessor, armature, phases);
        this.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F));
        this.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.5F);
        this.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true);
        this.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true);
    }

    public AirAttackAnimation(float transitionTime, String path, AssetAccessor<? extends Armature> armature, AttackAnimation.Phase... phases) {
        super(transitionTime, path, armature, phases);
        this.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F));
        this.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.5F);
        this.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false);
        this.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true);
    }

    protected void spawnHitParticle(ServerLevel world, LivingEntityPatch<?> attackerpatch, Entity hit, AttackAnimation.Phase phase) {
        super.spawnHitParticle(world, attackerpatch, hit, phase);
        world.sendParticles(ParticleTypes.CRIT, hit.getX(), hit.getY(), hit.getZ(), 15, 0.0, 0.0, 0.0, 1.0);
    }

    public boolean isBasicAttackAnimation() {
        return true;
    }
}
