package net.bridgesapi.core.i18n;

import net.bridgesapi.core.APIPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;

public class I18n {

    private static Configuration lang;

	public static void load(String lang, APIPlugin plugin) throws IOException {
		File file = new File(plugin.getDataFolder().getPath() + "/i18n/" + lang + ".lang");
		if (!file.exists()) {
			InputStream stream = plugin.getResource(lang + ".lang");
			if (stream == null) {
				stream = plugin.getResource("default.lang");
				lang = "default";
			}

			file = new File(plugin.getDataFolder().getPath() + "/i18n/" + lang + ".lang");
			if (!new File(plugin.getDataFolder().getPath() + "/i18n/").exists())
				new File(plugin.getDataFolder().getPath() + "/i18n/").mkdir();

			Files.copy(stream, file.toPath());
		}

		I18n.lang = YamlConfiguration.loadConfiguration(file);
	}

    public static String getTranslation(String key) {
        if (lang == null)
            try {
                load("default", APIPlugin.getInstance());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return lang.getString(key, key);
    }
}
