package de.michab.lab.tools.xslt;

import java.io.File;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Holds the necessary wiring for the XSL transformation display.
 *
 * @author Michael Binz
 */
public class TransformationComponent extends VBox
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
    public TransformationComponent()
    {
        // cool.
        _textArea.textProperty.bind(
                _text.getTextProperty() );

        new FileDragManager( this, this::onDrop );

        // lame.
        _text.file.addListener(
                (a,b,c) -> listenFileChanged( c ) );

        // Populate.
        getChildren().addAll(
                _filename,
                _textArea );
    }

    private void listenFileChanged( File newFile )
    {
        _filename.setText( newFile.getPath() );
    }
    private void onDrop( File file )
    {
        _text.file.set( file );
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
