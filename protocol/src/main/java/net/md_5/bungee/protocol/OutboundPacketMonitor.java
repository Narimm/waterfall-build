package net.md_5.bungee.protocol;

import lombok.AllArgsConstructor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@AllArgsConstructor
public class OutboundPacketMonitor extends MessageToByteEncoder<ByteBuf>
{
	private boolean server;

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception
	{
		msg.markReaderIndex();
    	int packetId = DefinedPacket.readVarInt(msg);
    	msg.resetReaderIndex();

    	PacketTracker.monitorEncodePacket(ctx, packetId, msg.readableBytes(), server);

    	// Pass it on
    	out.writeBytes(msg);
	}

}
