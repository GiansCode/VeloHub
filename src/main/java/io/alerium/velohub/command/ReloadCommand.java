package io.alerium.velohub.command;

import com.velocitypowered.api.command.SimpleCommand;
import io.alerium.velohub.VeloHubPlugin;
import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.util.logging.Level;

@RequiredArgsConstructor
public class ReloadCommand implements SimpleCommand {
    
    private final VeloHubPlugin plugin;
    
    @Override
    public void execute(Invocation invocation) {
        try {
            plugin.getConfig().reload();
            plugin.getLobbyManager().loadLobbies();
            
            invocation.source().sendMessage(plugin.getConfig().getMessage("successReload"));
        } catch (FileNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while reload", e);
            invocation.source().sendMessage(plugin.getConfig().getMessage("errorReload"));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("velohub.reload");
    }
}
