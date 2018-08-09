package bean.pwr.imskamieskiego.path_search;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public class PathSearcher {
    private static String TAG = "PathSearcher";

    private Looper looper;
    private Handler handler;
    private Handler responseHandle;

    private SearchCompleteListener listener;
    private boolean inProgress = false;

    private PathSearchAlgorithm algorithm = null;



    public PathSearcher(Context context){
        responseHandle = new Handler(context.getMainLooper());
    }


    private Handler prepareThread(String name){
        HandlerThread handlerThread = new HandlerThread(name);
        handlerThread.setDaemon(true);
        handlerThread.start();
        looper = handlerThread.getLooper();
        return new Handler(looper);
    }


    public void startSearch(final PathSearchAlgorithm algorithm){
        if(inProgress) throw new IllegalStateException();

        inProgress = true;
        handler = prepareThread(TAG);

        this.algorithm = algorithm;

        handler.post(this::startAlgorithm);

    }


    private void startAlgorithm(){

        this.algorithm.startSearch();

        responseHandle.post(this::taskComplete);
    }


    private void taskComplete() {
        Log.i(TAG,"Search complete");
        inProgress = false;

        if(listener != null){
            listener.searchComplete(this.algorithm.getPatch());
        }

        algorithm = null;
        looper.quitSafely();
    }


    public PathSearchAlgorithm getAlgorithm() {
        return algorithm;
    }


    public void setListener(SearchCompleteListener listener) {
        this.listener = listener;
    }


    public void removeListener(){
        listener = null;
    }


    public boolean isInProgress() {
        return inProgress;
    }
}
