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

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
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

    public final SimpleStringProperty textProperty =
            new SimpleStringProperty(
                    this,
                    "textProperty",
                    StringUtil.EMPTY_STRING );

    public TextFileLink()
    {
        file.addListener(
                (a,b,c) -> fileListener( c ) );
    }

    private void fileListenerException( File newFile )
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

    private InterruptibleThread _thread;

    private void fileListener( File newFile )
    {
        if ( ! newFile.isAbsolute() )
            newFile = newFile.getAbsoluteFile();

        try
        {
            fileListenerException( newFile );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

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

    @Command
    protected final void test( File f ) throws Exception
    {
        file.set( f );

        System.in.read();
    }

    /**
     *
     * @param argv
     */
    public static void main( String[] argv )
    {
        launch( TextFileLink.class, argv );
    }
}
