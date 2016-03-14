package net.md_5.bungee;

import com.google.common.base.Joiner;
import com.google.common.primitives.UnsignedLongs;
import com.google.common.primitives.Ints;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import io.github.waterfallmc.waterfall.utils.Hex;

/**
 * Series of utility classes to perform various operations.
 */
public class Util
{

    public static final int DEFAULT_PORT = 25565;

    /**
     * Method to transform human readable addresses into usable address objects.
     *
     * @param hostline in the format of 'host:port'
     * @return the constructed hostname + port.
     */
    public static InetSocketAddress getAddr(String hostline)
    {
        URI uri;
        try
        {
            uri = new URI( "tcp://" + hostline );
        } catch ( URISyntaxException ex )
        {
            throw new IllegalArgumentException( "Bad hostline", ex );
        }

        return new InetSocketAddress( uri.getHost(), ( uri.getPort() ) == -1 ? DEFAULT_PORT : uri.getPort() );
    }

    /**
     * Formats an integer as a hex value.
     *
     * @param i the integer to format
     * @return the hex representation of the integer
     */
    public static String hex(int i)
    {
        return Hex.encodeString(Ints.toByteArray(i));
    }

    /**
     * Constructs a pretty one line version of a {@link Throwable}. Useful for
     * debugging.
     *
     * @param t the {@link Throwable} to format.
     * @return a string representing information about the {@link Throwable}
     */
    public static String exception(Throwable t)
    {
        // TODO: We should use clear manually written exceptions
        StackTraceElement[] trace = t.getStackTrace();
        return t.getClass().getSimpleName() + " : " + t.getMessage()
                + ( ( trace.length > 0 ) ? " @ " + t.getStackTrace()[0].getClassName() + ":" + t.getStackTrace()[0].getLineNumber() : "" );
    }

    public static String csv(Iterable<?> objects)
    {
        return format( objects, ", " );
    }

    public static String format(Iterable<?> objects, String separators)
    {
        return Joiner.on( separators ).join( objects );
    }

    /**
     * Converts a String to a UUID
     *
     * @param uuid The string to be converted
     * @return The result
     */
    public static UUID getUUID(String uuid)
    {
        return new UUID( UnsignedLongs.parseUnsignedLong( uuid.substring( 0, 16 ), 16 ), UnsignedLongs.parseUnsignedLong( uuid.substring( 16 ), 16 ) );
    }
}
