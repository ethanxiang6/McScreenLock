package dev.ethan.ftnocap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * FallingTree aborts with "tree too big" once the scan visits more than maxScanSize (default 500) blocks.
 * Report an unlimited scan size so that check can never trigger.
 */
@Mixin(targets = "fr.rakambda.fallingtree.common.config.real.TreeConfiguration")
public abstract class TreeConfigurationMixin {
    @Inject(method = "getMaxScanSize", at = @At("HEAD"), cancellable = true)
    private void ftnocap$unlimitedScan(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Integer.MAX_VALUE);
    }
}
