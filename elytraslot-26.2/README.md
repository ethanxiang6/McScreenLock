# Elytra Slot for Minecraft 26.2 (Fabric)

Port of Illusive Soulworks' Elytra Slot (https://github.com/illusivesoulworks/elytraslot, LGPL-3.0-or-later)
from 1.21.1 to Minecraft 26.2. Requires Trinkets Updated 4.1.0 or newer (the `4.1.1+26.2` build is what this was written against) and Fabric API.

## What changed from 9.0.x
Trinkets 4.x already renders vanilla elytras in the `chest/cape` slot, lets them fly, damages them and hides the cape.
Keeping Elytra Slot's own render layer and flight hooks would draw two elytras, so they were removed:

- Kept: enables the `chest/cape` slot for players, the `#elytraslot:elytra` tag (vanilla elytra plus optional modded ones), and the rule that you cannot put an elytra in the slot while wearing one as chest armor.
- Removed: custom render layer, Fabric elytra events, Deeper Darker / Wavey Capes / MinecraftCapes integrations, and the hard-coded texture table. Modern modded elytras use the `glider` + `equippable` components and need only be added to the tag.
- Not ported: Forge and NeoForge builds.

## Build
Push to GitHub and run the `build` workflow (it builds Trinkets 4.1.1+26.2 from source first, then this mod).
The jar is the `elytraslot-26.2` artifact. Locally you need JDK 25 and Trinkets published to mavenLocal.
Install: put the Elytra Slot jar and the Trinkets jar (`trinkets-26.2` build, or the Trinkets Updated download) in `mods/` next to Fabric API.
