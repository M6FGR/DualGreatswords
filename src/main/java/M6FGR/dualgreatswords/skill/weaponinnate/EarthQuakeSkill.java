package M6FGR.dualgreatswords.skill.weaponinnate;

import M6FGR.dualgreatswords.gameassets.DualGreatSwordsAnimations;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.*;
public class EarthQuakeSkill extends SimpleWeaponInnateSkill {
    private static final UUID EVENT_UUID = UUID.fromString("b84e577a-c653-11ed-afa1-0242ac120002");

    public EarthQuakeSkill(SimpleWeaponInnateSkill.Builder builder) {
        super(builder);
    }

    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID, (event) -> {
            if (event.getDamageSource().getAnimation() == DualGreatSwordsAnimations.GREATSWORD_EARTH_QUAKE) {
                ValueModifier.ResultCalculator executionMinHealth = ValueModifier.calculator();
                this.getProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, this.properties.get(0));
                Objects.requireNonNull(executionMinHealth);
                executionMinHealth.multiply(0.8F);
                float health = event.getTarget().getHealth();
                float baseDamage = (float) event.getPlayerPatch().getOriginal().getAttributeValue(Attributes.ATTACK_DAMAGE);
                float modifiedBaseDamage = event.getPlayerPatch().getModifiedBaseDamage(baseDamage);
                float executionHealth = executionMinHealth.getResult(modifiedBaseDamage);
                if (health < executionHealth && event.getDamageSource() != null) {
                    event.getDamageSource().setExecute();
                }
            }

        });
    }

    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID);
    }

    @OnlyIn(Dist.CLIENT)
    public List<Component> getTooltipOnItem(ItemStack itemstack, CapabilityItem cap, PlayerPatch<?> playerpatch) {
        List<Component> list = Lists.newArrayList();
        List<Object> tooltipArgs = Lists.newArrayList();
        String traslatableText = this.getTranslationKey();
        double itemBaseDamage = playerpatch.getOriginal().getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() + (double) EnchantmentHelper.getDamageBonus(itemstack, MobType.UNDEFINED);
        Set<AttributeModifier> attributeModifiers = new HashSet();
        attributeModifiers.addAll(playerpatch.getOriginal().getAttribute(Attributes.ATTACK_DAMAGE).getModifiers());
        attributeModifiers.addAll(CapabilityItem.getAttributeModifiers(Attributes.ATTACK_DAMAGE, EquipmentSlot.MAINHAND, itemstack, playerpatch));

        AttributeModifier modifier;
        for (Iterator var10 = attributeModifiers.iterator(); var10.hasNext(); itemBaseDamage += modifier.getAmount()) {
            modifier = (AttributeModifier) var10.next();
        }

        ValueModifier.ResultCalculator executionMinHealth = ValueModifier.calculator();
        Optional var10000 = this.getProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, this.properties.get(0));
        Objects.requireNonNull(executionMinHealth);
        executionMinHealth.multiply(0.8F);
        ChatFormatting var10001 = ChatFormatting.RED;
        tooltipArgs.add(var10001 + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(executionMinHealth.getResult((float) itemBaseDamage)));
        list.add(Component.translatable(traslatableText).withStyle(ChatFormatting.WHITE).append(Component.literal(String.format("[%.0f]", this.consumption)).withStyle(ChatFormatting.AQUA)));
        list.add(Component.translatable(traslatableText + ".tooltip", tooltipArgs.toArray(new Object[0])).withStyle(ChatFormatting.DARK_GRAY));
        this.generateTooltipforPhase(list, itemstack, cap, playerpatch, this.properties.get(0), "Each Strike:");
        return list;
    }
}
