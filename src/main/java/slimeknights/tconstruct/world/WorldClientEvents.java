package slimeknights.tconstruct.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.ClientEventBase;
import slimeknights.tconstruct.library.client.particle.SlimeParticle;
import slimeknights.tconstruct.shared.block.StickySlimeBlock;
import slimeknights.tconstruct.world.block.SlimeGrassBlock;
import slimeknights.tconstruct.world.block.SlimeGrassBlock.FoliageType;
import slimeknights.tconstruct.world.client.SlimeColorReloadListener;
import slimeknights.tconstruct.world.client.SlimeColorizer;
import slimeknights.tconstruct.world.client.TinkerSlimeRenderer;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@EventBusSubscriber(modid=TConstruct.modID, value=Dist.CLIENT, bus=Bus.MOD)
public class WorldClientEvents extends ClientEventBase {
  /**
   * Called by TinkerClient to add the resource listeners, runs during constructor
   */
  public static void addResourceListener(IReloadableResourceManager manager) {
    for (FoliageType type : FoliageType.values()) {
      manager.addReloadListener(new SlimeColorReloadListener(type));
    }
  }

  @SubscribeEvent
  static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
    Minecraft.getInstance().particles.registerFactory(TinkerWorld.slimeParticle.get(), new SlimeParticle.Factory());
  }

  @SubscribeEvent
  static void clientSetup(FMLClientSetupEvent event) {
    RenderingRegistry.registerEntityRenderingHandler(TinkerWorld.blueSlimeEntity.get(), TinkerSlimeRenderer.BLUE_SLIME_FACTORY);

    // render types - ores
    RenderTypeLookup.setRenderLayer(TinkerWorld.cobaltOre.get(), RenderType.getCutoutMipped());

    // render types - slime plants
    for (SlimeGrassBlock.FoliageType type : SlimeGrassBlock.FoliageType.values()) {
      RenderTypeLookup.setRenderLayer(TinkerWorld.slimeLeaves.get(type), RenderType.getCutoutMipped());
      RenderTypeLookup.setRenderLayer(TinkerWorld.vanillaSlimeGrass.get(type), RenderType.getCutoutMipped());
      RenderTypeLookup.setRenderLayer(TinkerWorld.greenSlimeGrass.get(type), RenderType.getCutoutMipped());
      RenderTypeLookup.setRenderLayer(TinkerWorld.blueSlimeGrass.get(type), RenderType.getCutoutMipped());
      RenderTypeLookup.setRenderLayer(TinkerWorld.purpleSlimeGrass.get(type), RenderType.getCutoutMipped());
      RenderTypeLookup.setRenderLayer(TinkerWorld.magmaSlimeGrass.get(type), RenderType.getCutoutMipped());
      RenderTypeLookup.setRenderLayer(TinkerWorld.slimeFern.get(type), RenderType.getCutout());
      RenderTypeLookup.setRenderLayer(TinkerWorld.slimeTallGrass.get(type), RenderType.getCutout());
      RenderTypeLookup.setRenderLayer(TinkerWorld.slimeSapling.get(type), RenderType.getCutout());
    }
    RenderTypeLookup.setRenderLayer(TinkerWorld.purpleSlimeVine.get(), RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(TinkerWorld.blueSlimeVine.get(), RenderType.getCutout());

    // render types - slime blocks
    for (StickySlimeBlock.SlimeType type : StickySlimeBlock.SlimeType.TINKER) {
      RenderTypeLookup.setRenderLayer(TinkerWorld.slime.get(type), RenderType.getTranslucent());
    }
  }

  @SubscribeEvent
  static void registerBlockColorHandlers(ColorHandlerEvent.Block event) {
    BlockColors blockColors = event.getBlockColors();

    // slime plants - blocks
    for (SlimeGrassBlock.FoliageType type : SlimeGrassBlock.FoliageType.values()) {
      blockColors.register(
        (state, reader, pos, index) -> getSlimeColorByPos(pos, type, null),
        TinkerWorld.vanillaSlimeGrass.get(type), TinkerWorld.greenSlimeGrass.get(type), TinkerWorld.blueSlimeGrass.get(type),
        TinkerWorld.purpleSlimeGrass.get(type), TinkerWorld.magmaSlimeGrass.get(type));
      blockColors.register(
        (state, reader, pos, index) -> getSlimeColorByPos(pos, type, SlimeColorizer.LOOP_OFFSET),
        TinkerWorld.slimeLeaves.get(type));
      blockColors.register(
        (state, reader, pos, index) -> getSlimeColorByPos(pos, type, null),
        TinkerWorld.slimeFern.get(type), TinkerWorld.slimeTallGrass.get(type));
    }

    // vines
    blockColors.register(
      (state, reader, pos, index) -> getSlimeColorByPos(pos, SlimeGrassBlock.FoliageType.BLUE, SlimeColorizer.LOOP_OFFSET),
      TinkerWorld.blueSlimeVine.get());
    blockColors.register(
      (state, reader, pos, index) -> getSlimeColorByPos(pos, SlimeGrassBlock.FoliageType.PURPLE, SlimeColorizer.LOOP_OFFSET),
      TinkerWorld.purpleSlimeVine.get());
  }

  @SubscribeEvent
  static void registerItemColorHandlers(ColorHandlerEvent.Item event) {
    BlockColors blockColors = event.getBlockColors();
    ItemColors itemColors = event.getItemColors();
    // slime grass items
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.vanillaSlimeGrass);
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.greenSlimeGrass);
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.blueSlimeGrass);
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.purpleSlimeGrass);
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.magmaSlimeGrass);
    // plant items
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.slimeLeaves);
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.slimeFern);
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.slimeTallGrass);
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.blueSlimeVine);
    registerBlockItemColorAlias(blockColors, itemColors, TinkerWorld.purpleSlimeVine);
  }

  /**
   * Block colors for a slime type
   * @param pos   Block position
   * @param type  Slime foilage color
   * @param add   Offset position
   * @return  Color for the given position, or the default if position is null
   */
  private static int getSlimeColorByPos(@Nullable BlockPos pos, SlimeGrassBlock.FoliageType type, @Nullable BlockPos add) {
    if (pos == null) {
      return SlimeColorizer.getColorStatic(type);
    }
    if (add != null) {
      pos = pos.add(add);
    }

    return SlimeColorizer.getColorForPos(pos, type);
  }
}
