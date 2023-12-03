/*
 * Common.
 *
 * Released under Gnu Public License
 * Copyright © 2013 Michael G. Binz
 */
package de.michab.lab;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.swing.event.SwingPropertyChangeSupport;

import org.jdesktop.util.InterruptibleThread;


/**
 * Connects an input stream and an output stream by continuously forwarding the
 * data received from the input stream to the output stream.
 *
 * @version $Rev: 1992 $
 * @author micbinz
 */
public final class StreamConnector
{
    private final Logger LOG = Logger.getLogger( getClass().getName() );

    private final InputStream _src;
    private final OutputStream _target;

    private final Thread _dataPump;

    /**
     * Create a bridge connecting the passed input and output stream.
     *
     * @param source The data source.
     * @param target The data sink.
     */
    public StreamConnector( InputStream source, OutputStream target )
    {
        if ( source == null )
            throw new NullPointerException( "source==null" );
        if ( target == null )
            throw new NullPointerException( "target==null" );

        _src = source;
        _target =  target;

        _dataPump = new InterruptibleThread(
                runner,
                getClass().getSimpleName(),
                true );

        _dataPump.start();
    }

    /**
     * The data-pumping thread.
     */
    private final Runnable runner = new Runnable()
    {
        @Override
        public void run()
        {
            byte[] buffer = new byte[512];

            try
            {
                while ( ! Thread.interrupted() )
                {
                    int number = _src.read( buffer );

                    if ( number == -1 )
                        return;

                    _target.write( buffer, 0, number );
                }
            }
            catch ( IOException e )
            {
                LOG.info( e.toString() );
            }
            finally
            {
                LOG.info( Thread.currentThread().getName() + " leaving." );

                setConnected( false );
            }
        }
    };

    private boolean _isConnected = true;

    /**
     * Sets the connected status and handles property change event sending.
     *
     * @param newValue The value to set.
     */
    private void setConnected( boolean newValue )
    {
        boolean oldValue = _isConnected;
        _isConnected = newValue;
        _pcs.firePropertyChange( "connected", oldValue, newValue );
    }

    /**
     * Check whether this bridge is connected.
     *
     * @return True if this bridge is connected. False otherwise.
     */
    public boolean isConnected()
    {
        return _isConnected;
    }

    /**
     * Release resources.
     */
    public void dispose()
    {
        _isConnected = false;
        _dataPump.interrupt();
    }

    private final PropertyChangeSupport _pcs = new SwingPropertyChangeSupport( this,  true );

    public void addPropertyChangeListener( PropertyChangeListener pcs )
    {
        _pcs.addPropertyChangeListener( pcs );
    }
    public void addPropertyChangeListener( String pname, PropertyChangeListener pcs )
    {
        _pcs.addPropertyChangeListener( pname, pcs );
    }
    public void removePropertyChangeListener( PropertyChangeListener pcs )
    {
        _pcs.removePropertyChangeListener( pcs );
    }
    public void removePropertyChangeListener( String pname, PropertyChangeListener pcs )
    {
        _pcs.removePropertyChangeListener( pname, pcs );
    }
}
