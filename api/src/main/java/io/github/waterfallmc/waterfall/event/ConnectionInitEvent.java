package io.github.waterfallmc.waterfall.event;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.event.AsyncEvent;
import net.md_5.bungee.api.plugin.Cancellable;

import java.net.InetSocketAddress;

import lombok.ToString;

/**
 * Represents a brand new connection made to the proxy, allowing for plugins to
 * efficiently close a connection, useful for connection throttlers, etc
 */
@ToString
public class ConnectionInitEvent extends AsyncEvent<ConnectionInitEvent> implements Cancellable {

    private final InetSocketAddress remoteAddress;
    private boolean isCancelled = false;

    public ConnectionInitEvent(InetSocketAddress remoteAddress, Callback<ConnectionInitEvent> done) {
        super(done);
        this.remoteAddress = remoteAddress;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    /**
     * @return the INetSocketAddress of the connection being opened
     */
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
