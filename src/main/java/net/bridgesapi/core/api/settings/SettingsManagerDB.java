package net.bridgesapi.core.api.settings;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.settings.SettingsManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingsManagerDB implements SettingsManager {

	protected BukkitBridge api;

	public SettingsManagerDB(BukkitBridge api) {
		this.api = api;
	}

    public Map<String, String> getSettings(UUID player) {
        Map<String, String> data = api.getPlayerManager().getPlayerData(player).getValues();
        HashMap<String, String> settings = new HashMap<>();
        for (Map.Entry<String, String> line : data.entrySet()) {
            if (line.getKey().startsWith("settings.")) {
                String setting = line.getKey().split(".")[0];
                settings.put(setting, line.getValue());
            }
        }

        return settings;
    }

    public String getSetting(UUID player, String setting) {
		return api.getPlayerManager().getPlayerData(player).get("settings." + setting);
    }

    public void setSetting(UUID player, String setting, String value) {
		api.getPlayerManager().getPlayerData(player).set("settings." + setting, value);
    }
}
