package slimeknights.tconstruct.tools.modifiers.traits;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.tools.ToolStatsModifierBuilder;

public class LightweightModifier extends Modifier {
  public LightweightModifier() {
    super(0x2882d4);
  }

  @Override
  public void addToolStats(IModDataReadOnly persistentData, IModDataReadOnly volatileData, int level, ToolStatsModifierBuilder builder) {
    builder.multiplyAttackSpeed(1 + (level * 0.1f));
    builder.multiplyMiningSpeed(1 + (level * 0.1f));
  }
}
