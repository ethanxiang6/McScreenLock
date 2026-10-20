# Lock On (Fabric, Minecraft 26.2)

Press the toggle key (default `G`, rebind under Controls > Lock On) while looking at a player and your view locks onto them. It tracks them every frame until you press the key again. The lock also drops by itself if they die, leave, change dimension or get far out of range.

While locked, your mouse doesn't turn the view. Movement keys work as normal.

Settings in `config/lockon.json` (created on first launch): `maxRange` (64), `targetCone` (15°), `aimHeight` (0.85 of their height), `requireLineOfSight`, `lockMobs`, `showStatus`.

## Build
Push to GitHub and the `build` workflow uploads the jar as the `lockon-jar` artifact, or run `./gradlew build` with JDK 25.

Requires Fabric Loader 0.19.3+ and Fabric API for 26.2.
