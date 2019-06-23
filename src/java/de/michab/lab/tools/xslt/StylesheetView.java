/* $Id$
 *
 * Unpublished work.
 * Copyright © 2019 Michael G. Binz
 */
package de.michab.lab.tools.xslt;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Holds the necessary wiring for the XSL transformation display.
 *
 * @author Michael Binz
 */
public class StylesheetView extends BorderPane
{
    private TextFileLink _text =
            new TextFileLink();

    private Label _filename =
            new Label();

    private FxLineNumberTextArea _textArea =
            new FxLineNumberTextArea();

    /**
     *
     */
    public StylesheetView()
    {
        // cool.
        _textArea.textProperty.bind(
                _text.getTextProperty() );
        _filename.textProperty().bind(
                _text.getFilenameProperty() );
        new FileDragManager( this, this::onDrop );

        // Populate.
        setTop(
                _filename );
        setCenter(
                _textArea );
    }

    private void onDrop( File file )
    {
        _text.file.set( file );
    }

    public ObjectProperty<File> fileProperty ()
    {
        return _text.file;
    }
    public void selectLine( int lineNumber )
    {
        _textArea.selectLine( lineNumber );
    }

    public String getText()
    {
        return _text.getTextProperty().get();
    }
}
