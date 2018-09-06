package bean.pwr.imskamieskiego.NavigationWindow;


public interface NavWindowListener {
    void onBack();
    void startNavigation();
    void updateNavFragmentState();
    void setChangeFloorButtonCoords(int barHeight, int resumeCounter);
}
