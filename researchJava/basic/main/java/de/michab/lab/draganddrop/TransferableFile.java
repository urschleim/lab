package de.michab.lab.draganddrop;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.List;

/**
 * From http://http://www.straub.as/autor.html
 *
 * @version $Rev: 1178 $
 * @author Michael Binz
 */
public class TransferableFile implements Transferable
{
    private List fileList ;

    public TransferableFile(List files)
    {
        fileList = files;
    }

    // Returns an object which represents the data to be transferred.
    @Override
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException
    {
        if( flavor.equals(DataFlavor.javaFileListFlavor) )
            return fileList ;

        throw new UnsupportedFlavorException(flavor);
    }

    // Returns an array of DataFlavor objects indicating the flavors
    // the data can be provided in.
    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[] {DataFlavor.javaFileListFlavor} ;
    }

    // Returns whether or not the specified data flavor is supported for this object.
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return flavor.equals(DataFlavor.javaFileListFlavor) ;
    }
}
