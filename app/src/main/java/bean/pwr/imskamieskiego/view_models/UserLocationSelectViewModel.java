/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.MapRepository;
import bean.pwr.imskamieskiego.utils.EventWrapper;

public class UserLocationSelectViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> submitSearchByID;
    private LiveData<EventWrapper<MapPoint>> searchResult;

    public UserLocationSelectViewModel(@NonNull Application application) {
        super(application);
        LocalDB db = LocalDB.getDatabase(application.getApplicationContext());
        MapRepository mapRepository = new MapRepository(db);
        submitSearchByID = new MutableLiveData<>();
        LiveData<MapPoint> tmp = Transformations.switchMap(submitSearchByID, mapRepository::getPointByID);
        searchResult = Transformations.map(tmp, EventWrapper::new);
    }

    public LiveData<EventWrapper<MapPoint>> getSearchResult() {
        return searchResult;
    }

    public void searchPointByCode(int pointId) {
        submitSearchByID.postValue(pointId);
    }
}
