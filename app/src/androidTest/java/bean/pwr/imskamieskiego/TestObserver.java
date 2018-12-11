/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TestObserver<T> implements Observer<T> {
    public List<T> observedValues = new ArrayList<T>();

    @Override
    public void onChanged(@Nullable Object o) {
        observedValues.add((T) o);
    }
}
