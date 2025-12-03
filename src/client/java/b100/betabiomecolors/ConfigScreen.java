package b100.betabiomecolors;

import b100.lib.client.gui.ActionListener;
import b100.lib.client.gui.GuiButton;
import b100.lib.client.gui.GuiElement;
import b100.lib.client.gui.GuiScreen;
import b100.lib.client.mixin.IScreen;
import b100.lib.client.translate.Translate;
import net.minecraft.text.Text;

public class ConfigScreen extends GuiScreen implements ActionListener {

	public GuiButton enableModButton;
	public GuiButton linearInterpolationButton;
	public GuiButton doneButton;
	
	public ConfigScreen(IScreen parentScreen) {
		super(parentScreen);
	}

	@Override
	protected void onInit() {
		enableModButton = new GuiButton(this, Text.of("#"));
		linearInterpolationButton = new GuiButton(this, Text.of("#"));
		doneButton = new GuiButton(this, Text.of("#"));
		
		enableModButton.addActionListener(this);
		linearInterpolationButton.addActionListener(this);
		doneButton.addActionListener(this);
		
		add(enableModButton);
		add(linearInterpolationButton);
		add(doneButton);

		update();
	}
	
	public void update() {
		enableModButton.text = Text.of(Translate.translateIfExists(BetaBiomeColors.MODID + ".option.enableMod") + ": " + booleanString(BetaBiomeColorsConfig.modEnabled));
		
		if(BetaBiomeColors.isSodiumInstalled()) {
			linearInterpolationButton.text = Text.of(Translate.translateIfExists(BetaBiomeColors.MODID + ".option.smoothing") + ": " + booleanString(BetaBiomeColorsConfig.useSodiumLinearInterpolation));
			linearInterpolationButton.setClickable(true);
		}else {
			linearInterpolationButton.text = Text.of(Translate.translateIfExists(BetaBiomeColors.MODID + ".option.smoothing.no_sodium"));
			linearInterpolationButton.setClickable(false);
		}
		
		doneButton.text = Translate.translate(BetaBiomeColors.MODID + ".button.done");
	}
	
	public static String booleanString(boolean val) {
		return val ? Translate.translateIfExists(BetaBiomeColors.MODID + ".value.on") : Translate.translateIfExists(BetaBiomeColors.MODID + ".value.off");
	}

	@Override
	public void actionPerformed(GuiElement source) {
		if(source == enableModButton) {
			BetaBiomeColorsConfig.modEnabled = !BetaBiomeColorsConfig.modEnabled;
			BetaBiomeColors.reloadChunks();
		}
		if(source == linearInterpolationButton) {
			BetaBiomeColorsConfig.useSodiumLinearInterpolation = !BetaBiomeColorsConfig.useSodiumLinearInterpolation;
			BetaBiomeColors.reloadChunks();
		}
		if(source == doneButton) {
			BetaBiomeColorsConfig.save(BetaBiomeColors.CONFIG_FILE);
			close();
			return;
		}
		update();
	}
	
	@Override
	public void onResize() {
		final int buttonWidth = 200;
		int x = (width - buttonWidth) / 2;
		
		for(int i=0; i < elements.size(); i++) {
			GuiElement element = elements.get(i);
			
			int y;
			if(i == elements.size() - 1) {
				y = height - 40;	
			}else {
				y = 32 + i * 24;
			}
			
			element.setPosition(x, y);
		}
	}
	
}
