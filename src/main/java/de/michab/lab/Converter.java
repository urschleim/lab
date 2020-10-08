package de.michab.lab;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jdesktop.util.ReflectionUtil;
import org.smack.util.StringUtil;
import org.smack.util.collections.MultiMap;

public class Converter
{
    private final MultiMap<Class<?>, Class<?>, Function<?, ?>> _converters =
            new MultiMap<>();

    Converter()
    {
        add( String.class, Integer.class, Integer::decode );
    }

    public <F,T> Converter add( Class<F> f, Class<T> t, Function<F, T> c )
    {
        f = (Class<F>)
                ReflectionUtil.normalizePrimitives( f );
        t = (Class<T>)
                ReflectionUtil.normalizePrimitives( t );
        _converters.put(
                f,
                t,
                c );

        return this;
    }

    private <F,T> T convertImpl( Class<T> t, F f ) throws Exception
    {
        // Null is always convertible.
        if ( f == null )
            return null;

        t = (Class<T>)
                ReflectionUtil.normalizePrimitives( t );
        var fClass =
                ReflectionUtil.normalizePrimitives( f.getClass() );

        // If the target is assignment compatible just return the
        // value without conversion.
        if ( t.isAssignableFrom( fClass ) )
            return (T)f;

        // Check if a converter exists.
        var cvt = (Function<F,T>) _converters.get( fClass, t );

        if ( cvt != null )
            return cvt.apply( f );

        if ( t == String.class )
            return (T)f.toString();

        if ( fClass == String.class && t.isEnum() )
            return (T)transformEnum( t, (String)f );

        var c = ReflectionUtil.getConstructor( t, fClass );
        if ( c != null )
            return c.newInstance( f );

        throw new Exception("No converter.");
    }

    public <F,T> T convert( Class<T> t, F f )
    {
        try {
            return convertImpl( t, f );
        }
        catch (Exception e)
        {
            String message = String.format( "Cannot convert '%s' to %s.", f, t.getSimpleName()  );
            throw new IllegalArgumentException(
                    message,
                    e );
        }
    }

    private final Object transformEnum(
            Class<?> targetEnum,
            String argument )
        throws IllegalArgumentException
    {
        if ( targetEnum == null )
            throw new NullPointerException();
        if ( argument == null )
            throw new NullPointerException();
        if ( ! targetEnum.isEnum() )
            throw new AssertionError();

        // Handle enums.
        for ( Object c : targetEnum.getEnumConstants() )
        {
            if ( c.toString().equalsIgnoreCase( argument ) )
                return c;
        }

        // Above went wrong, generate a good message.
        List<String> allowed = new ArrayList<>();
        for ( Object c : targetEnum.getEnumConstants() )
            allowed.add( c.toString() );

        String message = String.format(
                "Unknown enum value: '%s'.  Allowed values are %s.",
                argument,
                StringUtil.concatenate( ", ", allowed ) );

        throw new IllegalArgumentException( message );
    }

    public static void main( String[] args )
    {
        Converter c = new Converter();

        // Null conversion.
        {
        JFrame swing = c.convert( JFrame.class, null );
        System.out.println( "JFrame=" + swing );
        }

        // Integer conversion primitive.
        {
        int i = c.convert( Integer.TYPE, "313" );
        System.out.println( "i=" + i );
        }
        // Integer conversion object.
        {
        int i = c.convert( Integer.class, "314" );
        System.out.println( "i=" + i );
        }
        // Enum conversion.
        {
        DayOfWeek e = c.convert( DayOfWeek.class, "WEDNESDAY" );
        System.out.println( "DayOfWeek=" + e );
        }
        // Assignable conversion.
        {
        Number e = c.convert( Number.class, Short.valueOf( (short)315 ) );
        System.out.println( "assignable=" + e );
        }
        // Constructable conversion.
        {
        JLabel e = c.convert( JLabel.class, "Label" );
        System.out.println( "assignable=" + e );
        }
    }
}
