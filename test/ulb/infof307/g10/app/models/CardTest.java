package ulb.infof307.g10.app.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;


class CardTest {

    private final int id = 1564984;
    private final String category = "French";
    private final String question = "Who ?";
    private final String answer = "You";
    private final int evaluation = 0;
    private final int typeOfCard = 0;
    private final Card card = new Card(id, category, question, answer, evaluation, typeOfCard);

    @Test
    void testId(){
        Assertions.assertEquals(this.card.getId(), id);
    }

    @Test
    void testQuestion(){
        Assertions.assertEquals(this.card.getQuestion(), question);
    }

    @Test
    void testAnswer(){
        Assertions.assertEquals(this.card.getAnswer(), answer);
    }

    @Test
    void testEvaluation(){
        Assertions.assertEquals(this.card.getEvaluation(), evaluation);
    }

    @Test
    void testCategory(){
        Assertions.assertEquals(this.card.getCategory(), category);
    }

    @Test
    void testTypeOfCard(){
        Assertions.assertEquals(this.card.getTypeOfCard(), typeOfCard);
    }

    @Test
    void testSetAnswer(){
        String newAnswer = "Me";
        card.setAnswer(newAnswer);
        Assertions.assertEquals(this.card.getAnswer(), newAnswer);
    }

    @Test
    void testSetQuestion(){
        String newQuestion = "What ?";
        card.setQuestion(newQuestion);
        Assertions.assertEquals(this.card.getQuestion(), newQuestion);
    }

    @Test
    void testSetEvaluation(){
        int newEvaluation = 1;
        card.setEvaluation(newEvaluation);
        Assertions.assertEquals(this.card.getEvaluation(), newEvaluation);
    }

    @Test
    void testSetCategory(){
        String newCategory = "English";
        card.setCategory(newCategory);
        Assertions.assertEquals(this.card.getCategory(), newCategory);
    }

    @Test
    void testSetId(){
        int newId = 1564985;
        card.setId(newId);
        Assertions.assertEquals(this.card.getId(), newId);
    }
}