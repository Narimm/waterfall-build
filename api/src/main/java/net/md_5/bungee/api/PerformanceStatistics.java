package net.md_5.bungee.api;

import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.config.ServerInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class PerformanceStatistics {
	private final List<UpstreamStatistics> upstreamStats;
	private final List<DownstreamStatistics> downstreamStats;

	@RequiredArgsConstructor
	@ToString
	@Getter
	public static class UpstreamStatistics {
		private final SocketAddress address;
		private final UUID userId;

		private final List<PacketInfo> packets;
	}

	@RequiredArgsConstructor
	@ToString
	@Getter
	public static class DownstreamStatistics {
		private final SocketAddress address;
		private final ServerInfo serverId;

		private final List<PacketInfo> packets;
	}

	@RequiredArgsConstructor
	@ToString
	@Getter
	public static class PacketInfo {
		private final int protocol;
		private final int id;

		private final int inboundCount;
		private final long inboundSize;

		private final int outboundCount;
		private final long outboundSize;
	}
}
