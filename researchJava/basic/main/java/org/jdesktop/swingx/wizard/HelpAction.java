/**
 * Wizard Framework
 * Copyright 2004 - 2005 Andrew Pietsch
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * $Id$
 */

package org.jdesktop.swingx.wizard;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 */
class HelpAction
extends AbstractAction
{
    private Wizard wizard;

    protected HelpAction(Wizard wizard)
    {
        super(I18n.getString("help.text"));
        this.wizard = wizard;
        putValue(Action.MNEMONIC_KEY, new Integer(I18n.getMnemonic("help.mnemonic")));
        wizard.addPropertyChangeListener("helpBroker", new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                configureState();
            }
        });

        configureState();
    }

    @Override
    public void
    actionPerformed(ActionEvent e)
    {
        HelpBroker helpBroker = wizard.getHelpBroker();

        if (helpBroker != null)
            helpBroker.activateHelp(wizard, wizard.getModel());
    }

    private void
    configureState()
    {
        setEnabled(wizard.getHelpBroker() != null);
    }

    /**
     * Generated for HelpAction.java.
     */
    private static final long serialVersionUID = 7741301570259449606L;
}
