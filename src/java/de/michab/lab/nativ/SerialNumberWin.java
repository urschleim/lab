/* $Id: SerialNumberWin.java 2179 2019-02-25 19:24:56Z Michael $
 *
 * Unpublished work.
 * Copyright © 2016 Michael G. Binz
 */
package de.michab.lab.nativ;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Logger;

import org.jdesktop.smack.util.FileUtils;
import org.jdesktop.util.StringUtil;

/**
 * Create a unique computer id.
 *
 * @version $Rev: 2179 $
 * @author Michael Binz
 */
public class SerialNumberWin implements SystemSerialNumber
{
    private static final Logger LOG =
            Logger.getLogger( SerialNumberWin.class.getName() );

    /**
     * Explicit ctor.
     */
    public SerialNumberWin()
    {
    }

    /**
     * Generate a serial number.  TODO this is currently only implemented
     * for Windows.
     *
     * @return A unique id for our host system.
     */
    @Override
    public final String getSerialNumber()
    {
        return getSerialNumberWin();
    }

    private static String getSerialNumberWin()
    {
        // See http://www.nextofwindows.com/the-best-way-to-uniquely-identify-a-windows-machine
        //
        // Alternatives:
        // wmic csproduct get UUID // Computer identity as a UUID from the motherboard.
        // Above can be FFFFFFFF-FFFF-FFFF...
        // Fallback 1:
        // wmic DISKDRIVE get SerialNumber // Serial number of the disk, BUT NOT UUID
        // Fallback 2:
        // HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Cryptography\MachineGuid
        //

        String result = getBiosSerialNumber();
        if ( StringUtil.hasContent( result ) )
            return result;

        result = getProductSerialNumber();
        if ( StringUtil.hasContent( result ) )
            return result;

        LOG.warning( "Generating fallback id." );
        return UUID.randomUUID().toString();
    }

    private static final String getBiosSerialNumber()
    {
        Process process = null;

        // See http://www.nextofwindows.com/the-best-way-to-uniquely-identify-a-windows-machine
        //
        // Alternatives:
        // wmic csproduct get UUID // Computer identity as a UUID from the motherboard.
        // Above can be FFFFFFFF-FFFF-FFFF...
        // Fallback 1:
        // wmic DISKDRIVE get SerialNumber // Serial number of the disk, BUT NOT UUID
        // Fallback 2:
        // HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Cryptography\MachineGuid
        //
        try {
            process = Runtime.getRuntime().exec(
                    new String[] {
                            "wmic",
                            "bios",
                            "get",
                            "serialnumber" });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try ( Scanner sc = new Scanner( process.getInputStream() ) )
        {
            if ( sc.hasNext( "SerialNumber" ) )
            {
                sc.next();
                if ( sc.hasNext() )
                    return sc.next().trim();
            }
        } finally {
            // See http://grep.codeconsult.ch/2005/02/01/better-cleanup-your-process-objects/
            FileUtils.forceClose( process.getInputStream() );
            FileUtils.forceClose( process.getErrorStream() );
            FileUtils.forceClose( process.getOutputStream() );
        }

        return null;
    }

    private static final String getProductSerialNumber()
    {
        Process process = null;

        // See http://www.nextofwindows.com/the-best-way-to-uniquely-identify-a-windows-machine
        //
        // Alternatives:
        // wmic csproduct get UUID // Computer identity as a UUID from the motherboard.
        // Above can be FFFFFFFF-FFFF-FFFF...
        // Fallback 1:
        // wmic DISKDRIVE get SerialNumber // Serial number of the disk, BUT NOT UUID
        // Fallback 2:
        // HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Cryptography\MachineGuid
        //
        try {
            process = Runtime.getRuntime().exec(
                    new String[] {
                            "wmic",
                            "csproduct",
                            "get",
                            "UUID" });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try ( Scanner sc = new Scanner( process.getInputStream() ) )
        {
            if ( sc.hasNext( "UUID" ) )
            {
                sc.next();
                if ( sc.hasNext() )
                    return sc.next().trim();
            }
        } finally {
            // See http://grep.codeconsult.ch/2005/02/01/better-cleanup-your-process-objects/
            FileUtils.forceClose( process.getInputStream() );
            FileUtils.forceClose( process.getErrorStream() );
            FileUtils.forceClose( process.getOutputStream() );
        }

        return null;
    }
}
