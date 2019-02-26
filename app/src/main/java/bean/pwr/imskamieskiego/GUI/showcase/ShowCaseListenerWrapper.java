/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI.showcase;

import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener;
import org.jetbrains.annotations.NotNull;

/**
 * Implement basic behavior for BubbleShowCase.
 */
public class ShowCaseListenerWrapper implements BubbleShowCaseListener {

    @Override
    public void onBackgroundDimClick(@NotNull BubbleShowCase bubbleShowCase) {

    }

    @Override
    public void onBubbleClick(@NotNull BubbleShowCase bubbleShowCase) {
        bubbleShowCase.dismiss();
    }

    @Override
    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
        bubbleShowCase.finishSequence();
    }

    @Override
    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {

    }
}
