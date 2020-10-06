package de.michab.lab;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdesktop.application.CliApplication;

/**
 *
 * @author micbinz
 */
public class SocketTest extends CliApplication
{
    private static final int testPort = 313;

    private void doServeImpl( Socket socket ) throws Exception
    {
        InputStream is =
                socket.getInputStream();
        OutputStream os =
                socket.getOutputStream();

        while ( true )
        {
            int c = is.read();

            if ( c == -1 )
                break;
            if ( c == 'x' )
                break;

            os.write( c );
            os.write( '-' );
        }

        socket.close();

        out( "Server thread shutdown.\n" );
    }

    private void doServe( Socket socket )
    {
        try {
            doServeImpl( socket );
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private void socketAcceptor( ServerSocket ss ) throws Exception
    {
        while ( true )
        {
            Socket socket =
                    ss.accept();
            out( "Connect ...\n" );
            Thread t = new Thread(
                    () -> doServe( socket ),
                    getClass().getSimpleName() );
            t.setDaemon(
                    true );
            t.start();
        }
    }

    public void socketServer()
    {
        try ( ServerSocket serverSocket =
                new ServerSocket( testPort ) )
        {

            socketAcceptor( serverSocket );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void startServer( boolean daemon )
    {
        Thread t = new Thread(
                () -> socketServer(),
                "start" );
        t.setDaemon( daemon );
        t.start();
    }

    /**
     * Start only the server.  Terminates if the server receives an
     * 'x' in its input stream.
     */
    @Command
    public void startServer()
    {
        out( "Operation %s\n", currentCommand() );

        startServer( false );
    }

    /**
     * Starts the full test in a single vm.
     *
     * @throws Exception In case of an error.
     */
    @Command
    public void start() throws Exception
    {
        out( "Operation %s\n", currentCommand() );

        startServer( true );

        // Connect to the server.
        Socket socket = new Socket(
                InetAddress.getLocalHost(),
                testPort );

        OutputStream os = socket.getOutputStream();
        // Write content to the server.
        os.write( "micbinz\nx".getBytes() );
        os.flush();
        InputStream is = socket.getInputStream();

        // Read and print server output.
        while ( true )
        {
            int c = is.read();
            if ( c == -1 )
                break;
            System.out.write( c );
        }

        socket.close();
    }

    /**
     *
     * @param argv
     */
    public static void main( String[] argv )
    {
        launch( SocketTest.class, argv );
    }
}
