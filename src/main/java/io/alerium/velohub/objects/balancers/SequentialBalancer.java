package io.alerium.velohub.objects.balancers;

import io.alerium.velohub.objects.Balancer;
import io.alerium.velohub.objects.Lobby;

import java.util.List;

public class SequentialBalancer implements Balancer {

    private int lastServer = 0;

    @Override
    public Lobby getLobby(List<Lobby> lobbies) {
        lastServer++;
        if (lobbies.size() >= lastServer)
            lastServer = 0;
        
        return lobbies.get(lastServer);
    }
    
}
