/* $Id: Util.java 2116 2018-12-21 16:06:12Z Michael $
 *
 * Common.
 *
 * Released under Gnu Public License
 * Copyright © 2011 Michael G. Binz
 */
package de.michab.lab;

import java.awt.Component;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * Helper operations.
 *
 * @version $Rev: 2116 $
 * @author micbinz
 */
public class Util {

    private static Logger LOG = Logger.getLogger( Util.class.getName() );

    /**
     * Closes everything that has a 'close()' operation. This is to prevent an explosion of
     * forceClose operations.
     *
     * @param pStream
     *            the object that is to be closed. {@code null} is allowed.
     * @throws IllegalArgumentException
     *             If the passed object has no close() operation, since this must be a programming
     *             error.
     */
    public static void forceClose( Object closeable )
    {
        if ( closeable == null )
        {
            return;
        }

        try {
            Method closeOperation = closeable.getClass().getMethod( "close" );

            closeOperation.invoke(closeable);
        }
        catch (NoSuchMethodException e) {

            throw new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {

            Throwable t = e;

            if (t instanceof InvocationTargetException) {
                t = t.getCause();
            }

            if (LOG.isLoggable(Level.FINE))
                LOG.log(Level.FINE, t.getMessage(), t);
        }
    }

    /**
     * Shows a modal error dialog.
     *
     * @param e The error that led to this dialog.
     * @param parent The dialog parent.
     */
    public static void showErrorDialog( Exception e, Component parent ) {
        JOptionPane.showMessageDialog(
            parent,
            e.getLocalizedMessage(),
            "An error occurred.",
            JOptionPane.ERROR_MESSAGE,
            null );
    }

    /**
     * Get the name of the local host.
     *
     * @return The name of the local host.
     */
    public static String getLocalHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch ( IOException e )
        {
            return getLocalHostAddress();
        }
    }

    /**
     * Get the name of the local host.
     *
     * @return The name of the local host.
     */
    public static String getLocalHostAddress()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch ( IOException ee )
        {
            return "127.0.0.1";
        }
    }

    /**
     * Creates a socket based on the normal hostname:port notation.
     *
     * @param hostPort The socket address based on hostname:port notation.
     * @return A newly allocated socket.
     * @throws MalformedURLException In case the syntax of hostname:port is wrong.
     * @throws IOException If the port could not be opened.
     */
    public static Socket createSocket( String hostPort ) throws IOException
    {
        String[] args = hostPort.split( ":" );
        if ( args.length != 2 )
            throw new MalformedURLException( hostPort );

        args[0] = args[0].trim();

        if ( args[0].isEmpty() )
            throw new MalformedURLException( hostPort );

        int port;
        try
        {
            port = Integer.parseInt( args[1] );
        }
        catch ( NumberFormatException e )
        {
            throw new MalformedURLException( hostPort );
        }

        return new Socket( args[0], port );
    }

    private static final char QUOTE_CHAR = '\"';

    /**
     * Quotes and concatenates a set of strings.
     * TODO(micbinz) does not support quoting of quote characters.
     * @param parts The strings to concatenate and quote.
     * @return The resulting string.
     * @see #splitQuoted(String)
     */
    public static String quote( char quoteChar, String part )
    {
        if ( ! Util.hasContent( part ) )
            return "";

        StringBuilder result = new StringBuilder();

        result.append( quoteChar );
        // TODO(micbinz) do also internal quoting.
        result.append( part );
        result.append( quoteChar );

        return result.toString();
    }
    public static String quote( String part )
    {
        return quote( QUOTE_CHAR, part );
    }

