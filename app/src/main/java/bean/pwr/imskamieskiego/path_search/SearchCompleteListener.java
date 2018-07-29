package bean.pwr.imskamieskiego.path_search;

import java.util.List;
import bean.pwr.imskamieskiego.model.map.MapPoint;

public interface SearchCompleteListener {
    void searchComplete(List<MapPoint> trace);
}
