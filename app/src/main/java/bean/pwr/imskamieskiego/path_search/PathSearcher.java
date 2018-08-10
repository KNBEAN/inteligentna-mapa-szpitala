package bean.pwr.imskamieskiego.path_search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

/**
 * PathSearcher is class which provide non-blocking executing of path search algorithms. For this
 * reason, this class is suitable for use in the main thread. PathSearcher execute algorithms in
 * separate thread and returns results via LiveData objects.
 */
public class PathSearcher {
    private static String TAG = "PathSearcher";

    private Looper looper;
    private Handler responseHandle;

    private boolean inProgress = false;

    private PathSearchAlgorithm algorithm;
    private MutableLiveData<List<MapPoint>> liveDataTrace;

    /**
     * Create new instance of PathSearcher.
     * @see PathSearcher
     * @param context
     */
    public PathSearcher(Context context){
        responseHandle = new Handler(context.getMainLooper());
        liveDataTrace = new MutableLiveData<>();
    }

    /**
     * Starts search algorithm execution.This method is non-blocking, because search process starts
     * in new thread. PathSearcher can only do one search at a time. IllegalStateException will be
     * thrown when attempting to run the search while the previous one has not been completed.
     * @param algorithm Instance of algorithm used to path search.
     * @throws IllegalStateException throw when previous search has not been completed
     */
    public void startSearch(PathSearchAlgorithm algorithm) throws IllegalStateException{
        if(inProgress) {
            throw new IllegalStateException("The previous search has not been completed. Can not start a new search.");
        }
        this.algorithm = algorithm;
        inProgress = true;
        Handler threadHandler = prepareThread(TAG);
        threadHandler.post(this::executeAlgorithm);
    }

    private Handler prepareThread(String name){
        HandlerThread handlerThread = new HandlerThread(name);
        handlerThread.setDaemon(true);
        handlerThread.start();
        looper = handlerThread.getLooper();
        return new Handler(looper);
    }

    private void executeAlgorithm(){
        Log.d(TAG, "Algorithm start");
        long startTime = System.nanoTime();
        algorithm.startSearch();
        long endTime = System.nanoTime();
        Log.d(TAG, "Algorithm ended in " + ((endTime-startTime)/1000000)+ "ms");

        liveDataTrace.postValue(algorithm.getPatch());
        responseHandle.post(this::searchComplete);
    }

    private void searchComplete() {
        Log.i(TAG,"Search complete");
        inProgress = false;
        algorithm = null;

        looper.quitSafely();
    }

    /**
     * Returns the actual state of the search process.
     * @return returns true if the search is in progress, otherwise returns false
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Returns the resulting path as LiveData. When the search is complete, the LiveData objects
     * will be updated.
     * @see LiveData
     * @return LiveData object representing the path of results
     */
    public LiveData<List<MapPoint>> getPath(){
        return liveDataTrace;
    }
}
