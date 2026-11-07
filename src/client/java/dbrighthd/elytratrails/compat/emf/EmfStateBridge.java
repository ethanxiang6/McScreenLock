package dbrighthd.elytratrails.compat.emf;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Reads and mounts the "current entity" state of Entity Model Features without a hard dependency on one EMF API generation.
 * <ul>
 *     <li>Modern EMF (3.3+): {@code EMFState.state()} / {@code ETFState.mount()}</li>
 *     <li>Older EMF (3.2.x, e.g. 3.2.6): {@code EMFAnimationEntityContext}</li>
 * </ul>
 * The modern classes are only touched through reflection or through {@link ModernMount}, which is only loaded when they exist,
 * so a missing class can never crash the game on load.
 */
public final class EmfStateBridge {
    private EmfStateBridge() {
    }

    private static final String MODERN_STATE_CLASS = "traben.entity_model_features.models.animation.state.EMFState";

    private static final boolean MODERN;
    private static Method modernStateGetter;
    private static Method modernUuid;
    private static Method modernTypeString;

    static {
        boolean modern = false;
        try {
            Class<?> stateClass = Class.forName(MODERN_STATE_CLASS);
            modernStateGetter = stateClass.getMethod("state");
            Class<?> returnType = modernStateGetter.getReturnType();
            modernUuid = returnType.getMethod("uuid");
            modernTypeString = returnType.getMethod("typeString");
            modern = true;
        } catch (Throwable ignored) {
            // older EMF without EMFState
        }
        MODERN = modern;
    }

    /**
     * Opaque token for whatever was mounted before {@link #mount}, to be handed back to {@link #unmount}.
     */
    public record Mounted(@Nullable Object previous, boolean modern) {
    }

    /**
     * @return the UUID of the entity EMF is currently animating, or null if none
     */
    public static @Nullable UUID currentUuid() {
        try {
            if (MODERN) {
                Object state = modernStateGetter.invoke(null);
                return state == null ? null : (UUID) modernUuid.invoke(state);
            }
            return LegacyMount.currentUuid();
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * @return the EMF type string (e.g. "player") of the entity EMF is currently animating, or null if none
     */
    public static @Nullable String currentTypeString() {
        try {
            if (MODERN) {
                Object state = modernStateGetter.invoke(null);
                return state == null ? null : (String) modernTypeString.invoke(state);
            }
            return LegacyMount.currentTypeString();
        } catch (Throwable t) {
            return null;
        }
    }

    public static Mounted mount(EntityRenderState renderState) {
        if (MODERN) {
            ModernMount.mount(renderState);
            return new Mounted(null, true);
        }
        return new Mounted(LegacyMount.mount(renderState), false);
    }

    public static void unmount(Mounted mounted) {
        if (mounted.modern()) {
            ModernMount.unmount();
        } else {
            LegacyMount.unmount(mounted.previous());
        }
    }
}
