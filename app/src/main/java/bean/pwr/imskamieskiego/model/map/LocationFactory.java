package bean.pwr.imskamieskiego.model.map;

import javax.annotation.Nullable;

public class LocationFactory {

    /**
     * Create new Location object with given parameters.
     * @param id
     * @param name
     * @param description
     * @return
     */
    public static Location create(int id, String name, String description){
        return new locationImpl(id, name, description);
    }

    /**
     * Create new Location object with given parameters. ID of this object will be set to -1.
     * @param name
     * @param description
     * @return
     */
    public static Location create(String name, String description){
        return create(-1, name, description);
    }

    static private class locationImpl implements Location{
        private  int id;
        private String name;
        private String description;

        public locationImpl(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Nullable
        @Override
        public String getDescription() {
            return description;
        }
    }


}
