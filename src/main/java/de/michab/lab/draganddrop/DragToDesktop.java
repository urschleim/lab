package de.michab.lab.draganddrop;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * From http://www.straub.as/java/clip-dnd/dnd.html
 *
 * Guru level.
 *
 * @version $Rev: 1178 $
 * @author Michael Binz
 */
public class DragToDesktop extends JFrame
{
    private Container conPane ;
    private JList dragFileLister;
    private JScrollPane sp ;
    private String sourceDir = "c:/cygwin64/tmp";

    public DragToDesktop()
    {
        conPane = getContentPane();
        initCenter();
        initFrame();
    }

    // ----------------------------- initCenter ----------------------------- \\
    private void initCenter()
    {
        File source = new File(sourceDir) ;
        File[] files = source.listFiles() ;
        Vector fileVector = new Vector();
        for(int i=0; i<files.length; i++)
        {
            if ( files[i].isDirectory() )
                fileVector.add( "<dir>  " + files[i].getName());
            else
                fileVector.add("<file> "+ files[i].getName());
        }
        dragFileLister = new JList(fileVector);
        dragFileLister.setDragEnabled(true);
        dragFileLister.setTransferHandler( new ListTransferHandler(sourceDir) );
        sp = new JScrollPane(dragFileLister);
        conPane.add(sp, BorderLayout.CENTER);
    }

    // ------------------------------ initFrame ------------------------------ \\
    private void initFrame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        setTitle(" DragDemo   listing of " + sourceDir);
        setSize(300,400);
        setLocation(47,47);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        DragToDesktop wnd = new DragToDesktop();
    }
}
