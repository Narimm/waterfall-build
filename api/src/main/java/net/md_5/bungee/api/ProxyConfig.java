package net.md_5.bungee.api;

import java.util.Collection;
import java.util.Map;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * Core configuration adaptor for the proxy api.
 *
 * @deprecated This class is subject to rapid change between releases
 */
@Deprecated
public interface ProxyConfig
{

    /**
     * Time before users are disconnected due to no network activity.
     */
    int getTimeout();

    /**
     * UUID used for metrics.
     */
    String getUuid();

    /**
     * Set of all listeners.
     */
    Collection<ListenerInfo> getListeners();

    /**
     * Set of all servers.
     *
     * @deprecated The returned map may be modified concurrently by the proxy.
     *             The safe alternative is {@link #getServersCopy()}.
     */
    @Deprecated // Waterfall
    Map<String, ServerInfo> getServers();

    // Waterfall start - Dynamic server addition/removal api
    /**
     * Return all servers registered to this proxy, keyed by name. The returned map
     * is an immutable snapshot of the actual server collection. It cannot be modified,
     * and it will not change.
     *
     * @return all registered remote server destinations
     */
    Map<String, ServerInfo> getServersCopy();

    /**
     * Gets the server info of a server.
     *
     * @param name the name of the configured server
     * @return the server info belonging to the specified server
     */
    ServerInfo getServerInfo(String name);

    /**
     * Register the given server to the proxy.
     * Any currently registered server with the same name will be replaced.
     * This change is not saved to config.yml
     *
     * @param server The server to register with the proxy
     *
     * @return the previously registered server with the same name, or null if there was no such server.
     */
    ServerInfo addServer(ServerInfo server);

    /**
     * Register all of the given servers to the proxy.
     * This change is not saved to config.yml
     *
     * @param servers The collection of servers to register with the proxy
     *
     * @return true if any servers were added or replaced.
     */
    boolean addServers(Collection<ServerInfo> servers);

    /**
     * Un-register the server with the given name from the proxy.
     * This change is not saved to config.yml
     *
     * @param name The name of the server to unregister
     *
     * @return the server that was removed, or null if there is no server with the given name.
     */
    ServerInfo removeServerNamed(String name);

    /**
     * Un-register the given server from the proxy.
     * The server is matched by name only, other fields in the given {@link ServerInfo} are ignored.
     * This change is not saved to config.yml
     *
     * @param server the server to unregister from the proxy
     *
     * @return the server that was removed, or null if there is no server with a matching name.
     */
    ServerInfo removeServer(ServerInfo server);

    /**
     * Un-register servers with any of the given names from the proxy.
     * This change is not saved to config.yml
     *
     * @param names a collection of server names to be unregistered
     *
     * @return true if any servers were removed.
     */
    boolean removeServersNamed(Collection<String> names);

    /**
     * Un-register all of the given servers from the proxy.
     * The servers are matched by name only, other fields in the given {@link ServerInfo} are ignored.
     * This change is not saved to config.yml
     *
     * @param servers a collection of servers to be unregistered
     *
     * @return true if any servers were removed.
     */
    boolean removeServers(Collection<ServerInfo> servers);
    // Waterfall end

    /**
     * Does the server authenticate with mojang
     */
    boolean isOnlineMode();

    /**
     * Whether proxy commands are logged to the proxy log
     */
    boolean isLogCommands();

    /**
     * Returns the player max.
     */
    int getPlayerLimit();

    /**
     * A collection of disabled commands.
     */
    Collection<String> getDisabledCommands();

    /**
     * The connection throttle delay.
     */
    @Deprecated
    int getThrottle();

    /**
     * Whether the proxy will parse IPs with spigot or not
     */
    @Deprecated
    boolean isIpForward();

    /**
     * The encoded favicon.
     *
     * @deprecated Use #getFaviconObject instead.
     */
    @Deprecated
    String getFavicon();

    /**
     * The favicon used for the server ping list.
     */
    Favicon getFaviconObject();

    //
    // Waterfall Options
    //

    /**
     * The supported versions
     *
     * @return the supported versions
     */
    String getGameVersion();

    /**
     * Whether Netty's async DNS resolver is used for account authentication.
     *
     * @return whether Netty's async DNS resolver is used for account authentication.
     */
    boolean isUseNettyDnsResolver();

    // Throttling options

    /**
     * How often tab-complete packets can be sent.
     * <br>
     * Values in milliseconds.
     *
     * @return how often tab-complete packets can be sent in milliseconds
     */
    int getTabThrottle();

    /**
     * Should we disable the tab completion limit for 1.13+ clients
     *
     * @return should we disable the tab completion limit for 1.13+ clients
     */
    boolean isDisableModernTabLimiter();
}
