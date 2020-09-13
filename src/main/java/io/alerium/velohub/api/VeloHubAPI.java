package io.alerium.velohub.api;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import io.alerium.velohub.VeloHubPlugin;
import io.alerium.velohub.objects.Lobby;

import java.util.Optional;

public final class VeloHubAPI {
    private VeloHubAPI() {
    }
    
    private static VeloHubPlugin plugin;

    public static void init(VeloHubPlugin pl) {
        plugin = pl;
    }

    /**
     * This method gets the Lobby where a Player is currently
     * @param player The Player
     * @return The Lobby, can be null
     */
    public static Lobby getLobby(Player player) {
        Optional<ServerConnection> server = player.getCurrentServer();
        if (!server.isPresent())
            return null;
        
        return plugin.getLobbyManager().getLobby(server.get().getServer().getServerInfo().getName());
    }
    
}
