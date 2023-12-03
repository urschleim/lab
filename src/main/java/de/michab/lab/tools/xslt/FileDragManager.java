/*
 * Unpublished work.
 * Copyright © 2019 Michael G. Binz
 */
package de.michab.lab.tools.xslt;

import java.io.File;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Adds the necessary callbacks for DnD to a text area.
 * If the DragManager is applied, the text area accepts
 * file drag and drop, as well as text drag and drop.
 *
 * @author Michael Binz
 */
class FileDragManager
{
    private static final Logger LOG =
            Logger.getLogger( FileDragManager.class.getName() );

    private final Node _host;

    private final Consumer<File> _onDrop;

    /**
     *
     * @param host For now we accept only a text area.
     */
    public FileDragManager( Node host, Consumer<File> onDrop )
    {
        _onDrop =
                onDrop;
        _host =
                host;
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
    }

    private void dragDropped( DragEvent de )
    {
        Dragboard d = de.getDragboard();

        if ( d.hasFiles() && d.getFiles().size() == 1 )
            _onDrop.accept( d.getFiles().get( 0 ) );
    }

    public void dispose()
    {
        _host.setOnDragOver(
                null );
        _host.setOnDragDropped(
                null );
    }
}
