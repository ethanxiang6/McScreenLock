package dbrighthd.elytratrails.compat.emf;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import traben.entity_model_features.models.animation.state.EMFEntityRenderState;
import traben.entity_texture_features.features.state.ETFState;

/**
 * EMF 3.3+ / ETF 7.2+ path. Only loaded by {@link EmfStateBridge} when {@code EMFState} exists.
 */
final class ModernMount {
    private ModernMount() {
    }

    static void mount(EntityRenderState renderState) {
        ETFState.mount(EMFEntityRenderState.from(renderState));
    }

    static void unmount() {
        ETFState.unMount();
    }
}
