package ulb.infof307.g10.app.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ulb.infof307.g10.app.models.Deck;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DeckControllerTest {
    PacketController pc = new PacketController(Mockito.mock(Socket.class));
    WindowController wc = new WindowController(null);
    DeckController deckController = new DeckController(pc, wc);

    @Test
    void checkCardCategoryTest1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = DeckController.class.getDeclaredMethod("checkCardCategory", List.class, String.class);
        method.setAccessible(true);

        List<String> categoryList = Arrays.asList("math", "math", "math");
        boolean answer = (boolean) method.invoke(deckController,  categoryList, "math");
        assertTrue(answer);
    }

    @Test
    void checkCardCategoryTest2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = DeckController.class.getDeclaredMethod("checkCardCategory", List.class, String.class);
        method.setAccessible(true);

        List<String> categoryList = Arrays.asList("math", "french", "geo");
        boolean answer = (boolean) method.invoke(deckController,  categoryList, "math");
        assertTrue(answer);
    }

    @Test
    void checkCardCategoryTest3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = DeckController.class.getDeclaredMethod("checkCardCategory", List.class, String.class);
        method.setAccessible(true);

        List<String> categoryList = Arrays.asList("math", "french", "geo");
        boolean answer = (boolean) method.invoke(deckController,  categoryList, "latin");
        assertFalse(answer);
    }

    @Test
    void getIndexDeckTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = DeckController.class.getDeclaredMethod("getIndexDeck", Deck.class);
        method.setAccessible(true);
        List<String> categoryList = Arrays.asList("math", "french", "geo");
        Deck deck = new Deck(Integer.toString(1), "deckName", categoryList, "0");
        int answer = (int) method.invoke(deckController, deck);
        assertEquals(answer, -1);

    }

    @Test
    void getCurrentDeckObjetTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = DeckController.class.getDeclaredMethod("getCurrentDeckObjet", String.class);
        method.setAccessible(true);
        Deck answer = (Deck) method.invoke(deckController, "Math");
        assertNull(answer);

    }

    @Test
    void getDeckCategory() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = DeckController.class.getDeclaredMethod("getDeckCategory", String.class);
        method.setAccessible(true);
        List<String> deckCategory = new ArrayList<>();

        List<String> answer = (List<String>) method.invoke(deckController, "Math");
        assertEquals(answer, deckCategory);

    }
}