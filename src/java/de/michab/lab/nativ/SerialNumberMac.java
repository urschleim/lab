/* $Id: SerialNumberMac.java 2179 2019-02-25 19:24:56Z Michael $
 *
 * Unpublished work.
 * Copyright © 2018 Michael G. Binz
 */
package de.michab.lab.nativ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jdesktop.util.JavaUtil;
import org.jdesktop.util.StringUtil;

/**
 * Detect the system serial number on a Mac.
 *
 * @version $Id: SerialNumberMac.java 2179 2019-02-25 19:24:56Z Michael $
 * @author Michael Binz
 */
public class SerialNumberMac implements SystemSerialNumber
{
    /**
     * Catch ctor.
     */
    public SerialNumberMac()
    {
    }

    @Override
    public String getSerialNumber()
    {
        Process process = null;

        try {
            process = Runtime.getRuntime().exec(
                    new String[] {
                            "ioreg",
                            "-rd1",
                            "-c",
                            "IOPlatformExpertDevice"
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try ( BufferedReader br = new BufferedReader(
                new InputStreamReader(process.getInputStream())))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if ( line.contains("IOPlatformUUID")) {
                    return StringUtil.splitQuoted(line)[2];
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // See http://grep.codeconsult.ch/2005/02/01/better-cleanup-your-process-objects/
            JavaUtil.force( process.getInputStream()::close );
            JavaUtil.force( process.getErrorStream()::close );
            JavaUtil.force( process.getOutputStream()::close );
        }

        return null;
    }
}
