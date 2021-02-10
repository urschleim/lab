package de.michab.lab;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A promise to deliver a value.  The common use is that a producer
 * thread performs a {@link #set(Object)}-operation and a consumer
 * thread performs a {@link #get()} operation. In this case the
 * consumer is blocked until the producer sets the value.<p>
 *
 * An instance of this class is reusable, that is two threads
 * can communicate a continuous sequence of values via a
 * {@link Promise}. In this case the producer gets blocked
 * as long as the consumer did not fetch the previous object.
 *
 * @param <T> The value's type.
 * @author micbinz
 */
public class Promise<T>
{
    private volatile T _value;

    private final Semaphore _rLock = new Semaphore(0);
    private final Semaphore _wLock = new Semaphore(1);

    /**
     * Create an instance.
     */
    public Promise()
    {
    }

    /**
     * Get the value.  If the value is not yet set (see {@link #set(Object)}),
     * the operation blocks.
     *
     * @return The value.
     * @throws InterruptedException In case of an interrupt.
     */
    public synchronized T get() throws InterruptedException
    {
        _rLock.acquire();

        try
        {
            return _value;
        }
        finally
        {
            _value = null;
            _wLock.release();
        }
    }

    /**
     * Get the value with a timeout.  If the value is not yet set
     * (see {@link #set(Object)}), the operation blocks until the
     * timeout is reached.
     *
     * @param timeout The maximum time to wait for a value.
     * @param unit The time unit of the timeout argument.
     * @return The value.
     * @throws InterruptedException In case of an interrupt.
     * @throws TimeoutException If no value has been set in the
     * timeout period.
     */
    public synchronized T get( long timeout, TimeUnit unit )
            throws InterruptedException, TimeoutException
    {
        if ( ! _rLock.tryAcquire( timeout, unit ) )
            throw new TimeoutException();

        try
        {
            return _value;
        }
        finally
        {
            _value = null;
            _wLock.release();
        }
    }

    /**
     * Set the value.  This may be called repeatedly.  If the
     * previously set value was not read (see {@link #get()}) then
     * this operation blocks.
     *
     * @param value The value.
     * @throws InterruptedException In case of an interrupt.
     */
    public void set( T value ) throws InterruptedException
    {
        _wLock.acquire();

        try {
            _value = value;
        }
        finally
        {
            _rLock.release();
        }
    }

    /**
     * Set the value with a timeout.  This may be called repeatedly.  If the
     * previously set value was not already read (see {@link #get()}) then
     * this operation blocks until the timeout is reached.
     *
     * @param value The promised value.
     * @param timeout The maximum time to wait.
     * @param unit The time unit of the timeout argument.
     * @throws InterruptedException In case of an interrupt.
     * @throws TimeoutException If the previously set value was not read in
     * time.
     */
    public void set( T value, long timeout, TimeUnit unit ) throws InterruptedException, TimeoutException
    {
        if ( ! _wLock.tryAcquire( timeout, unit ) )
            throw new TimeoutException();

        try {
            _value = value;
        }
        finally
        {
            _rLock.release();
        }
    }

    public static void main( String[] args ) throws Exception
    {
        Promise<Float> promise = new Promise<>();

        new Thread( () -> {
            try
            {
                System.out.println( "Sleep" );
                Thread.sleep( 1000 );
                System.out.println( "Set" );
                promise.set( 3.1415926f );
                promise.set( (float)Math.E );
            }
            catch ( InterruptedException e )
            {
                return;
            }
        }).start();

        System.out.println( "Received: " + promise.get() );
        Thread.sleep( 1000 );
        System.out.println( "Received: " + promise.get() );
    }
}
