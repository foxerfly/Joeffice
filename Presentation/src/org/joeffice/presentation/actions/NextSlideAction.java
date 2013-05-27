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

import java.awt.Toolkit;
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
 * Shows the next slide (in editor mode).
 * Beep if at the last slide.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "View/Office/Presentation",
        id = "org.joeffice.presentation.actions.NextSlideAction")
@ActionRegistration(
        iconBase = "org/joeffice/presentation/actions/arrow_right.png",
        displayName = "#CTL_NextSlideAction")
@ActionReferences(value = {
    @ActionReference(path = "Office/Presentation/Toolbar", position = 200),
    @ActionReference(path = "Shortcuts", name = "Page_Down")
})
@Messages("CTL_NextSlideAction=Next Slide")
public class NextSlideAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        nextSlide();
    }

    public void nextSlide() {
        SlidesTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SlidesTopComponent.class);
        if (currentTopComponent != null) {
            int currentSlide = currentTopComponent.getSelectedSlide();
            if (currentSlide < currentTopComponent.getPresentation().getSlides().length - 1) {
                currentTopComponent.setSelectedSlide(currentSlide + 1);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
