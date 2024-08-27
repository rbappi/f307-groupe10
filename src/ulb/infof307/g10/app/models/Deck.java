package ulb.infof307.g10.app.models;

import java.util.ArrayList;
import java.util.List;


public class Deck {
    private String id;
    private List<String> categoryList;
    private List<Card> listOfCards = new ArrayList<>();
    private String deckName;
    private String scoreMax;

    public Deck(String id, String deckName, List<String> categoryList, String scoreMax){
        this.id = id;
        this.deckName = deckName;
        this.categoryList = categoryList;
        this.scoreMax = scoreMax;
    }
    public void setId(String id) {this.id = id; }
    public String getId() {return id; }
    public void setCategoryList(List<String> categoryList) {this.categoryList = categoryList; }
    public void addCategory(String category) {this.categoryList.add(category);}
    public List<String> getCategoryList() {return categoryList; }
    public List<Card> getListOfCards() {return listOfCards; }
    public void addCard(Card card){listOfCards.add(card); }
    public String getDeckName() { return this.deckName; }
    public boolean isEmpty() {return this.listOfCards.isEmpty();}
    public String getScoreMax() {return this.scoreMax;}
}
