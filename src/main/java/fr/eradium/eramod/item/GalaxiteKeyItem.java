package fr.eradium.eramod.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class GalaxiteKeyItem extends Item {
	public GalaxiteKeyItem() {
		super(new Item.Properties().stacksTo(8).rarity(Rarity.RARE));
	}
}