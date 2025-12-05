package b100.betabiomecolors;

import b100.lib.client.gui.ActionListener;
import b100.lib.client.gui.Focusable;
import b100.lib.client.gui.GuiButton;
import b100.lib.client.gui.GuiElement;
import b100.lib.client.gui.GuiScreen;
import b100.lib.client.gui.GuiTextField;
import b100.lib.client.gui.Textures;
import b100.lib.client.gui.Textures.GuiTextures;
import b100.lib.client.mixin.IScreen;
import b100.lib.client.translate.Translate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;

public class ConfigScreen extends GuiScreen implements ActionListener {

	public GuiButton enableModButton;
	public GuiButton linearInterpolationButton;
	public GuiTextField seedTextField;
	public GuiButton doneButton;

	private int borderPos1;
	private int borderPos2;
	
	public ConfigScreen(IScreen parentScreen) {
		super(parentScreen);
	}

	@Override
	protected void onInit() {
		enableModButton = new GuiButton(this, Text.of("#"));
		enableModButton.addActionListener(this);
		add(enableModButton);
		
		linearInterpolationButton = new GuiButton(this, Text.of("#"));
		linearInterpolationButton.addActionListener(this);
		add(linearInterpolationButton);
		
		seedTextField = new GuiTextField(this, Text.of("#"));
		seedTextField.setText(String.valueOf(BetaBiomeColorsConfig.seed));
		add(seedTextField);
		
		doneButton = new GuiButton(this, Text.of("#"));
		doneButton.addActionListener(this);
		add(doneButton);
		
		update();
		
		setBackgroundScissorEnabled(true);
	}
	
	public void update() {
		enableModButton.text = Text.of(translate("option.enableMod") + ": " + booleanString(BetaBiomeColorsConfig.modEnabled));
		
		if(BetaBiomeColors.isSodiumInstalled()) {
			linearInterpolationButton.text = Text.of(translate("option.smoothing") + ": " + booleanString(BetaBiomeColorsConfig.useSodiumLinearInterpolation));
			linearInterpolationButton.setClickable(true);
		}else {
			linearInterpolationButton.text = Text.of(translate("option.smoothing.no_sodium"));
			linearInterpolationButton.setClickable(false);
		}
		
		doneButton.text = Text.of(translate("button.done"));
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
			back();
			return;
		}
		update();
	}
	
	@Override
	public void focusChanged(Focusable focusable) {
		super.focusChanged(focusable);
		if(focusable == seedTextField && !focusable.isFocused()) {
			updateSeed();
		}
	}
	
	@Override
	public void back() {
		updateSeed();
		super.back();
	}
	
	@Override
	public void close() {
		updateSeed();
		super.close();
	}
	
	public void updateSeed() {
		String seedString = seedTextField.getText();
		if(seedString.length() == 0) {
			return;
		}
		long seed;
		try {
			seed = Long.parseLong(seedString);
		}catch (Exception e) {
			seed = seedString.hashCode();
		}
		if(BetaBiomeColorsConfig.seed != seed) {
			BetaBiomeColors.setSeed(seed);
			BetaBiomeColors.reloadChunks();
		}
	}
	
	@Override
	public void draw() {
		GuiTextures textures = Textures.INSTANCE.getCurrentGuiTextures();
		
		final int separatorSize = 2;
		
		if(utils.isInWorld()) {
			MatrixStack matrixStack = utils.drawContext.getMatrices();
			matrixStack.push();
			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0f));
			
			utils.drawTexture(textures.headerSeparator, 0, -borderPos2 - 2, 0, 0, height, separatorSize, 32, separatorSize);
			utils.drawTexture(textures.footerSeparator, 0, -borderPos1, 0, 0, height, separatorSize, 32, separatorSize);
			
			matrixStack.pop();
		}
		
		super.draw();
		
		utils.drawCenteredString(Text.of("Beta Biome Colors"), width / 2, 20, 0xFFFFFF, true);
		utils.drawString(translate("option.seed"), seedTextField.posX, seedTextField.posY - 10, 0xFFFFFF, true);
	}
	
	@Override
	public void onResize() {
		super.onResize();
		
		final int buttonWidth = 200;
		int x = (width - buttonWidth) / 2;
		
		enableModButton.setPosition(x, 40 + 0 * 24);
		linearInterpolationButton.setPosition(x, 40 + 1 * 24);
		seedTextField.setPosition(x, 40 + 2 * 24 + 1 * 12);
		
		doneButton.setPosition(x, height - 40);
		
		final int p = 8;
		int w = buttonWidth + 2 * p;
		int x1 = (width - w) / 2;
		setBackgroundScissorArea(x1, 0, w, height);
		
		borderPos1 = x1;
		borderPos2 = x1 + w;
	}
	
	public static String booleanString(boolean val) {
		return val ? translate("value.on") : translate("value.off");
	}
	
	public static String translate(String key) {
		String key2 = "betabiomecolors." + key;
		if(Translate.translationExists(key2)) {
			return Translate.translateToString(key2);
		}
		return key;
	}
	
}
