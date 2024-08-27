package ulb.infof307.g10.app.models;

public class DeckForSale {

    private int id;
    private String deckName;
    private String sellerUsername;
    private int price;
    private boolean inStore;

    public DeckForSale(int id, String deckName, String sellerUsername, int price, boolean inStore){
        this.id = id;
        this.deckName = deckName;
        this.sellerUsername = sellerUsername;
        this.price = price;
        this.inStore = inStore;

    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getDeckName() { return this.deckName; }
    public String getSellerUsername() { return this.sellerUsername; }
    public int getPrice() { return this.price; }
    public boolean isInStore() { return this.inStore; }

}
