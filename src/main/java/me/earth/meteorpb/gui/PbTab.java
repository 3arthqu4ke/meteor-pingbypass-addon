package me.earth.meteorpb.gui;

import me.earth.pingbypass.PingBypass;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.client.gui.screen.Screen;

public class PbTab extends Tab {
    private final PingBypass pingBypass;

    public PbTab(PingBypass pingBypass) {
        super("PingBypass-" + StringUtil.capitalize(pingBypass.getSide().getName()));
        this.pingBypass = pingBypass;
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new PbModulesScreen(pingBypass, this, theme);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof PbModulesScreen;
    }

}
