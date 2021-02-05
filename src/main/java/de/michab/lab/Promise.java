package de.michab.lab;

import java.util.concurrent.Semaphore;

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
        try
        {
            _rLock.acquire();
            return _value;
        }
        finally
        {
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
        try {
            _wLock.acquire();
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
