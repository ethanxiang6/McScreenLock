package dbrighthd.elytratrails.compat.emf;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jetbrains.annotations.Nullable;
import traben.entity_model_features.models.animation.EMFAnimationEntityContext;
import traben.entity_model_features.models.animation.state.EMFEntityRenderState;

import java.util.UUID;

/**
 * EMF 3.2.x path (no EMFState / ETFState). Only loaded by {@link EmfStateBridge} when {@code EMFState} does not exist.
 */
final class LegacyMount {
    private LegacyMount() {
    }

    static @Nullable UUID currentUuid() {
        EMFEntityRenderState state = EMFAnimationEntityContext.getEmfState();
        return state == null ? null : state.uuid();
    }

    static @Nullable String currentTypeString() {
        EMFEntityRenderState state = EMFAnimationEntityContext.getEmfState();
        return state == null ? null : state.typeString();
    }

    static @Nullable Object mount(EntityRenderState renderState) {
        EMFEntityRenderState previous = EMFAnimationEntityContext.getEmfState();
        EMFAnimationEntityContext.setCurrentEntityNoIteration(EMFEntityRenderState.from(renderState));
        return previous;
    }

    static void unmount(@Nullable Object previous) {
        if (previous instanceof EMFEntityRenderState state) {
            EMFAnimationEntityContext.setCurrentEntityNoIteration(state);
        } else {
            EMFAnimationEntityContext.reset();
        }
    }
}
