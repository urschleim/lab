package de.michab.lab.tools.xslt;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.util.StringUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Michael Binz
 */
public class LargeTextView extends BorderPane
{
    public final StringProperty textProperty =
            new SimpleStringProperty( this, "text", StringUtil.EMPTY_STRING );

    private final ListView<String> _listView =
            new ListView<>();

    private MenuItem _paste =
            new MenuItem( "Paste ..." );
    private ContextMenu _contextMenu =
            new ContextMenu( _paste );

    public LargeTextView()
    {
        this( StringUtil.EMPTY_STRING );
    }
    public LargeTextView( String text )
    {
        // Wiring the listeners.
        _listView.setOnContextMenuRequested(
                (s) -> contextMenuListener() );
        _paste.setOnAction(
                (s) -> pasteContextMenuListener() );
        textProperty.addListener(
                (a,b,c) -> textChangeListener(c) );
        // Wiring the components.
        _listView.contextMenuProperty().set(
                _contextMenu );
        setCenter(
                _listView );
    }

    private void contextMenuListener()
    {
        Clipboard cc =
                Clipboard.getSystemClipboard();

        System.err.println( "hasString: " + cc.hasString() );

        _paste.setDisable(
                ! cc.hasString() );
    }

    private void pasteContextMenuListener()
    {
        Clipboard cc =
                Clipboard.getSystemClipboard();
        if ( ! cc.hasString() )
            return;
        textProperty.set(
                cc.getString() );
    }

    private void textChangeListener( String text )
    {
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
}
