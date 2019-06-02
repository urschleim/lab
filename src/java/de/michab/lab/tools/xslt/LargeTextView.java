package de.michab.lab.tools.xslt;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.util.StringUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

/**
 *
 * @author Michael Binz
 */
public class LargeTextView extends HBox
{
    public final StringProperty textProperty =
            new SimpleStringProperty( new TextArea(), "text", StringUtil.EMPTY_STRING );

    private final ListView<String> _listView =
            new ListView<>();

    public LargeTextView()
    {
        this( StringUtil.EMPTY_STRING );
    }
    public LargeTextView( String text )
    {
        textProperty.addListener(
                (a,b,c) -> textChangeListener(c) );

        getChildren().add( _listView );
    }

    private void textChangeListener( String text )
    {
        textProperty.set( text );

        StringReader sr =
                new StringReader( text );
        BufferedReader br =
                new BufferedReader( sr );

        List<String> list =
                new ArrayList<>();

        while ( true )
        {
            try {
            String line =
                    br.readLine();
            if ( line == null )
                break;

            list.add( line );
            }
            catch ( Exception notPossible )
            {
            }
        }

        _listView.setItems(
                FXCollections.observableArrayList( list ) );
    }

    public void selectLine( int lineNumber )
    {

    }

    /**
     * Access the text property.  This is for compatibility with
     * {@link TextArea}.  Prefer to use {@link #textProperty}
     * directly.
     *
     * @return The text property.
     */
    public final StringProperty textProperty()
    {
        return textProperty;
    }

    /**
     * Enable drag and drop.
     * @param what True enables dnd.
     */
    public void setDndEnabled( boolean what )
    {
    }
}
