package net.md_5.bungee.perfmon;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.md_5.bungee.api.PerformanceStatistics;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.PerformanceStatistics.DownstreamStatistics;
import net.md_5.bungee.api.PerformanceStatistics.PacketInfo;
import net.md_5.bungee.api.PerformanceStatistics.UpstreamStatistics;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.PerformanceTracker;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketRecorder;
import net.md_5.bungee.protocol.PacketTracker;
import net.md_5.bungee.protocol.Protocol;
import lombok.Getter;
import lombok.Setter;

public class PerformanceMonitor implements PerformanceTracker, PacketRecorder {
	@Getter
	@Setter
	private boolean enabled;

	private Object lock = new Object();

	private Map<SocketAddress, PerformanceEntry> upstreamPackets;
	private Map<SocketAddress, PerformanceEntry> downstreamPackets;

	public PerformanceMonitor() {
		upstreamPackets = Maps.newHashMap();
		downstreamPackets = Maps.newHashMap();

		PacketTracker.setRecorder(this);
	}

	private PerformanceEntry getEntry(Map<SocketAddress, PerformanceEntry> map, SocketAddress address) {
		PerformanceEntry entry = map.get(address);
		if (entry == null) {
			entry = new PerformanceEntry(address);
			map.put(address, entry);
		}

		return entry;
	}

	@Override
	public void monitorUpstreamPacket(ChannelHandlerContext ctx, int packetId, long size, boolean in) {
		if (!enabled)
			return;

		Protocol protocol = ctx.pipeline().get( MinecraftDecoder.class ).getProtocol();

		synchronized(lock) {
			PerformanceEntry entry = getEntry(upstreamPackets, ctx.channel().remoteAddress());
			if (in) {
				entry.addInboundPacket(protocol, packetId, size);
			} else {
				entry.addOutboundPacket(protocol, packetId, size);
			}
		}
	}

	@Override
	public void monitorDownstreamPacket(ChannelHandlerContext ctx, int packetId, long size, boolean in) {
		if (!enabled)
			return;

		Protocol protocol = ctx.pipeline().get( MinecraftDecoder.class ).getProtocol();

		synchronized(lock) {
			PerformanceEntry entry = getEntry(downstreamPackets, ctx.channel().remoteAddress());
			if (in) {
				entry.addInboundPacket(protocol, packetId, size);
			} else {
				entry.addOutboundPacket(protocol, packetId, size);
			}
		}
	}

	@Override
	public PerformanceStatistics getStatistics() {
		synchronized(lock) {
			// For resolving users
			Map<InetSocketAddress, ProxiedPlayer> userMap = Maps.newHashMap();
			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
				userMap.put(player.getAddress(), player);
			}

			// Compile upstream results
			List<UpstreamStatistics> upstreamStats = Lists.newArrayList();

			for (Entry<SocketAddress, PerformanceEntry> entry : upstreamPackets.entrySet()) {
				ProxiedPlayer player = userMap.get((InetSocketAddress)entry.getKey());

				List<PacketInfo> packets = entry.getValue().toPacketInfo();
				upstreamStats.add(new UpstreamStatistics(entry.getKey(), (player != null ? player.getUniqueId() : null), packets));
			}

			// For resolving servers
			Map<InetSocketAddress, ServerInfo> serverMap = Maps.newHashMap();
			for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
				serverMap.put(server.getAddress(), server);
			}

			// Compile downstream results
			List<DownstreamStatistics> downstreamStats = Lists.newArrayList();

			for (Entry<SocketAddress, PerformanceEntry> entry : downstreamPackets.entrySet()) {
				ServerInfo server = serverMap.get((InetSocketAddress)entry.getKey());

				List<PacketInfo> packets = entry.getValue().toPacketInfo();
				downstreamStats.add(new DownstreamStatistics(entry.getKey(), server, packets));
			}

			return new PerformanceStatistics(upstreamStats, downstreamStats);
		}
	}

	@Override
	public PerformanceStatistics getAndResetStatistics() {
		synchronized(lock) {
			PerformanceStatistics stats = getStatistics();
			resetStatistics();
			return stats;
		}
	}

	@Override
	public void resetStatistics() {
		synchronized(lock) {
			upstreamPackets.clear();
			downstreamPackets.clear();
		}
	}
}
