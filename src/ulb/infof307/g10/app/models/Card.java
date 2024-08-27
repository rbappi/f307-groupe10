package ulb.infof307.g10.app.models;

public class Card {
    private int id;
    private String category;
    private String question;
    private String answer;
    private int evaluation;
    private int typeOfCard;

    public Card(int id, String category, String question, String answer, int evaluation, int typeOfCard){
        this.id = id;
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.evaluation = evaluation;
        this.typeOfCard = typeOfCard;
    }
    public int getId(){
        return id;
    }
    public String getAnswer() {return answer; }
    public String getQuestion() {
        return question;
    }
    public int getEvaluation() { return this.evaluation; }
    public String getCategory() { return category; }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setEvaluation(int evaluation) { this.evaluation = evaluation; }
    public void setCategory(String category) { this.category = category; }
    public int getTypeOfCard() { return typeOfCard; }
}




