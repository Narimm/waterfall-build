package net.md_5.bungee;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.command.ConsoleCommandSender;

public class BungeeCordLauncher
{

    public static void main(String[] args) throws Exception
    {
        Security.setProperty( "networkaddress.cache.ttl", "30" );
        Security.setProperty( "networkaddress.cache.negative.ttl", "10" );
        // For JDK9+ we force-enable multi-release jar file support #3087
        if ( System.getProperty( "jdk.util.jar.enableMultiRelease" ) == null )
        {
            System.setProperty( "jdk.util.jar.enableMultiRelease", "force" );
        }

        // Waterfall start
        // By default, Netty allocates 16MiB arenas for the PooledByteBufAllocator. This is too much
        // memory for Minecraft, which imposes a maximum packet size of 2MiB! We'll use 4MiB as a more
        // sane default.
        //
        // Note: io.netty.allocator.pageSize << io.netty.allocator.maxOrder is the formula used to
        // compute the chunk size. We lower maxOrder from its default of 11 to 9. (We also use a null
        // check, so that the user is free to choose another setting if need be.)
        if (System.getProperty("io.netty.allocator.maxOrder") == null) {
            System.setProperty("io.netty.allocator.maxOrder", "9");
        }
        // Waterfall end

        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.acceptsAll( Arrays.asList( "help" ), "Show the help" );
        parser.acceptsAll( Arrays.asList( "v", "version" ), "Print version and exit" );
        parser.acceptsAll( Arrays.asList( "noconsole" ), "Disable console input" );

        OptionSet options = parser.parse( args );

        if ( options.has( "help" ) )
        {
            parser.printHelpOn( System.out );
            return;
        }
        if ( options.has( "version" ) )
        {
            System.out.println( BungeeCord.class.getPackage().getImplementationVersion() );
            return;
        }

        if ( BungeeCord.class.getPackage().getSpecificationVersion() != null && System.getProperty( "IReallyKnowWhatIAmDoingISwear" ) == null )
        {
            Date buildDate = new SimpleDateFormat( "yyyyMMdd" ).parse( BungeeCord.class.getPackage().getSpecificationVersion() );

            Calendar deadline = Calendar.getInstance();
            deadline.add( Calendar.WEEK_OF_YEAR, -8 );
            if ( buildDate.before( deadline.getTime() ) )
            {
                System.err.println( "*** Hey! This build is potentially outdated :( ***" );
                System.err.println( "*** Please check for a new build from https://papermc.io/downloads ***" );
                System.err.println( "*** Should this build be outdated, you will get NO support for it. ***" );
                System.err.println( "*** Server will start in 10 seconds ***" );
                Thread.sleep( TimeUnit.SECONDS.toMillis( 10 ) );
            }
        }

        BungeeCord bungee = new BungeeCord();
        ProxyServer.setInstance( bungee );
        bungee.getLogger().info( "Enabled Waterfall version " + bungee.getVersion() );
        bungee.start();

        if ( !options.has( "noconsole" ) )
        {
            // Waterfall start - Use TerminalConsoleAppender
            new io.github.waterfallmc.waterfall.console.WaterfallConsole().start();
            /*
            String line;
            while ( bungee.isRunning && ( line = bungee.getConsoleReader().readLine( ">" ) ) != null )
            {
                if ( !bungee.getPluginManager().dispatchCommand( ConsoleCommandSender.getInstance(), line ) )
                {
                    bungee.getConsole().sendMessage( new ComponentBuilder( "Command not found" ).color( ChatColor.RED ).create() );
                }
            }
            */
            // Waterfall end
        }
    }
}
