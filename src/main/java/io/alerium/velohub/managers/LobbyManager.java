package io.alerium.velohub.managers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import io.alerium.velohub.VeloHubPlugin;
import io.alerium.velohub.objects.Balancer;
import io.alerium.velohub.objects.Lobby;
import io.alerium.velohub.objects.balancers.FirstAvailableBalancer;
import io.alerium.velohub.objects.balancers.LowestBalancer;
import io.alerium.velohub.objects.balancers.RandomBalancer;
import io.alerium.velohub.objects.balancers.SequentialBalancer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

@RequiredArgsConstructor
public class LobbyManager {

    private final VeloHubPlugin plugin;
    private final List<Lobby> lobbies = Collections.synchronizedList(new ArrayList<>());
    
    private Balancer balancer;
    @Getter private boolean sendPlayerToHubOnClose;

    /**
     * This method loads all the lobbies from the config file
     */
    public void loadLobbies() {
        lobbies.clear();
        
        for (JsonElement element : plugin.getConfig().getObject().getAsJsonArray("lobby-servers")) {
            JsonObject object = element.getAsJsonObject();
            String name = object.get("name").getAsString();
            int protocolVersion = object.get("lowest-protocol-version").getAsInt();
            
            Optional<RegisteredServer> optionalServer = plugin.getServer().getServer(name);
            if (!optionalServer.isPresent()) {
                plugin.getLogger().info("Invalid server name: " + name);
                continue;
            }

            RegisteredServer server = optionalServer.get();
            Lobby lobby = new Lobby(name, protocolVersion, server);
            
            server.ping().whenComplete((ping, exception) -> {
                if (ping == null) {
                    plugin.getLogger().log(Level.WARNING, "An error occurred while pinging " + lobby.getName(), exception);
                    return;
                }
                
                Optional<ServerPing.Players> players = ping.getPlayers();
                if (!players.isPresent())
                    return;
                
                lobby.setMaxPlayers(players.get().getMax());
            });
            lobbies.add(lobby);
        }
        
        plugin.getLogger().info("Loaded " + lobbies.size() + " lobbies.");

        balancer = getBalancer(plugin.getConfig().getObject().get("load-balancing-method").getAsString());
        sendPlayerToHubOnClose = plugin.getConfig().getObject().get("send-player-to-hub-on-server-close").getAsBoolean();
    }

    /**
     * This method gets the first available Lobby
     * @return The Lobby, can be null
     */
    public Lobby getLobby() {
        return balancer.getLobby(lobbies);
    }

    /**
     * This method gets the Lobby based on the server name
     * @param name The server name
     * @return The Lobby, can be null
     */
    public Lobby getLobby(String name) {
        for (Lobby lobby : lobbies) {
            if (lobby.getName().equalsIgnoreCase(name))
                return lobby;
        }
        
        return null;
    }
    
    private Balancer getBalancer(String type) {
        switch (type.toUpperCase()) {
            case "LOWEST":
                return new LowestBalancer();
            case "FIRSTAVAILABLE":
                return new FirstAvailableBalancer();
            case "RANDOM":
                return new RandomBalancer();
            case "SEQUENTIAL":
                return new SequentialBalancer();
            default:
                return null;
        }
    }
    
}
