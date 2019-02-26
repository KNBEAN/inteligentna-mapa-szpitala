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

/**
 * This class is a wrapper for BubbleShowCase. This wrapper with ShowCaseSequence class are hack for
 * BubbleShowCase sequence limitations like for ex. inability to finish sequence, when back button
 * is pressed.
 */
public class ShowCaseSequenceItem implements BubbleShowCaseListener {

    private BubbleShowCaseBuilder builder;
    private BubbleShowCase showCaseItem;

    private SequenceItemListener sequenceItemListener;

    /**
     * Create new wrapper object. If a listener has been set to the builder object, it will be
     * overwritten via wrapper. If custom listener is required, have to overwrite listener methods
     * implemented in this class.
     *
     * @param builder object of BubbleShowCaseBuilder
     */
    public ShowCaseSequenceItem(BubbleShowCaseBuilder builder) {
        this.builder = builder;
    }

    /**
     * Show showcase item
     * @return BubbleShowCase item
     */
    BubbleShowCase show() {
        builder.listener(this);
        showCaseItem = builder.show();
        return showCaseItem;
    }

    void dismiss() {
        if (showCaseItem != null) {
            showCaseItem.dismiss();
            showCaseItem = null;
        }
        if (sequenceItemListener != null) {
            sequenceItemListener.onDismiss();
        }
    }

    ShowCaseSequenceItem setSequenceItemListener(SequenceItemListener sequenceItemListener) {
        this.sequenceItemListener = sequenceItemListener;
        return this;
    }

    public void close() {
        if (showCaseItem != null) {
            showCaseItem.dismiss();
            showCaseItem = null;
        }
        if (sequenceItemListener != null) {
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
        dismiss();
    }
}
