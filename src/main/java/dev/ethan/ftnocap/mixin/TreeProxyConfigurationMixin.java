package dev.ethan.ftnocap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Same override for the proxy wrapper FallingTree puts around its configuration. */
@Mixin(targets = "fr.rakambda.fallingtree.common.config.proxy.TreeProxyConfiguration")
public abstract class TreeProxyConfigurationMixin {
    @Inject(method = "getMaxScanSize", at = @At("HEAD"), cancellable = true)
    private void ftnocap$unlimitedScan(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Integer.MAX_VALUE);
    }
}
