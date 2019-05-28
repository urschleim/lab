/* $Id: PlayController.java 1992 2018-07-05 18:16:33Z Michael $
 *
 * Audio tests.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */
package application.audio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;

import org.jdesktop.util.InterruptibleThread;





/**
 * Wiring for the UI elements.
 *
 * @version $Rev: 1992 $
 * @author micbinz
 */
class PlayController implements ActionListener
{
    private final JComboBox _in;
    private final JComboBox _out;
    private final JToggleButton _button;

    private Thread _player = null;

    /**
     * Create an instance.
     *
     * @param button The combined play/stop button.
     * @param in The audio source selector.
     * @param out The audio sink selector.
     */
    public PlayController( JToggleButton button, JComboBox in, JComboBox out )
    {
        assert button != null;
        assert in != null;
        assert out != null;

        _button = button;
        _in = in;
        _out = out;

        _button.setText( TXT_Start );

        _button.addActionListener( this );
    }



    @Override
    public void actionPerformed(ActionEvent ae)
    {
        assert ae.getSource() == _button;

        if ( _button.isSelected() )
            doSelect();
        else
            doDeselect();
    }



    /**
     * Start streaming.
     */
    private void doSelect()
    {
        Mixer.Info inSelected =
            (Mixer.Info)_in.getSelectedItem();
        Mixer.Info outSelected =
            (Mixer.Info)_out.getSelectedItem();

        if ( inSelected == null )
            return;
        if ( outSelected == null )
            return;

        try
        {
            Player p = new Player(
                    AudioSystem.getMixer(inSelected),
                    AudioSystem.getMixer(outSelected) );

            _player = new InterruptibleThread( p );
            _player.start();
        }
        catch ( LineUnavailableException e )
        {
            e.printStackTrace();
            return;
        }

        // Switch UI state to playing mode.
        _button.setText( TXT_Stop );
        _in.setEnabled( false );
        _out.setEnabled( false );
	}



    /**
     * Stop streaming.
     */
    private void doDeselect()
    {
        _button.setText( TXT_Start );
        _in.setEnabled( true );
        _out.setEnabled( true );

        if ( _player == null )
            return;

        _player.interrupt();
        _player = null;
    }


    private static final String TXT_Start = "Play...";
    private static final String TXT_Stop = "Stop";
}
