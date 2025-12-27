package M6FGR.dualgreatswords.main;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DualGreatSwords.MOD_ID)
@SuppressWarnings({"removal", "unchecked"})
public class DualGreatSwords {

    public static final String MOD_ID = "dualgreatswords";
    public static boolean regGuarded = true;
    public static final Logger LOGGER = LogManager.getLogger("DualGreatswords");

    public DualGreatSwords() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(DualGreatSwords::buildSkillEvent);

    }
    public static void buildSkillEvent(RegisterEvent event) {
        if (EpicFightSkills.GUARD != null) {
            if (!regGuarded) {
                try {
                    regGuard();
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

                regGuarded = true;
            }
        }
    }

    public static void regGuard() throws NoSuchFieldException, IllegalAccessException {
        LOGGER.info("buildSkillEvent");
        Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardMotions = new HashMap<>();
        Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardBreakMotions = new HashMap<>();
        Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> advancedGuardMotions = new HashMap<>();
        guardMotions.put(CapabilityItem.WeaponCategories.GREATSWORD, (item, player) -> {
            return item.getStyle(player) == CapabilityItem.Styles.OCHS ? Animations.SWORD_DUAL_GUARD_HIT : Animations.GREATSWORD_GUARD_HIT;
        });
        guardBreakMotions.put(CapabilityItem.WeaponCategories.GREATSWORD, (item, player) -> {
            return item.getStyle(player) == CapabilityItem.Styles.OCHS ? Animations.BIPED_COMMON_NEUTRALIZED : Animations.GREATSWORD_GUARD_BREAK;
        });
        Field temp = GuardSkill.class.getDeclaredField("guardMotions");
        temp.setAccessible(true);
        Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> target = (Map)temp.get(EpicFightSkills.GUARD);
        Iterator var5 = guardMotions.keySet().iterator();

        WeaponCategory weaponCapability;
        while(var5.hasNext()) {
            weaponCapability = (WeaponCategory)var5.next();
            target.put(weaponCapability, guardMotions.get(weaponCapability));
        }

        target = (Map)temp.get(EpicFightSkills.PARRYING);
        var5 = guardMotions.keySet().iterator();

        while(var5.hasNext()) {
            weaponCapability = (WeaponCategory)var5.next();
            target.put(weaponCapability, guardMotions.get(weaponCapability));
        }

        target = (Map)temp.get(EpicFightSkills.IMPACT_GUARD);
        var5 = guardMotions.keySet().iterator();

        while(var5.hasNext()) {
            weaponCapability = (WeaponCategory)var5.next();
            target.put(weaponCapability, guardMotions.get(weaponCapability));
        }

        temp = GuardSkill.class.getDeclaredField("guardBreakMotions");
        temp.setAccessible(true);
        target = (Map)temp.get(EpicFightSkills.GUARD);
        var5 = guardBreakMotions.keySet().iterator();

        while(var5.hasNext()) {
            weaponCapability = (WeaponCategory)var5.next();
            target.put(weaponCapability, guardBreakMotions.get(weaponCapability));
        }

        target = (Map)temp.get(EpicFightSkills.PARRYING);
        var5 = guardBreakMotions.keySet().iterator();

        while(var5.hasNext()) {
            weaponCapability = (WeaponCategory)var5.next();
            target.put(weaponCapability, guardBreakMotions.get(weaponCapability));
        }

        target = (Map)temp.get(EpicFightSkills.IMPACT_GUARD);
        var5 = guardBreakMotions.keySet().iterator();

        while(var5.hasNext()) {
            weaponCapability = (WeaponCategory)var5.next();
            target.put(weaponCapability, guardBreakMotions.get(weaponCapability));
        }

        temp = GuardSkill.class.getDeclaredField("advancedGuardMotions");
        temp.setAccessible(true);
        target = (Map)temp.get(EpicFightSkills.PARRYING);
        var5 = advancedGuardMotions.keySet().iterator();

        while(var5.hasNext()) {
            weaponCapability = (WeaponCategory)var5.next();
            target.put(weaponCapability, advancedGuardMotions.get(weaponCapability));
        }

    }
}
