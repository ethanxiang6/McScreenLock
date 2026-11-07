# Trinkets for Minecraft 26.2

This is the unmodified source of Patbox's "Trinkets Updated" at tag `4.1.1+26.2`
(https://github.com/Patbox/trinkets, MIT, continuation of Emi's Trinkets).
Only `.github/workflows/build.yml` was changed so it builds with Temurin 25 on GitHub Actions.

Differences from Trinkets 3.10.0 (1.21.1) that affect other mods:
- Java package `dev.emi.trinkets` is now `eu.pb4.trinkets`; mod id is `trinkets_updated` (it `provides` `trinkets`).
- Cardinal Components is no longer needed.
- `TrinketComponent` is now `TrinketAttachment`, `Trinket` is now `TrinketCallback`, `SlotReference` is now `TrinketSlotAccess`.
- Vanilla elytras (the `glider` component) already render, fly and take damage in a trinket slot.
- Slots are only active for an entity when a datapack lists them (see the Elytra Slot port).

Build: push this folder to a GitHub repo, open Actions, run "build", download the `trinkets-26.2` artifact.
Or locally with JDK 25: `./gradlew build` (jar ends up in `build/libs/`).
