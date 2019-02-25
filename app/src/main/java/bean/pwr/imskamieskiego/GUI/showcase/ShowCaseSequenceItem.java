/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI.showcase;

import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener;

import org.jetbrains.annotations.NotNull;

public class ShowCaseSequenceItem implements BubbleShowCaseListener {

    private BubbleShowCaseBuilder builder;
    private BubbleShowCase showCaseItem;

    private SequenceItemListener sequenceItemListener;

    public ShowCaseSequenceItem(BubbleShowCaseBuilder builder) {
        this.builder = builder;
    }

    public BubbleShowCase show() {
        builder.listener(this);
        showCaseItem = builder.show();
        return showCaseItem;
    }

    public void dismiss(){
        if (showCaseItem != null){
            showCaseItem.dismiss();
            showCaseItem = null;
        }
        if (sequenceItemListener != null){
            sequenceItemListener.onDismiss();
        }
    }

    public ShowCaseSequenceItem setSequenceItemListener(SequenceItemListener sequenceItemListener) {
        this.sequenceItemListener = sequenceItemListener;
        return this;
    }

    public void close(){
        if (showCaseItem != null){
            showCaseItem.dismiss();
            showCaseItem = null;
        }
        if (sequenceItemListener != null){
            sequenceItemListener.onClose();
        }
    }

    public BubbleShowCaseBuilder getBuilder() {
        return builder;
    }

    @Override
    public void onBackgroundDimClick(@NotNull BubbleShowCase bubbleShowCase) {

    }

    @Override
    public void onBubbleClick(@NotNull BubbleShowCase bubbleShowCase) {
        dismiss();
    }

    @Override
    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
        close();
    }

    @Override
    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {

    }
}
