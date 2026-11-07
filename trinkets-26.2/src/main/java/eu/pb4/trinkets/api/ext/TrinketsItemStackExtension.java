package eu.pb4.trinkets.api.ext;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface TrinketsItemStackExtension {
    default void hurtAndBreak(int amount, LivingEntity owner, TrinketSlotAccess access) {
        TrinketsApi.hurtAndBreakItemStack((ItemStack) this, amount, owner, access);
    }
}
