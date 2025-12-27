package M6FGR.dualgreatswords.world.loot;

import M6FGR.dualgreatswords.main.DualGreatSwords;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import yesman.epicfight.api.forgeevent.SkillLootTableRegistryEvent;
import yesman.epicfight.config.CommonConfig;
import yesman.epicfight.data.loot.function.SetSkillFunction;
import yesman.epicfight.world.item.EpicFightItems;

@EventBusSubscriber(
        modid = DualGreatSwords.MOD_ID,
        bus = Bus.MOD
)
public class DualGreatSwordsLootingEngine {
    public DualGreatSwordsLootingEngine() {
    }

    @SubscribeEvent
    public static void SkillDrops(SkillLootTableRegistryEvent event) {
        int modifier = CommonConfig.SKILL_BOOK_MOB_DROP_CHANCE_MODIFIER.get();
        int dropChance = 100 + modifier;
        int antiDropChance = 100 - modifier;
        float dropChanceModifier = antiDropChance == 0 ? Float.MAX_VALUE : (float)dropChance / (float)antiDropChance;
        event.add(EntityType.ZOMBIE, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.HUSK, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.DROWNED, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get()
                        ).apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.STRAY, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.SKELETON, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.SPIDER, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.CAVE_SPIDER, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.CREEPER, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.ENDERMAN, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.01F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.PIGLIN, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.PIGLIN_BRUTE, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.WITHER_SKELETON, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.WITCH, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.WITHER, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.8F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.ENDER_DRAGON, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.8F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.PILLAGER, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.VINDICATOR, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))))
                .add(EntityType.EVOKER, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.005F * dropChanceModifier))
                        .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get())
                                .apply(SetSkillFunction.builder(1.0F, "dualgreatswords:dualgreatsword"))));
    }
}