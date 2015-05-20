package net.bridgesapi.core.i18n;

import com.google.common.io.ByteStreams;
import net.bridgesapi.core.APIPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class I18n {

    private static Configuration lang;

    public static void load(String lang, APIPlugin plugin) throws IOException {
        File file = new File(plugin.getDataFolder().getPath() + "/i18n/" + lang + ".lang");
        if (!file.exists()) {
            file = new File(plugin.getDataFolder().getPath() + "/i18n/default.lang");
            if (!file.exists()) {
                new File(plugin.getDataFolder().getPath() + "/i18n/").mkdir();
                file.createNewFile();
                try (InputStream is = plugin.getResource("default.lang");
                     OutputStream os = new FileOutputStream(file)) {
                     ByteStreams.copy(is, os);
                }
            }
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
