package io.alerium.velohub.objects.balancers;

import io.alerium.velohub.objects.Balancer;
import io.alerium.velohub.objects.Lobby;

import java.util.List;

public class FirstAvailableBalancer implements Balancer {
    
    @Override
    public Lobby getLobby(List<Lobby> lobbies) {
        for (Lobby lobby : lobbies) {
            int onlinePlayers = lobby.getServer().getPlayersConnected().size();
            if (onlinePlayers < lobby.getMaxPlayers())
                return lobby;
        }
        
        return null;
    }
    
}
