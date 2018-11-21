package bean.pwr.imskamieskiego.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventWrapperTest {

    @Test
    void getDataToHandle() {
        List<Integer> expectedList = Arrays.asList(1, 0, 2, 3, 7);
        EventWrapper<List> eventWrapper = new EventWrapper<>(expectedList);

        assertAll(
                () -> assertFalse(eventWrapper.isHandled()),
                () -> assertEquals(expectedList, eventWrapper.handleData()),
                () -> assertTrue(eventWrapper.isHandled()),
                () -> assertNull(eventWrapper.handleData()),
                () -> assertEquals(expectedList, eventWrapper.getData())
        );
    }

    @Test
    void setNullAsData() {
        EventWrapper<Integer> eventWrapper = new EventWrapper<>(null);

        assertAll(
                () -> assertFalse(eventWrapper.isHandled()),
                () -> assertNull(eventWrapper.handleData()),
                () -> assertTrue(eventWrapper.isHandled())
        );
    }
}