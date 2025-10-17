/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package fr.eradium.eramod.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

import net.minecraft.world.item.Item;

import fr.eradium.eramod.item.TeleCasterItem;
import fr.eradium.eramod.item.EscapitathItem;
import fr.eradium.eramod.EramodMod;

public class EramodModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(EramodMod.MODID);
	public static final DeferredItem<Item> TELE_CASTER = REGISTRY.register("tele_caster", TeleCasterItem::new);
	public static final DeferredItem<Item> ESCAPITATH = REGISTRY.register("escapitath", EscapitathItem::new);
	// Start of user code block custom items
	// End of user code block custom items
}