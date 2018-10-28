package net.md_5.bungee.perfmon;

import java.net.SocketAddress;
import java.util.List;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.PerformanceStatistics.PacketInfo;
import net.md_5.bungee.protocol.Protocol;
import lombok.Getter;

class PerformanceEntry {
	@Getter
	private final SocketAddress sourceAddress;

	private int[][] inboundPacketCounts;
	private long[][] inboundPacketSizes;

	private int[][] outboundPacketCounts;
	private long[][] outboundPacketSizes;

	public PerformanceEntry(SocketAddress source) {
		this.sourceAddress = source;

		inboundPacketCounts = new int[Protocol.values().length][Protocol.MAX_PACKET_ID+1];
		inboundPacketSizes = new long[Protocol.values().length][Protocol.MAX_PACKET_ID+1];
		outboundPacketCounts = new int[Protocol.values().length][Protocol.MAX_PACKET_ID+1];
		outboundPacketSizes = new long[Protocol.values().length][Protocol.MAX_PACKET_ID+1];
	}

	public void addInboundPacket(Protocol protocol, int id, long size) {
		inboundPacketCounts[protocol.ordinal()][id]++;
		inboundPacketSizes[protocol.ordinal()][id] += size;
	}

	public void addOutboundPacket(Protocol protocol, int id, long size) {
		outboundPacketCounts[protocol.ordinal()][id]++;
		outboundPacketSizes[protocol.ordinal()][id] += size;
	}

	public List<PacketInfo> toPacketInfo() {
		List<PacketInfo> list = Lists.newArrayListWithCapacity((Protocol.MAX_PACKET_ID+1) * Protocol.values().length);

		for (int p = 0; p < Protocol.values().length; ++p) {
			for (int i = 0; i < inboundPacketCounts[p].length; ++i) {
				if (inboundPacketCounts[p][i] != 0 || outboundPacketCounts[p][i] != 0) {
					PacketInfo info = new PacketInfo(p, i, inboundPacketCounts[p][i], inboundPacketSizes[p][i], outboundPacketCounts[p][i], outboundPacketSizes[p][i]);
					list.add(info);
				}
			}
		}

		return list;
	}
}
