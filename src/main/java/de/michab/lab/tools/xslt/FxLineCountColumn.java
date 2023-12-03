/*
 * Unpublished work.
 * Copyright © 2019 Michael G. Binz
 */
package de.michab.lab.tools.xslt;

import java.net.URL;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

/**
 * A single column component only displaying line numbers.
 *
 * @version $Id: a6b3d60070fa628b95472594db76ce028997e7fb $
 * @author Michael
 */
class FxLineCountColumn extends TextArea
{
    /**
     * Keep a reference to prevent gc.
     */
    private IntegerBinding _sizeProperty;

    private SimpleIntegerProperty _masterLineCount =
            new SimpleIntegerProperty();

    public final SimpleObjectProperty<TextArea> _master =
            new SimpleObjectProperty<>( this, "master" );

    public FxLineCountColumn()
    {
        setId( "fx-line-count-column" );
        _master.addListener( this::masterChanged );

        setEditable( false );
        setMinWidth( USE_PREF_SIZE );
        setMaxWidth( USE_PREF_SIZE );
    }

    private void masterChanged( ObservableValue<? extends TextArea> observable,
            TextArea oldValue, TextArea newValue )
    {
        _sizeProperty = Bindings.size(
                newValue.getParagraphs() );
        _masterLineCount.bind(
                _sizeProperty );
        setText(
                generateContent( _masterLineCount.get() ) );
        _sizeProperty.addListener( (a,b,v) ->
           setText( generateContent( v.intValue() ) ) );

        // Two-way scroll setup.
        newValue.scrollTopProperty().addListener( (a,b,c) ->
            setScrollTop( c.doubleValue() ) );
        scrollTopProperty().addListener(  (a,b,c) ->
            newValue.setScrollTop( c.doubleValue() ) );

        setPrefColumnCount( ("" + _masterLineCount.get()).length() +1 );
    }

    private static String generateContent( int linecount )
    {
        StringBuilder result =
                new StringBuilder();

        for ( int i = 1 ; i <= linecount ; i++ )
        {
            result.append( i + "\n" );
        }

        return result.toString();
    }

    @Override
    public String getUserAgentStylesheet() {
        String resourceName =
                String.format( "resources/%s.css",
                        getClass().getSimpleName() );
        URL is =
                this.getClass().getResource(
                        resourceName );

        return is.toString();
    }
}
