package io.alerium.velohub.objects.balancers;

import io.alerium.velohub.objects.Balancer;
import io.alerium.velohub.objects.Lobby;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LowestBalancer implements Balancer {
    
    @Override
    public Lobby getLobby(List<Lobby> lobbies) {
        Lobby lowestLobby = null;
        
        for (Lobby lobby : lobbies) {
            if (lowestLobby == null || lowestLobby.getServer().getPlayersConnected().size() > lobby.getServer().getPlayersConnected().size())
                lowestLobby = lobby;
        }
        
        return lowestLobby;
    }
    
}
