package io.alerium.velohub.objects.balancers;

import io.alerium.velohub.objects.Balancer;
import io.alerium.velohub.objects.Lobby;

import java.util.List;
import java.util.Random;

public class RandomBalancer implements Balancer {
    
    private final Random random = new Random();
    
    @Override
    public Lobby getLobby(List<Lobby> lobbies) {
        return lobbies.get(random.nextInt(lobbies.size()));
    }
    
}
