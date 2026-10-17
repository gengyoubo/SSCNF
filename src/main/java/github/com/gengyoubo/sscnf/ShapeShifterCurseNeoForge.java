package github.com.gengyoubo.sscnf;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import github.com.gengyoubo.sscnf.other.config.SscClientConfig;
import github.com.gengyoubo.sscnf.other.config.SscCommonConfig;
import github.com.gengyoubo.sscnf.other.advancement.SscAdvancementTriggers;
import github.com.gengyoubo.sscnf.network.ModNetwork;
import github.com.gengyoubo.sscnf.form.FormManager;
import github.com.gengyoubo.sscnf.registry.ModBlocks;
import github.com.gengyoubo.sscnf.registry.ModBlockEntities;
import github.com.gengyoubo.sscnf.registry.ModCreativeModeTabs;
import github.com.gengyoubo.sscnf.registry.ModEntities;
import github.com.gengyoubo.sscnf.registry.ModItems;
import github.com.gengyoubo.sscnf.registry.ModMenuTypes;
import github.com.gengyoubo.sscnf.registry.ModRecipeSerializers;
import github.com.gengyoubo.sscnf.registry.ModEffects;
import github.com.gengyoubo.sscnf.registry.ModPotions;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ShapeShifterCurseNeoForge.MOD_ID)
public final class ShapeShifterCurseNeoForge {
    public static final String MOD_ID = "shape_shifter_curse";
    public static final String RESOURCE_NAMESPACE = "shape-shifter-curse";
    public static final Logger LOGGER = LogManager.getLogger(ShapeShifterCurseNeoForge.MOD_ID);

    @SuppressWarnings("removal")
    public ShapeShifterCurseNeoForge(IEventBus modBus, ModContainer container) {
        github.com.gengyoubo.sscnf.data.PlayerAttachments.register(modBus);
        container.registerConfig(ModConfig.Type.CLIENT, SscClientConfig.SPEC,
                "shape-shifter-curse-client.toml");
        container.registerConfig(ModConfig.Type.COMMON, SscCommonConfig.SPEC,
                "shape-shifter-curse-common.toml");

        github.com.gengyoubo.sscnf.registry.ModArmorMaterials.MATERIALS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModCreativeModeTabs.TABS.register(modBus);
        ModMenuTypes.MENUS.register(modBus);
        ModRecipeSerializers.SERIALIZERS.register(modBus);
        ModRecipeSerializers.TYPES.register(modBus);
        ModEffects.EFFECTS.register(modBus);
        ModPotions.POTIONS.register(modBus);
        modBus.addListener(this::commonSetup);
        SscAdvancementTriggers.initialize(modBus);
        FormManager.initialize();
        github.com.gengyoubo.sscnf.content.BuiltinContent.register();
        modBus.addListener(ModNetwork::initialize);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        
    }
}
