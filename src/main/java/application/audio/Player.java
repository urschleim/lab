/* $Id$
 *
 * Audio tests.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */
package application.audio;

import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;



/**
 * Streams data from an in-line to an out line.
 *
 * @version $Rev: 544 $
 * @author micbinz
 */
class Player implements Runnable
{
    private final TargetDataLine _inLine;
    private final SourceDataLine _outLine;



    /**
     * Get the first available line from the passed line infos.
     *
     * @param mixer The mixer.
     * @param lineInfos An array of line infos.
     * @return The first available line.
     */
    private Line getLine( Mixer mixer, Line.Info[] lineInfos )
    {
        assert lineInfos != null;

        for ( Line.Info c : lineInfos )
        {
            try
            {
                Line currentLine = mixer.getLine( c );
                if ( currentLine != null )
                    return currentLine;
            }
            catch ( LineUnavailableException e )
            {
                // Ignore and try next.
                ;
            }
        }

        return null;
    }



    /**
     * Select the player's data source.
     *
     * @param mixer The mixer to use.
     * @return The player's data source.
     */
    private TargetDataLine getInLine( Mixer mixer )
    {
        return (TargetDataLine)getLine(
                mixer,
                mixer.getTargetLineInfo() );
    }



    /**
     * Select the player's data sink.
     *
     * @param mixer The mixer to use.
     * @return The player's data sink.
     */
    private SourceDataLine getOutLine( Mixer mixer )
    {
        return (SourceDataLine)getLine(
                mixer,
                mixer.getSourceLineInfo() );
    }



    /**
     * Create a instance.
     *
     * @param in The mixer to use to identify the data source.
     * @param out The mixer used to identify the data sink.
     */
    Player( Mixer in, Mixer out )
        throws LineUnavailableException
    {
        _inLine =
            getInLine( in );
        _outLine =
            getOutLine( out );

        if ( _inLine == null )
            throw new RuntimeException( "No in line." );
        if ( _outLine == null )
            throw new RuntimeException( "No out line." );

        try
        {
            _inLine.open();
            _outLine.open();
        }
        catch ( LineUnavailableException e )
        {
            dispose();
            throw e;
        }
    }



    /**
     * Performs actual data streaming.
     */
    private void streamData()
    {
        byte[] buffer = new byte[ 16 ];

        while ( ! Thread.currentThread().isInterrupted() )
        {
            int num = _inLine.read( buffer, 0, buffer.length );
            _outLine.write( buffer, 0, num );
        }
    }



    @Override
    public void run()
    {
        try
        {
            _inLine.start();
            _outLine.start();

            streamData();
        }
        finally
        {
            _inLine.stop();
            _outLine.stop();

            dispose();
        }
    }



    /**
     * Release resources.
     */
    private void dispose()
    {
        if ( _inLine != null )
            _inLine.close();
        if ( _outLine != null )
            _outLine.close();
    }
}
