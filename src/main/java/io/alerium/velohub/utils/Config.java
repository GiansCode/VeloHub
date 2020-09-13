package io.alerium.velohub.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Path;

public class Config {
    
    private static final Gson gson = new Gson();
    
    private final Object plugin;
    private final File file;
    @Getter private JsonObject object;
    
    public Config(Object plugin, Path folder, String name) throws IOException {
        this.plugin = plugin;
        File folderFile = folder.toFile();
        if (!folderFile.exists())
            folderFile.mkdir();
        
        file = new File(folderFile, name + ".json");
        if (!file.exists())
            createFile();
        
        reload();
    }

    /**
     * This method reloads the Config
     * @throws FileNotFoundException When the file doesn't exist
     */
    public void reload() throws FileNotFoundException {
        object = gson.fromJson(new FileReader(file), JsonObject.class);
    }

    /**
     * This method loads a Component using MiniMessage
     * @param name The message name
     * @param placeholders The placeholders
     * @return The Component
     */
    public Component getMessage(String name, String... placeholders) {
        return MiniMessage.get().parse(object.getAsJsonObject("messages").get(name).getAsString(), placeholders);
    }
    
    private void createFile() throws IOException {
        InputStream stream = plugin.getClass().getClassLoader().getResourceAsStream(file.getName());
        
        file.createNewFile();
        FileUtils.copyInputStreamToFile(stream, file);
    }
    
}
