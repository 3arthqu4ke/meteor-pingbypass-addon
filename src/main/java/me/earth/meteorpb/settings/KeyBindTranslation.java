package me.earth.meteorpb.settings;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.input.Bind;
import me.earth.pingbypass.api.input.Key;
import meteordevelopment.meteorclient.utils.misc.Keybind;

public class KeyBindTranslation {
    public static Keybind getMeteorBind(Bind bind) {
        return bind.getKeys()
                   .stream()
                   .map(key -> key.getType() == Key.Type.KEYBOARD ? Keybind.fromKey(key.getCode()) : Keybind.fromButton(key.getCode()))
                   .findFirst()
                   .orElse(Keybind.none());
    }

    public static Bind getPbBind(PingBypass pingBypass, Keybind bind) {
        int value = bind.getValue();
        boolean isKey = bind.isKey();
        if (value == -1) {
            return Bind.none();
        }

        Key key = pingBypass.getKeyBoardAndMouse().getKeyByCode(isKey ? Key.Type.KEYBOARD : Key.Type.MOUSE, value);
        if (key == null) {
            return Bind.none();
        }

        return new Bind(key);
    }

}
