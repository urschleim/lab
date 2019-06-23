/* $Id$
 *
 * Unpublished work.
 * Copyright © 2019 Michael G. Binz
 */
package de.michab.lab.tools.xslt;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.jdesktop.application.CliApplication;
import org.jdesktop.util.InterruptibleThread;
import org.jdesktop.util.StringUtil;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * A component that links a file's text content dynamically to a property.
 * If the file content changes, the property gets updated.
 *
 * @author Michael G. Binz
 */
public class TextFileLink extends CliApplication
{
    public final SimpleObjectProperty<File> file =
            new SimpleObjectProperty<>(
                    this,
                    "file",
                    null );

    private final SimpleStringProperty textProperty =
            new SimpleStringProperty(
                    this,
                    "textProperty",
                    StringUtil.EMPTY_STRING );

    /**
     * Create an instance.
     */
    public TextFileLink()
    {
        file.addListener(
                (a,b,c) -> startListen( c ) );
    }

    /**
     * @return The link's text property.
     */
    public ReadOnlyStringProperty getTextProperty()
    {
        return textProperty;
    }

    /**
     * Initializes listening on the passed file.
     *
     * @param newFile The file to listen on.
     * @throws IOException Thrown in case of an error.
     */
    private void startListenImpl( File newFile )
            throws IOException
    {
        WatchService watchService =
                FileSystems.getDefault().newWatchService();

        Path path =
                newFile.toPath();
        Path dir =
                path.getParent();

        dir.register(
                watchService,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE );

        if ( _thread != null )
            _thread.interrupt();

        _thread = new InterruptibleThread(
                () -> doListen( path.getFileName(), watchService ),
                getClass().getName(),
                true );
        _thread.start();
    }

    /**
     * A thread that is responsible for listening to file changes.
     */
    private InterruptibleThread _thread;

    /**
     * Listens to file changes, handles exceptions.
     * @param newFile The file to listen on.
     */
    private void startListen( File newFile )
    {
        if ( ! newFile.isAbsolute() )
            newFile = newFile.getAbsoluteFile();

        targetFileChanged(
                newFile,
                StandardWatchEventKinds.ENTRY_CREATE );

        try
        {
            startListenImpl( newFile );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Implements the thread activity on detecting file changes.
     *
     * @param path The file name to listen on.
     * @param watchService A watch service.
     */
    private void doListen(
            Path path,
            WatchService watchService )
    {
        try
        {
            while ( ! Thread.interrupted() )
            {
                WatchKey key = watchService.take();
                for ( WatchEvent<?> c : key.pollEvents() )
                {
                    // See context() documentation.  Cast is safe.
                    Path context =
                            (Path)c.context();
                    if ( ! path.equals( context ) )
                        break;

                    targetFileChanged( file.get(), c.kind() );

                }
                key.reset();
            }
        }
        catch ( InterruptedException e )
        {
            return;
        }
    }

    /**
     * Handles file changes.
     *
     * @param file The file that changed, this is the property value.
     * @param kind The kind of change.
     */
    private void targetFileChanged(  File file, Kind<?> kind )
    {
        System.out.println(
                "Event kind:" + kind
                + ". File affected: " + file + ".");

        if ( kind == StandardWatchEventKinds.ENTRY_DELETE )
        {
            textProperty.set( StringUtil.EMPTY_STRING );
            return;
        }

        try
        {
            byte[] content = Files.readAllBytes( file.toPath() );
            textProperty.set( new String( content ) );
        }
        catch ( Exception e )
        {
            textProperty.set( StringUtil.EMPTY_STRING );
        }

    }

    /**
     * Test operation.
     * @param f A file to listen
     * @throws Exception
     */
    @Command
    protected final void test( File f ) throws Exception
    {
        file.set( f );

        System.in.read();
    }

    /**
     * Startup.
     *
     * @param argv Command line arguments.
     */
    public static void main( String[] argv )
    {
        launch( TextFileLink.class, argv );
    }
}
