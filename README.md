# ASync_glint

Client-side configurable armor enchantment glint for Minecraft 1.12.2 and Forge.

## Features

- Changes the vanilla armor enchantment glint color.
- Available styles: blue, cyan, white, gold, red, green, purple, rainbow, and off.
- Glint opacity can be adjusted from 25% to 100% in 1% increments.
- Settings are available in **Options → Skin Customization**.
- When ASync Tactical Armor is installed, these two standalone controls are hidden
  to avoid duplicating Tactical Armor's own glint controls.
- Settings persist in `config/async_glint.cfg`.
- Uses one glint render pass instead of the vanilla two passes.
- Skips remote-player armor glint beyond 64 blocks.
- Client-side only; the server does not need the mod.

## Requirements

- Minecraft 1.12.2
- Minecraft Forge 14.23.5.2859
- Java 8 JDK for development and building

## Building

Clone the repository and run the Gradle wrapper from the project directory.

Windows:

```powershell
.\gradlew.bat clean build
```

Linux/macOS:

```bash
./gradlew clean build
```

The compiled mod will be created in `build/libs/`.

To generate IDE run configurations:

```powershell
.\gradlew.bat genIntellijRuns
```

or:

```powershell
.\gradlew.bat genEclipseRuns
```

## Installation

1. Install Forge 14.23.5.2859 for Minecraft 1.12.2.
2. Build the project or download a release JAR.
3. Put the JAR into the Minecraft `mods` directory.
4. Open **Options → Skin Customization** to configure the glint.

## Technical note

ASync_glint is a legacy Forge coremod. Its class transformer replaces the vanilla
`LayerArmorBase.renderEnchantedGlint` implementation with an optimized renderer.
Other coremods that transform the same method may be incompatible.


