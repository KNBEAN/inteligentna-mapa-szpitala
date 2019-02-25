/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI.showcase;

import java.util.ArrayList;
import java.util.List;

public class ShowCaseSequence implements SequenceItemListener {

    private int sequenceCount = -1;

    private List<ShowCaseSequenceItem> showCaseSequenceItemList;
    private SequenceCloseListener closeListener;
    private SequenceEndListener endListener;

    public ShowCaseSequence(){
        showCaseSequenceItemList = new ArrayList<>();
    }

    public ShowCaseSequence addItem(List<ShowCaseSequenceItem> itemList){
        showCaseSequenceItemList.addAll(itemList);
        return this;
    }

    public ShowCaseSequence addItem(ShowCaseSequenceItem item){
        showCaseSequenceItemList.add(item);
        return this;
    }

    public void start() {
        if (!showCaseSequenceItemList.isEmpty()) {
            sequenceCount = 0;
            showCaseSequenceItemList.get(sequenceCount)
                    .setSequenceItemListener(this)
                    .show();
        }
    }

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

    public ShowCaseSequence setCloseListener(SequenceCloseListener closeListener) {
        this.closeListener = closeListener;
        return this;
    }

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
