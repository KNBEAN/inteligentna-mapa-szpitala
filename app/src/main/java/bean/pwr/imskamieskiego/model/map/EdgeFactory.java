package bean.pwr.imskamieskiego.model.map;

/**
 * This class can be use to create Edge objects.
 */
public class EdgeFactory {

    /**
     * Create new Location object with given parameters.
     * @param from
     * @param to
     * @param length
     * @return
     */
    static public Edge create(int from, int to, int length){
        return new EdgeImpl(from, to, length);
    }

    private static class EdgeImpl implements Edge{
        private int from;
        private int to;
        private int length;

        public EdgeImpl(int from, int to, int length) {
            this.from = from;
            this.to = to;
            this.length = length;
        }

        @Override
        public int getFrom() {
            return from;
        }

        @Override
        public int getTo() {
            return to;
        }

        @Override
        public int getLength() {
            return length;
        }
    }

}
