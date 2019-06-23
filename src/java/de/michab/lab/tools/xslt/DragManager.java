/* $Id$
 *
 * Unpublished work.
 * Copyright © 2019 Michael G. Binz
 */
package de.michab.lab.tools.xslt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.util.StringUtil;

import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Adds the necessary callbacks for DnD to a text area.
 * If the DragManager is applied, the text area accepts
 * file drag and drop, as well as text drag and drop.
 *
 * @author Michael Binz
 * @deprecated Use FileDragManager
 */
@Deprecated
public class DragManager
{
    private static final Logger LOG =
            Logger.getLogger( DragManager.class.getName() );

    private final TextArea _host;

    public static void attachTo( TextArea textArea )
    {
        new DragManager( textArea );
    }
    public static void detachFrom( TextArea textArea )
    {
        textArea.setOnDragOver(
                null );
        textArea.setOnDragDropped(
                null );
    }

    /**
     *
     * @param host For now we accept only a text area.
     */
    private DragManager( TextArea host )
    {
        _host = host;

        _host.setOnDragOver(
                this::dragOver );
        _host.setOnDragDropped(
                this::dragDropped );
    }

    private void dragOver( DragEvent de )
    {
        Dragboard d = de.getDragboard();

        if ( d.hasFiles() && d.getFiles().size() == 1 )
            de.acceptTransferModes( TransferMode.COPY );
        else if ( d.hasString() )
            de.acceptTransferModes( TransferMode.COPY );
    }

    private void dragDropped( DragEvent de )
    {
        Dragboard d = de.getDragboard();

        String newText;

        if ( d.hasFiles() && d.getFiles().size() == 1 )
            newText = readAllBytes( d.getFiles().get( 0 ).toPath() );
        else if ( d.hasString() )
            newText = d.getString();
        else
            newText = StringUtil.EMPTY_STRING;

       _host.setText( newText );
    }

    /**
     * Exception free file reading.
     * @param path The file to read.
     * @return File contents.
     */
    private String readAllBytes( Path path )
    {
        try
        {
            return new String(
                    Files.readAllBytes( path )  );
        }
        catch ( Exception e )
        {
            LOG.log(
                    Level.WARNING,
                    "Unexpected, could not read file: " + path,
                    e );
            return StringUtil.EMPTY_STRING;

        }
    }
}
