/*
 * Copyright 2013 Japplis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joeffice.presentation.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import org.joeffice.desktop.ui.OfficeTopComponent;
import org.joeffice.presentation.SlidesTopComponent;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Go to the last slide (in editor mode).
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "View/Office/Presentation",
        id = "org.joeffice.presentation.actions.LastSlideAction")
@ActionRegistration(
        displayName = "#CTL_LastSlideAction")
@ActionReferences({
    @ActionReference(path = "Shortcuts", name = "End")
})
@Messages("CTL_LastSlideAction=Last Slide")
public class LastSlideAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        lastSlide();
    }

    public void lastSlide() {
        SlidesTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SlidesTopComponent.class);
        if (currentTopComponent != null) {
            int lastSlide = currentTopComponent.getPresentation().getSlides().length - 1;
            currentTopComponent.setSelectedSlide(lastSlide);
        }
    }
}
