package b100.betabiomecolors;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import b100.lib.client.gui.ScreenWrapper;
import b100.lib.client.mixin.IScreen;

public class ModMenuIntegration implements ModMenuApi {
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new ScreenWrapper(new ConfigScreen((IScreen) parent));
	}
	
}
