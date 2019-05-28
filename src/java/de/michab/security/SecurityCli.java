package de.michab.security;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.smack.util.ResourceUtils;
import org.jdesktop.util.JavaUtil;
import org.smack.util.SecurityUtil;

import de.michab.lab.ConsoleApplication;

public class SecurityCli extends ConsoleApplication
{
    private static final Logger LOG =
            Logger.getLogger( SecurityCli.class.getName() );

    private final static String PASSWORD = "87c96f0d";
    private final static String ALIAS = "mmt";
    private final static String KEYSTORE = "resources/mmt.pfx";

    public SecurityCli()
    {

    }

    private static final PrivateKey loadPrivate() throws Exception
    {
        // https://docs.oracle.com/javase/tutorial/security/apisign/enhancements.html
        String spass = PASSWORD;
        String kpass = PASSWORD;

        KeyStore ks =
                KeyStore.getInstance( "pkcs12" );

        try ( InputStream ksfis = ResourceUtils.getResourceAsStream(
                SecurityCli.class,
                "mmt.pfx" ) )
        {
            BufferedInputStream ksbufin =
                    new BufferedInputStream( ksfis );

            ks.load( ksbufin, spass.toCharArray() );
        }

        return (PrivateKey)ks.getKey(
                ALIAS,
                kpass.toCharArray() );
    }

    private static final PublicKey loadPublic()
    {
        InputStream is = ResourceUtils.getResourceAsStream(
                SecurityCli.class,
                "mmt.cert" );
        try
        {
            return SecurityUtil.readCert( is ).getPublicKey();
        }
        catch ( Exception e )
        {
            LOG.log( Level.SEVERE, "CustosInit", e );
            throw new AssertionError( null, e );
        }
        finally
        {
            JavaUtil.force( is::close );
        }
    }

    @Command
    protected void tst() throws Exception {
        out( "Private\n" );
        out( "%s\n", loadPrivate() );
        out( "Public\n" );
        out( "%s\n", loadPublic() );
    }

    public static void main( String[] argv )
    {
        launch( SecurityCli.class, argv );
    }
}
