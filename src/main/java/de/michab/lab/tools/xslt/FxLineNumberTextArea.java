/*
 * Unpublished work.
 * Copyright © 2019 Michael G. Binz
 */
package de.michab.lab.tools.xslt;

import java.util.List;

import org.smack.util.StringUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 *
 * @version $Rev: 2152 $
 * @author Michael
 */
public class FxLineNumberTextArea extends HBox
{
    private TextArea _master;

    public final ObjectProperty<Font> fontProperty;

    public final StringProperty textProperty;

    public FxLineNumberTextArea( String text )
    {
        _master =
                new TextArea( text );
        // Use available space. Can this be done better?
        _master.setPrefColumnCount(
                1000 );

       textProperty =
               _master.textProperty();
       fontProperty =
               _master.fontProperty();

       FxLineCountColumn lcc =
               new FxLineCountColumn();
       lcc._master.set(
               _master );
       getChildren().addAll(
               lcc,
               _master );

       lcc.fontProperty().bind(
               fontProperty );
    }

    public FxLineNumberTextArea()
    {
        this( StringUtil.EMPTY_STRING );
    }

    /**
     * @param lineNumber The line number to select.
     */
    public void selectLine( int lineNumber )
    {
        List<CharSequence> paragraphs =
                _master.getParagraphs();

        int lineIndex = lineNumber - 1;
        if ( lineIndex < 0 || lineNumber > paragraphs.size() )
        {
            _master.deselect();
            return;
        }

        int position = 0;

        for ( int i = 0 ; i < lineIndex ; i++ )
        {
            CharSequence c = paragraphs.get( i );
            position += c.length() + 1;
        }

        _master.selectRange(
                position,
                position + paragraphs.get( lineIndex ).length() );
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
