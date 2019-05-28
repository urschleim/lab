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
 * $Id: PreviousAction.java 1018 2015-08-03 19:04:22Z Michael $
 */

package org.jdesktop.swingx.wizard;

import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

/**
 * Created by IntelliJ IDEA.
 * @author andrewp
 */
class
PreviousAction
extends WizardAction
{
    protected PreviousAction(Wizard model)
    {
        super("previous", model, new ArrowIcon(SwingConstants.WEST));
    }

    @Override
    public void
    doAction(ActionEvent e)
    {
        getModel().previousStep();
    }

    @Override
    protected void
    updateState()
    {
        WizardStep activeStep = getActiveStep();
        boolean busy = activeStep != null && activeStep.isBusy();
        setEnabled(getModel().isPreviousAvailable() && !busy);
    }

    /**
     * Generated for PreviousAction.java.
     */
    private static final long serialVersionUID = -4243012872441917011L;
}
