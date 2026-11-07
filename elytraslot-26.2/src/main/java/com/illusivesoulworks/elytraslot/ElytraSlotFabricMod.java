/*
 * Copyright (C) 2018-2024 Illusive Soulworks
 *
 * Elytra Slot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Elytra Slot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * and the GNU Lesser General Public License along with Elytra Slot.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.elytraslot;

import dev.yumi.commons.TriState;
import eu.pb4.trinkets.api.event.TrinketCanEquipCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

/**
 * Elytra Slot for Minecraft 26.2.
 *
 * <p>Trinkets 4.x renders, flies and damages vanilla elytras in trinket slots on its own,
 * so this mod only has to (1) enable the chest/cape slot for players and allow elytras in it
 * (see the data files) and (2) keep the original rule that you cannot wear an elytra in the
 * slot while another one is already worn as chest armor.
 */
public class ElytraSlotFabricMod implements ModInitializer {

  public static final String MOD_ID = "elytraslot";
  public static final TagKey<Item> ELYTRA_TAG =
      TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "elytra"));

  @Override
  public void onInitialize() {
    TrinketCanEquipCallback.EVENT.register((stack, slot, entity, canEquipDefault) -> {

      if (stack.is(ELYTRA_TAG) && entity.getItemBySlot(EquipmentSlot.CHEST).is(ELYTRA_TAG)) {
        return TriState.FALSE;
      }
      return TriState.DEFAULT;
    });
  }
}
