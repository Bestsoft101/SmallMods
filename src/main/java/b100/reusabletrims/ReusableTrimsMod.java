package b100.reusabletrims;

import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registries;

public class ReusableTrimsMod {
	
	public static boolean isArmorTrim(Item item) {
		if(item == null) {
			return false;
		}
		if(item instanceof SmithingTemplateItem) {
			String name = Registries.ITEM.getId(item).getPath();
			if(name.contains("armor_trim")) {
				return true;
			}
			if(name.contains("upgrade")) {
				return false;
			}
			return true;
		}
		return false;
	}

}
