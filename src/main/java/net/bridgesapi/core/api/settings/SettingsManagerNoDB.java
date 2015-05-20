package net.bridgesapi.core.api.settings;

import net.bridgesapi.api.settings.SettingsManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingsManagerNoDB implements SettingsManager {

    public Map<String, String> getSettings(UUID player) {
        return new HashMap<>();
    }

    public String getSetting(UUID player, String setting) {
		return null;
    }

    public void setSetting(UUID player, String setting, String value) {
    }
}
