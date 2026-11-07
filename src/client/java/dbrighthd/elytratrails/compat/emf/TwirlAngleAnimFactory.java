package dbrighthd.elytratrails.compat.emf;

import org.jetbrains.annotations.Nullable;
import traben.entity_model_features.models.animation.AnimSetupContext;
import traben.entity_model_features.models.animation.math.expression_tree.MathValue;
import traben.entity_model_features.models.animation.math.variables.factories.UniqueVariableFactory;

import static dbrighthd.elytratrails.twirling.TwirlManager.getExtraRollRadiansFromUUID;

public class TwirlAngleAnimFactory extends UniqueVariableFactory {
    @Override
    public MathValue.@Nullable ResultSupplier getSupplierOrNull(String s, AnimSetupContext animSetupContext) {
        return () -> {
            var uuid = EmfStateBridge.currentUuid(); // Store so state mounting change due to resource reload won't break this
            return uuid == null ? 0 : getExtraRollRadiansFromUUID(uuid);
        };
    }

    @Override
    public boolean createsThisVariable(String s) {
        return s.equals("twirl_angle");
    }

    @Override
    public @Nullable String getExplanationTranslationKey() {
        return "text.elytratrails.animation.twirl_angle_explanation";
    }

    @Override
    public @Nullable String getTitleTranslationKey() {
        return "text.elytratrails.animation.twirl_angle";
    }
}
