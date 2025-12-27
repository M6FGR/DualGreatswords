package M6FGR.dualgreatswords.api.animation.types;

import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;

import javax.annotation.Nullable;
import java.util.Locale;

public class SimpleNoRotAttackAnimation extends AttackAnimation {
    public SimpleNoRotAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends SimpleNoRotAttackAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        this(convertTime, antic, preDelay, contact, recovery, collider, colliderJoint, accessor, armature, false);
    }

    public SimpleNoRotAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends SimpleNoRotAttackAnimation> accessor, AssetAccessor<? extends Armature> armature, boolean directional) {
        this(convertTime, accessor, armature, new AttackAnimation.Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, colliderJoint, collider));
        if (directional) {
            this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
            this.addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false);
        }

    }

    public SimpleNoRotAttackAnimation(float convertTime, AnimationManager.AnimationAccessor<? extends SimpleNoRotAttackAnimation> accessor, AssetAccessor<? extends Armature> armature, AttackAnimation.Phase... phases) {
        super(convertTime, accessor, armature, phases);
        this.addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.5F);
        this.addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.0F));
        this.addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false);
    }

    public void loadAnimation() {
        super.loadAnimation();
        if (!this.properties.containsKey(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED)) {
            float basisSpeed = Float.parseFloat(String.format(Locale.US, "%.0f", 1.0F / this.getTotalTime()));
            this.addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, basisSpeed);
        }

    }

    protected void bindPhaseState(AttackAnimation.Phase phase) {
        float preDelay = phase.preDelay;
        this.stateSpectrumBlueprint.newTimePair(phase.start, preDelay).addState(EntityState.PHASE_LEVEL, 1).newTimePair(phase.start, phase.contact).addState(EntityState.CAN_SKILL_EXECUTION, false).addState(EntityState.CAN_BASIC_ATTACK, false).newTimePair(phase.start, phase.recovery).addState(EntityState.MOVEMENT_LOCKED, true).addState(EntityState.UPDATE_LIVING_MOTION, false).newTimePair(phase.start, phase.end).addState(EntityState.INACTION, true).newTimePair(phase.antic, phase.end).addState(EntityState.TURNING_LOCKED, false).newTimePair(preDelay, phase.contact).addState(EntityState.ATTACKING, true).addState(EntityState.PHASE_LEVEL, 2).newConditionalTimePair((entitypatch) -> {
            return entitypatch.isLastAttackSuccess() ? 1 : 0;
        }, phase.contact, phase.recovery).addConditionalState(0, EntityState.CAN_BASIC_ATTACK, false).addConditionalState(1, EntityState.CAN_BASIC_ATTACK, true).newTimePair(phase.contact, phase.end).addState(EntityState.PHASE_LEVEL, 3);
    }

    public boolean isBasicAttackAnimation() {
        return true;
    }
}
