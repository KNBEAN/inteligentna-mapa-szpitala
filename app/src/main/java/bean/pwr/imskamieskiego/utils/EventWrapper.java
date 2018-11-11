package bean.pwr.imskamieskiego.utils;

public class EventWrapper<T> {

    private boolean isHandled;
    private T data;

    /**
     * Create new event wrapper for data. Wrapper preserve information, was data read before.
     * @param data
     */
    public EventWrapper(T data){
        this.data = data;
        isHandled = false;
    }

    /**
     * Return information, was data read before.
     * @return return true if data was handled in past. False if data is new.
     */
    public boolean isHandled() {
        return isHandled;
    }

    public T handleData(){
        if (isHandled){
            return null;
        }else {
            isHandled = true;
            return data;
        }
    }

    public T getData() {
        return data;
    }
}
