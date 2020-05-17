package net.md_5.bungee.tab;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.protocol.packet.PlayerListItem;

public class GlobalPing extends Global
{

    private static final int PING_THRESHOLD = 20;
    /*========================================================================*/
    private int lastPing;

    @Override
    public void onPingChange(int ping)
    {
        if ( ping - PING_THRESHOLD > lastPing && ping + PING_THRESHOLD < lastPing )
        {
            lastPing = ping;
            PlayerListItem packet = new PlayerListItem();
            packet.setAction( PlayerListItem.Action.UPDATE_LATENCY );
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid( player.getUniqueId() );
            item.setUsername( player.getName() );
            item.setDisplayName( TextComponent.fromLegacyText( player.getDisplayName() ) );
            item.setPing( player.getPing() );
            packet.setItems( new PlayerListItem.Item[]
            {
                item
            } );
            BungeeCord.getInstance().broadcast( packet );
        }
    }
}
