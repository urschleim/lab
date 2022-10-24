/* $Id$
 *
 * Audio tests.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */
package application.audio;

import java.awt.Component;

import javax.sound.sampled.Mixer;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


/**
 * Render a Mixer.Info instance.
 *
 * @version $Rev: 544 $
 * @author micbinz
 */
@SuppressWarnings("serial")
class MixerInfoRenderer extends DefaultListCellRenderer
{
    /**
     *
     */
    public MixerInfoRenderer()
    {
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus )
    {
        Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

        // This condition is valid for DefaultListCellRenderers.
        assert c == this;

        Mixer.Info mi = (Mixer.Info)value;
        setText( mi.getName() );

        return c;
    }
}
