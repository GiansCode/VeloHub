package io.alerium.velohub.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import io.alerium.velohub.VeloHubPlugin;
import io.alerium.velohub.objects.Lobby;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerListener {

    private final VeloHubPlugin plugin;
    
    @Subscribe
    public void onInitialServerSelect(PlayerChooseInitialServerEvent event) {
        Lobby lobby = plugin.getLobbyManager().getLobby();
        if (lobby == null) {
            event.getPlayer().disconnect(plugin.getConfig().getMessage("kickNoLobbyAvailable"));
            return;
        }
        
        event.setInitialServer(lobby.getServer());
    }
    
    @Subscribe
    public void onKick(KickedFromServerEvent event) {
        if (!plugin.getLobbyManager().isSendPlayerToHubOnClose())
            return;
        
        Optional<Component> kickReason = event.getServerKickReason();
        if (!kickReason.isPresent())
            return;
        
        String json = GsonComponentSerializer.gson().serialize(kickReason.get());
        if (!json.toLowerCase().contains("server closed"))
            return;
        
        Lobby lobby = plugin.getLobbyManager().getLobby();
        if (lobby == null) {
            event.setResult(KickedFromServerEvent.DisconnectPlayer.create(plugin.getConfig().getMessage("kickNoLobbyAvailable")));
            return;
        }
        
        event.setResult(KickedFromServerEvent.RedirectPlayer.create(lobby.getServer()));
    }
    
}
