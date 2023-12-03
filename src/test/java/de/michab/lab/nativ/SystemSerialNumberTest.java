package de.michab.lab.nativ;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jdesktop.util.PlatformType;
import org.junit.jupiter.api.Test;
import org.smack.util.StringUtil;

public class SystemSerialNumberTest
{
    @Test
    public void SystemSerialNumberTestTest() throws Exception
    {
        var ssn =
                PlatformType.load( SystemSerialNumber.class );
        assertNotNull( ssn );

        var sn = ssn.getSerialNumber();
        assertTrue( StringUtil.hasContent( sn ) );
    }
}
