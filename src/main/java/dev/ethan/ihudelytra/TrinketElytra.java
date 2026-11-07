package dev.ethan.ihudelytra;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Finds the glider (elytra) the local player wears in a Trinkets slot.
 * Any failure (Trinkets API change, no player yet) simply means "nothing to show".
 */
public final class TrinketElytra {
    private TrinketElytra() {
    }

    public static ItemStack find() {
        try {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return ItemStack.EMPTY;
            }
            for (TrinketSlotAccess slot : TrinketsApi.getAttachment(player)
                    .equipped(stack -> !stack.isEmpty() && stack.has(DataComponents.GLIDER), false)) {
                if (!slot.cosmetic()) {
                    return slot.get();
                }
            }
        } catch (Throwable ignored) {
        }
        return ItemStack.EMPTY;
    }
}
