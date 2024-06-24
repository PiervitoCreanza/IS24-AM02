package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalBoardTest {
    private GlobalBoard globalBoard;
    private GlobalBoard emptyGlobalBoard;

    @BeforeEach
    void setUp() {
        //Two cards for each deck
        ArrayList<GameCard> mockGameCards = new ArrayList<>(List.of(Mockito.mock(GameCard.class), Mockito.mock(GameCard.class)));
        ArrayList<ObjectiveCard> mockObjectiveCards = new ArrayList<>(List.of(Mockito.mock(ObjectiveCard.class), Mockito.mock(ObjectiveCard.class)));
        //When a globalBoard is created two cards will be drawn from the decks, so all decks will be empty
        emptyGlobalBoard = new GlobalBoard(new Deck<>(mockGameCards), new Deck<>(mockGameCards), new Deck<>(mockObjectiveCards), new Deck<>(mockGameCards));

        //Add a card to both decks, so they won't be empty
        mockGameCards.add(Mockito.mock(GameCard.class));
        mockObjectiveCards.add(Mockito.mock(ObjectiveCard.class));
        globalBoard = new GlobalBoard(new Deck<>(mockGameCards), new Deck<>(mockGameCards), new Deck<>(mockObjectiveCards), new Deck<>(mockGameCards));
    }

    @Test
    @DisplayName("Constructor draws two cards for each field")
    void constructorShouldInitializeFieldsCorrectly() {
        assertEquals(2, globalBoard.getGlobalObjectives().size());
        assertEquals(2, globalBoard.getFieldGoldCards().size());
        assertEquals(2, globalBoard.getFieldResourceCards().size());
    }

    @Test
    @DisplayName("isGoldDeckEmpty returns true when deck is empty")
    void isGoldDeckEmptyShouldReturnTrueWhenDeckIsEmpty() {
        assertTrue(emptyGlobalBoard.isGoldDeckEmpty());
    }

    @Test
    @DisplayName("isGoldDeckEmpty returns false when deck is not empty")
    void isGoldDeckEmptyShouldReturnFalseWhenDeckIsNotEmpty() {
        assertFalse(globalBoard.isGoldDeckEmpty());
    }

    @Test
    @DisplayName("isResourceDeckEmpty returns true when deck is empty")
    void isResourceDeckEmptyShouldReturnTrueWhenDeckIsEmpty() {
        assertTrue(emptyGlobalBoard.isResourceDeckEmpty());
    }

    @Test
    @DisplayName("isResourceDeckEmpty returns false when deck is not empty")
    void isResourceDeckEmptyShouldReturnFalseWhenDeckIsNotEmpty() {
        assertFalse(globalBoard.isResourceDeckEmpty());
    }

    @Test
    @DisplayName("drawCardFromField removes selected gold card from field and adds a new one")
    void drawCardFromFieldShouldRemoveGoldCardAndAddNewCard() {
        GameCard chosenGoldCard = Mockito.mock(GameCard.class);
        when(chosenGoldCard.getCardId()).thenReturn(1);
        //Cards on the field will be chosen randomly from the deck, so I need to "force add" the card that I want to draw
        globalBoard.getFieldGoldCards().removeFirst();
        globalBoard.getFieldGoldCards().add(chosenGoldCard);

        globalBoard.drawCardFromField(chosenGoldCard.getCardId());
        assertFalse(globalBoard.getFieldGoldCards().contains(chosenGoldCard));
        assertEquals(2, globalBoard.getFieldGoldCards().size());
    }

    @Test
    @DisplayName("drawCardFromField removes selected resource card from field and adds a new one")
    void drawCardFromFieldShouldRemoveResourceCardAndAddNewCard() {
        GameCard chosenResourceCard = Mockito.mock(GameCard.class);
        when(chosenResourceCard.getCardId()).thenReturn(1);
        //Cards on the field will be chosen randomly from the deck, so I need to "force add" the card that I want to draw
        globalBoard.getFieldResourceCards().removeFirst();
        globalBoard.getFieldResourceCards().add(chosenResourceCard);

        globalBoard.drawCardFromField(chosenResourceCard.getCardId());
        assertFalse(globalBoard.getFieldResourceCards().contains(chosenResourceCard));
        assertEquals(2, globalBoard.getFieldResourceCards().size());
    }

    @Test
    @DisplayName("drawCardFromField returns an exception if the card is not present")
    void drawCardFromFieldShouldThrowExceptionIfCardNotPresent() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> globalBoard.drawCardFromField(-1));
        assertEquals("This card is not present on the field", exception.getMessage());
    }
}
