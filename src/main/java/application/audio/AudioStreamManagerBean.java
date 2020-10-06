/* $Id$
 *
 * Audio tests.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */
package application.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;



/**
 * A ready-made component that allows to activate an audio stream for
 * an arbitrary audio source and target combination.
 *
 * @version $Rev: 544 $
 * @author micbinz
 */
@SuppressWarnings("serial")
public class AudioStreamManagerBean
    extends Box
{
    private final DefaultComboBoxModel lmIn =
        new DefaultComboBoxModel();
    private final DefaultComboBoxModel lmOut =
        new DefaultComboBoxModel();

    private final JComboBox inList;
    private final JComboBox outList;

    private final JToggleButton startBut =
        new JToggleButton();

    private final static MixerInfoRenderer MIR =
        new MixerInfoRenderer();

    /**
     * Create an instance.
     */
    public AudioStreamManagerBean()
    {
        super( BoxLayout.LINE_AXIS );

        for ( Mixer.Info c : AudioSystem.getMixerInfo() )
        {
            Mixer mixer = AudioSystem.getMixer( c );

            // If this is an IN line.
            if ( mixer.getSourceLineInfo().length == 0  )
                lmIn.addElement( c );

            if ( mixer.getTargetLineInfo().length == 0  )
                lmOut.addElement( c );
        }

        inList =
            new JComboBox( lmIn );
        inList.setRenderer( MIR );
        outList =
            new JComboBox( lmOut );
        outList.setRenderer( MIR );

        new PlayController( startBut, inList, outList);

        add( inList );
        add( outList );
        add( startBut );
    }
}
