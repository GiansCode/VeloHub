package io.alerium.velohub.objects;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor @Getter
public class Lobby {
    
    private final String name;
    private final int lowestProtocolVersion;
    private final RegisteredServer server;
    
    @Setter private int maxPlayers = -1;
    
}
