package net.md_5.bungee.protocol;

import io.netty.channel.ChannelHandlerContext;

public class PacketTracker {
	private static PacketRecorder recorder;

	public static void setRecorder(PacketRecorder packetRecorder) {
		recorder = packetRecorder;
	}

	public static void monitorDecodePacket(ChannelHandlerContext ctx, int packetId, long size, boolean server) {
		if (server) {
			recorder.monitorUpstreamPacket(ctx, packetId, size, true);
		} else {
			recorder.monitorDownstreamPacket(ctx, packetId, size, true);
		}
	}

	public static void monitorEncodePacket(ChannelHandlerContext ctx, int packetId, long size, boolean server) {
		if (server) {
			recorder.monitorUpstreamPacket(ctx, packetId, size, false);
		} else {
			recorder.monitorDownstreamPacket(ctx, packetId, size, false);
		}
	}
}
