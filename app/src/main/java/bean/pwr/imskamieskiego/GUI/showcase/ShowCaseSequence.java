/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI.showcase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Showcase sequence controller
 */
public class ShowCaseSequence implements SequenceItemListener {

    private int sequenceCount = -1;

    private List<ShowCaseSequenceItem> showCaseSequenceItemList;
    private SequenceCloseListener closeListener;
    private SequenceEndListener endListener;

    /**
     * Create new sequence with empty showcase item list.
     */
    public ShowCaseSequence(){
        showCaseSequenceItemList = new ArrayList<>();
    }

    /**
     * Add list of showcase items to list
     * @param itemList showcase items
     * @return showcase sequence object
     */
    public ShowCaseSequence addItem(@NotNull List<ShowCaseSequenceItem> itemList){
        showCaseSequenceItemList.addAll(itemList);
        return this;
    }

    /**
     * Add showcase item to list
     * @param item showcase item
     * @return showcase sequence object
     */
    public ShowCaseSequence addItem(@NotNull ShowCaseSequenceItem item){
        showCaseSequenceItemList.add(item);
        return this;
    }

    /**
     * Start showcase sequence. If showcase item list is empty, it do nothing.
     */
    public void start() {
        if (!showCaseSequenceItemList.isEmpty()) {
            sequenceCount = 0;
            showCaseSequenceItemList.get(sequenceCount)
                    .setSequenceItemListener(this)
                    .show();
        }
    }

    /**
     * Stops sequence and dismiss last displayed showcase item. List of showcase item is cleared.
     */
    public void stop() {
        if (sequenceCount >= 0 && sequenceCount < showCaseSequenceItemList.size()) {
            ShowCaseSequenceItem currencyShownItem = showCaseSequenceItemList.get(sequenceCount);
            sequenceCount = -1;
            currencyShownItem.dismiss();
            showCaseSequenceItemList.clear();
        }
    }

    @Override
    public void onDismiss() {
        sequenceCount++;
        if (sequenceCount < showCaseSequenceItemList.size() && sequenceCount > 0) {
            showCaseSequenceItemList.get(sequenceCount)
                    .setSequenceItemListener(this)
                    .show();
        } else {
            if (endListener != null) endListener.onSequenceEnd();
        }
    }

    @Override
    public void onClose() {
        showCaseSequenceItemList.clear();
        if (closeListener != null) closeListener.onSequenceClose();
    }

    /**
     * Set listener whose method is called when the sequence is skipp.
     * @param closeListener
     * @return showcase sequence object
     */
    public ShowCaseSequence setCloseListener(SequenceCloseListener closeListener) {
        this.closeListener = closeListener;
        return this;
    }

    /**
     * Set listener whose method is called when the sequence is end.
     * @param endListener
     * @return
     */
    public ShowCaseSequence setEndListener(SequenceEndListener endListener) {
        this.endListener = endListener;
        return this;
    }

    interface SequenceEndListener{
        void onSequenceEnd();
    }

    interface SequenceCloseListener{
        void onSequenceClose();
    }
}
