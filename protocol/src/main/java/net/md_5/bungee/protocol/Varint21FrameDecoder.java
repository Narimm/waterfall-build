package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class Varint21FrameDecoder extends ByteToMessageDecoder
{

    private static boolean DIRECT_WARNING;
    // Waterfall start
    private boolean allowEmptyPackets;

    public Varint21FrameDecoder(boolean allowEmptyPackets) {
        this.allowEmptyPackets = allowEmptyPackets;
    }
    // Waterfall end

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        in.markReaderIndex();

        for ( int i = 0; i < 3; i++ ) // Waterfall
        {
            if ( !in.isReadable() )
            {
                in.resetReaderIndex();
                return;
            }

            // Waterfall start
            byte read = in.readByte();
            if ( read >= 0 )
            {
                in.resetReaderIndex();
                int length = DefinedPacket.readVarInt( in );
                // Waterfall end
                if ( length == 0 && !allowEmptyPackets) // Waterfall
                {
                    throw new CorruptedFrameException( "Empty Packet!" );
                }

                if ( in.readableBytes() < length )
                {
                    in.resetReaderIndex();
                    return;
                    // Waterfall start
                } else {
                    out.add(in.readRetainedSlice(length));
                    return;
                    // Waterfall end
                }
            }
        }

        throw new CorruptedFrameException( "length wider than 21-bit" );
    }
}
