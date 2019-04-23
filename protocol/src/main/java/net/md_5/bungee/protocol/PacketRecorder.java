package net.md_5.bungee.protocol;

import io.netty.channel.ChannelHandlerContext;

public interface PacketRecorder {
	public void monitorUpstreamPacket(ChannelHandlerContext ctx, int packetId, long size, boolean in);
	public void monitorDownstreamPacket(ChannelHandlerContext ctx, int packetId, long size, boolean in);
}