//    /**
//     * Splits a whitespace delimited and quoted string as used on command
//     * lines into its elements.  For example 'Admiral "von Schneider"' is
//     * split into 'Admiral' and 'von Schneider'.
//     *
//     * TODO(micbinz) does not support quoting of quote characters.
//     * @param toParse The string to split
//     * @return The split strings.
//     */
//    public static String[] splitQuoted( char quoteChar, String someString )
//    {
//        // See also http://stackoverflow.com/questions/10695143/split-a-quoted-string-with-a-delimiter
//        // for a sketch of solving the same with regular expressions.  Can be made workable, but is even less
//        // understandable.
//
//        boolean inDoubleQuotes = false;
//
//        ArrayList<String> result = new ArrayList<String>();
//        StringBuilder sb = new StringBuilder();
//
//        for ( char c : someString.toCharArray() )
//        {
//            boolean isDoubleQuote = c == quoteChar;
//
//            if ( isDoubleQuote )
//            {
//                inDoubleQuotes = !inDoubleQuotes;
//
//                if ( ! inDoubleQuotes )
//                {
//                    // End of the quoted sequence.
//                    result.add( sb.toString() );
//                    sb.setLength( 0 );
//                }
//                continue;
//            }
//
//            if ( inDoubleQuotes )
//            {
//                sb.append( c );
//                continue;
//            }
//            else if ( Character.isWhitespace( c ) )
//            {
//                if ( sb.length() > 0 )
//                {
//                    result.add( sb.toString() );
//                    sb.setLength( 0 );
//                }
//                continue;
//            }
//
//            sb.append( c );
//        }
//
//        if ( sb.length() > 0 || inDoubleQuotes )
//            result.add( sb.toString() );
//
//        return result.toArray( new String[ result.size()] );
//    }
//    public static String[] splitQuoted( String someString )
//    {
//        return splitQuoted( QUOTE_CHAR, someString );
//    }

    /**
     * Check whether the passed string is not null and has non-whitespace content.
     *
     * @param string The string to test.
     * @return true if the string has content.
     */
    public static boolean hasContent( String string )
    {
        if ( string == null )
            return false;

        return ! string.trim().isEmpty();
    }

    /**
     * Read a byte with a timeout from a socket.
     *
     * @param socket The socket to read from.
     * @param timeoutMs The timeout in non-zero microseconds.
     * @return null if no byte could be read in the timeout, otherwise a byte.
     * @throws IOException If socket communication failed.
     */
    public static Byte readSocket( Socket socket, int timeoutMs )
        throws IOException
    {
        int originalTimeout = socket.getSoTimeout();

        try
        {
            socket.setSoTimeout( timeoutMs );

            int result = socket.getInputStream().read();

            socket.setSoTimeout( originalTimeout );
            return Byte.valueOf( (byte)result );
        }
        catch ( SocketTimeoutException e )
        {
            socket.setSoTimeout( originalTimeout );
            return null;
        }
    }


//    static void testSplitQuote()
//    {
//        // Test split quote.
//        String[] testCases = {
//                // Plain
//                "ab cd ef",
//                // Whitespace is tab.
//                "ab\tcd\tef",
//                // Whitespace is mixed and at the end.
//                "ab\tcd ef\t \t \t \t \tgh \t",
//                // Quoted simple.
//                "ab \"cd ef\" gh",
//                // Quoted leading and trailing spaces.
//                "ab \" cd ef \" gh",
//                // Last quote not terminated, trailing space.
//                "ab \" cd ef ",
//                // Empty string.
//                "ab \"\" cd",
//                // Pathological: ab" cd ef" -> "ab cd ef"
//                "ab\" cd ef ",
//                // Empty string at eol.
//                "michael \""
//        };
//
//        for ( String c : testCases )
//        {
//            System.err.println( "parseQuoted( '" + c + "' )" );
//            for ( String c1 : StringUtils.splitQuoted( c ) )
//                System.err.println( "'" + c1 + "'" );
//        }
//    }

    /**
     * Concatenates the strings in the passed list using the passed delimiter. For example:
     * '#', [Tick Trick Track] -> "Tick#Trick#Track".
     *
     * @param delimiter The delimiter to use.
     * @param list The list of strings.
     * @return A concatenated string.
     */
    public static String concat( char delimiter, String[] list )
    {
        if ( list.length == 0 )
            return "";

        StringBuilder result = null;

        for ( String c : list )
        {
            if ( result == null )
                result = new StringBuilder();
            else
                result.append( delimiter );

            result.append( c );
        }

        return result.toString();
    }
    public static String concat( char delimiter, List<String> list )
    {
        return concat( delimiter, list.toArray( new String[list.size()] ) );
    }

    public static void testConcat()
    {
        String[] nephewsArray = new String[]{ "Huey", "Dewey", "Louie" };
        List<String> nephewsList = Arrays.asList( nephewsArray );
        String[] uncaArray = new String[]{ "Donald" };

        String result;

        result = concat( ';', nephewsArray );
        System.err.println( result);
        result = concat( '_', nephewsList );
        System.err.println( result);
        result = concat( '_', uncaArray );
        System.err.println( result);
    }

    private Util() {
        throw new AssertionError();
    }
}
