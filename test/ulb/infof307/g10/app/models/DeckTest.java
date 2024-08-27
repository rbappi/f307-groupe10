package ulb.infof307.g10.app.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class DeckTest {

    private  String id = "1564984";
    private List<String> category = new ArrayList<String>();
    private Deck deck = new Deck(id, "test", category, "100");

    public DeckTest(){
        deck.addCategory("Math");
    }

    @Test
    void testSetId(){
        String newId = "03546854";
        this.deck.setId(newId);
        Assertions.assertEquals(this.deck.getId(), newId);
    }

    @Test
    void testGetId(){
        this.deck.setId("0");
        String newId2 = "0";
        Assertions.assertEquals(this.deck.getId(), newId2);
    }

    @Test
    void testSetCategory(){
        List<String> newCategory = new ArrayList<>();
        newCategory.add("Geographie");
        this.deck.setCategoryList(newCategory);
        Assertions.assertEquals(this.deck.getCategoryList(), newCategory);
    }

    @Test
    void testAddCategory(){
        String newCategory = "Geographie";
        deck.addCategory(newCategory);
        Assertions.assertEquals(this.deck.getCategoryList().get(1), newCategory);
    }

    @Test
    void testGetCategory(){
        deck.addCategory("Geographie");
        Assertions.assertEquals("Geographie", this.deck.getCategoryList().get(1));
    }

    @Test
    void testAddCard(){
        Card card = new Card(1, "Math", "2+2", "4", 0, 3);
        this.deck.addCard(card);
        Assertions.assertEquals(this.deck.getListOfCards().get(0), card);
    }

    @Test
    void testGetDeckName(){
        Assertions.assertEquals("test", this.deck.getDeckName());
    }

    @Test
    void testIsEmpty(){
        Assertions.assertTrue(this.deck.isEmpty());
    }

    @Test
    void testGetListOfCards(){
        List <Card> listOfCards = this.deck.getListOfCards();
        Assertions.assertEquals(this.deck.getListOfCards(), listOfCards);
    }

    @Test
    void testGeScoreMax(){
        String scoreMax = this.deck.getScoreMax();
        Assertions.assertEquals(this.deck.getScoreMax(), scoreMax);
    }
}