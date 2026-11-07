package dev.ethan.ihudelytra.mixin;

import dev.ethan.ihudelytra.TrinketElytra;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds a fifth row under the boots in Inventory HUD+'s "block" armor layout that shows the
 * elytra from the Trinkets cape slot, using the HUD's own slot renderer so the durability
 * text follows the player's Inventory HUD+ settings.
 */
@Mixin(targets = "dlovin.inventoryhud.gui.renderers.BlockArmorRenderer")
public abstract class BlockArmorRendererMixin {
    @Shadow
    public boolean showArmor;

    @Shadow
    protected abstract int renderSlot(GuiGraphicsExtractor graphics, int x, int y, ItemStack stack, int slotType, int seed);

    // renderItems(graphics, armorOnRight): armor column is at x=75 (right-aligned) or x=0; rows are 18px apart, boots at y=54
    @Inject(method = "renderItems", at = @At("TAIL"))
    private void ihudelytra$renderElytra(GuiGraphicsExtractor graphics, boolean armorOnRight, CallbackInfo ci) {
        if (!this.showArmor) {
            return;
        }
        ItemStack elytra = TrinketElytra.find();
        if (elytra.isEmpty()) {
            return;
        }
        this.renderSlot(graphics, armorOnRight ? 75 : 0, 72, elytra, 1, 60);
    }

    // The block is 70px tall for alignment purposes; grow it by one row so bottom-anchored HUDs stay on screen
    @ModifyConstant(method = "render", constant = @Constant(floatValue = 70.0F))
    private float ihudelytra$blockHeight(float original) {
        return this.showArmor && !TrinketElytra.find().isEmpty() ? original + 18.0F : original;
    }
}
