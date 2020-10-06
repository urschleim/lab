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

import java.util.EventObject;

/**
 * This event is fired when ever a {@link Wizard} is closed or cancelled.
 */
public class
WizardEvent
extends EventObject
{
    /**
     * Constructs a new event for the specified {@link Wizard}.
     * @param source
     */
    public WizardEvent(Wizard source)
    {
        super(source);
    }

    /**
     * Returns the {@link Wizard} that fired the event.
     * @return the wizard that fired the event.
     */
    public Wizard
    getWizard()
    {
        return (Wizard) getSource();
    }

    /**
     * Generated for WizardEvent.java.
     */
    private static final long serialVersionUID = 9024495826182446459L;
}
