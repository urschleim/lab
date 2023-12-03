/*
 * Experimental.
 *
 * Released under Gnu Public License
 * Copyright © 2010,2011 Michael G. Binz
 */
package de.michab.lab;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-save map with an attached entry factory.
 *
 * @author Michael Binz
 */
public class Cache<K, V>
{
    /**
     * The Cache's factory interface.  The create operation is allowed to
     * create null values.
     */
    public static interface ValueFactory<K,V>
    {
        V create( K pKey );
    }



    /**
     * The actual cache.
     */
    private final ConcurrentHashMap<K, V> _cache =
        new ConcurrentHashMap<K, V>();



    /**
     * The map of locks.  Each entry represents a currently
     * active load operation for the passed key.
     */
    private final Map<K,Lock> _keyLocks =
        new HashMap<K, Lock>();



    /**
     * The factory passed in construction.
     */
    private final ValueFactory<K, V> _factory;



    /**
     * Create a cache using the passed factory.
     *
     * @param pFactory The value factory used on no cache hit.
     */
    public Cache( ValueFactory<K, V> factory )
    {
        _factory = factory;
    }



    /**
     * Access a value in the cache.  If it does not exist, it is created
     * via the factory. Multithread save and non blocking in case the object
     * is already in the cache.
     *
     * @param key The value's key.
     * @return The corresponding value. Whether this operation can return
     * {@code null} depends on the factory.  If the factory maps certain
     * keys to {@code null}, then this operation can return {@code null}.
     */
    public V get( K key )
    {
        if ( _cache.containsKey( key ) )
            return _cache.get( key );

        return atomicUpdate( key );
    }



    /**
     * The single common lock for this cache.
     */
    private final ReentrantLock _passLock = new ReentrantLock();



    /**
     * Called in case the key was not found in this cache.  Performs the
     * actual cache update.
     *
     * @param key The cache key.
     * @return The corresponding value.
     */
    private V atomicUpdate( K key )
    {
        // Get the common lock ensuring that only a single thread continues.
        _passLock.lock();
        try
        {
            // Atomically check that the key has not been added in the
            // meantime and return if the key HAS been added.
            if ( _cache.containsKey( key ) )
                return _cache.get( key );

            // Try to find the lock for the current key.
            Lock keyLock = _keyLocks.get( key );

            try
            {
                if ( keyLock == null )
                {
                    // If there was no lock, then this means we are the first
                    // thread asking for this key. So we create a lock...
                    keyLock = new ReentrantLock();
                    // ...lock it...
                    keyLock.lock();
                    // ...and make it visible for the threads that follow.
                    _keyLocks.put( key, keyLock );
                    // Now release the common lock.  This allows the other
                    // threads waiting on the common lock to continue.
                    // More formally the lock propagated from the common lock to
                    // the key specific lock.
                    _passLock.unlock();
                    // Perform the actual cache update with a little help of
                    // the factory.
                    _cache.put( key, _factory.create( key ) );
                    // The cache is updated, we remove the key lock.
                    _keyLocks.remove( key );
                }
                else
                {
                    // There is already a key lock, so some other thread is
                    // currently updating cache content.  We release the
                    // common lock, allowing the follow-up treads to continue.
                    _passLock.unlock();
                    System.err.println( Thread.currentThread().getName() + ": Wait : " + key );
                    // We acquire the key lock (and expect here to wait in the
                    // most common case.
                    keyLock.lock();
                    System.err.println( Thread.currentThread().getName() + ": Cont : " + key );
                }

                // Reaching this point means that the cache content is updated.
                // Return it.
                return _cache.get( key );
            }
            finally
            {
                // Unlock the next thread waiting on this key lock.
                keyLock.unlock();
            }
        }
        finally
        {
            // Ensure that the common lock is released properly.  This is
            // normally only needed if the requested object was found in the
            // cache in the very first safety check above.  All other code
            // paths ensure that the lock is already released here.
            if ( _passLock.isHeldByCurrentThread() )
                _passLock.unlock();
        }
    }

    /**
     * The cache map for implementation two.
     */
    private ConcurrentMap<K, FutureTask<V>> _map =
            new ConcurrentHashMap<K, FutureTask<V>>();

    /**
     * An executor that uses the calling thread. If an element is
     * not in the cache, then the first calling thread is responsible
     * for creating the entry.
     */
    private Executor executor = new Executor()
    {
        @Override
        public void execute( Runnable command )
        {
            command.run();
        }
    };

    /**
     * Access a cache entry.  This is implementation two using Java
     * concurrency.
     *
     * @param key The key to look up.
     * @return The cache entry.
     * @throws InterruptedException If the thread was interrupted.
     * @throws ExecutionException If an error occurred creating an
     * cache entry.
     */
    public V get2( final K key )
        throws InterruptedException, ExecutionException
    {
        FutureTask<V> f = _map.get( key );

        if ( f == null )
        {
            Callable<V> c = new Callable<V>()
            {
                @Override
                public V call()
                {
                    return _factory.create( key );
                }
            };
            f = new FutureTask<V>( c );
            FutureTask<V> old = _map.putIfAbsent( key, f );
            if ( old == null )
                executor.execute( f );
            else
                f = old;
        }
        return f.get();
    }
}
