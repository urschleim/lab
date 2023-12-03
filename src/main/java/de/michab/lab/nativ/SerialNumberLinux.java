/*
 * Unpublished work.
 * Copyright © 2018 Michael G. Binz
 */
package de.michab.lab.nativ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

/**
 * Create a unique computer id.
 *
 * @version $Rev: 2179 $
 * @author Michael Binz
 */
public class SerialNumberLinux implements SystemSerialNumber
{
    @SuppressWarnings("unused")
    private static final Logger LOG =
            Logger.getLogger( SerialNumberLinux.class.getName() );

    /**
     * Explicit ctor.
     */
    public SerialNumberLinux()
    {
    }

    /**
     * @return A unique id for our host system.
     */
    @Override
    public final String getSerialNumber()
    {
        try
        {
            return getSerialNumberLinux();
        }
        catch ( Exception e )
        {
            return null;
        }
    }

	/**
     * http://manpages.ubuntu.com/manpages/zesty/man5/machine-id.5.html
     *
     * @return A unique system id for Linux systems.
     * @throws IOException
     */
    private static String getSerialNumberLinux() throws IOException
    {
        // Throws FileNotFoundException.
        byte[] etcMachineId =
                Files.readAllBytes( new File( "/etc/machine-id" ).toPath() );

        return new String(
                etcMachineId ).trim();
    }
}
