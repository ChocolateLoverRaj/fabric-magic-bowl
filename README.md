# Fabric Magic Bowl
Get infinite amounts of items with the magic bowl.

[![Minecraft_ 1 17 - Singleplayer 2021-07-01 18-02-00_Trim_Moment](https://user-images.githubusercontent.com/52586855/124199348-4c9a5900-da98-11eb-81f0-91c176fc83fa.jpg)](https://youtu.be/PpABBjD4oN8)

## Usage
### Obtaining
Get the item called `magic_bowl:magic_bowl`. This mod does not include a crafting recipe for this bowl, so the only way of getting it is the miscellaneous tab in the creative inventory or using `/give`.

### Adding items to the bowl!
![add](https://user-images.githubusercontent.com/52586855/124200717-a81a1600-da9b-11eb-8b05-cd7a586086ea.gif)

Right click with the magic bowl. Then put any item into the first slot. Then close the menu. The item name will now say `${item} in Magic Bowl`.

### Taking items from the bowl
![take](https://user-images.githubusercontent.com/52586855/124200667-8a4cb100-da9b-11eb-839a-ec7fdd8ec94d.gif)

Once loaded with an item, right click on the magic bowl to take the item. It will go into your inventory. There is a 3 second cooldown.

### Removing items from the bowl
![Minecraft_ 1 17 - Singleplayer 2021-07-01 18-02-00_Trim_Moment(2)](https://user-images.githubusercontent.com/52586855/124202631-6049bd80-daa0-11eb-9de7-bfd1c64f5a68.jpg)

If you want to change what item is in the bowl, you need to wash it. Wash it by right clicking it while looking at water.

## Installing
### Magic Bowl Mod
Download the mod from the releases

### Required Libraries
- Download completeconfig from https://jitpack.io/com/gitlab/Lortseam/completeconfig/1.0.0/completeconfig-1.0.0.jar
- Download [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

### Optional Libraries
- To use gui for [configuring](#configuring), download [ModMenu](https://github.com/TerraformersMC/ModMenu)
- If you use ModMenu, then you'll also need to download [Cloth Config](https://www.curseforge.com/minecraft/mc-mods/cloth-config)

## Configuring
> **Important** - The GUI for `bannedItems` currently is broken, and trying to edit it with the GUI crashes the game. For you, you can edit the config file manually to configure banned items.

You can edit `config/magic_bowl.conf` in your Minecraft folder. If you are in Single Player, you can use [ModMenu](https://github.com/TerraformersMC/ModMenu) and configure the mod from the mods screen.

```conf
config {
    bannedItems=[
        "minecraft:enchanted_golden_apple",
        "minecraft:golden_apple"
    ]
    cooldown=600
    foodOnly=true
}
```

### `cooldown`
Number of ticks until the magic bowl can be used again.

### `foodOnly`
Setting to true disallows putting non food items into the bowl.

### `bannedItems`
A list of items that cannot be added to the bowl. If you consider an item too OP, you can add it to this list.
