package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private GameCard mockGameCard;
    private Deck<GameCard> emptyDeck;
    private Deck<GameCard> fullDeck;

    @BeforeEach
    void setUp() {
        mockGameCard = Mockito.mock(GameCard.class);
        emptyDeck = new Deck<>(new ArrayList<>());
        fullDeck = new Deck<>(new ArrayList<>(List.of(mockGameCard)));
    }

    @Test
    @DisplayName("Deck constructor returns an exception when a NULL objects is passed")
    void nullArgumentInTheConstructorShouldThrowException() {
        assertThrows(NullPointerException.class, () -> new Deck<>(null));
    }

    @Test
    @DisplayName("isEmpty returns true if deck is empty")
    void isEmptyShouldReturnTrueForEmptyDeck() {
        assertTrue(emptyDeck.isEmpty());
    }

    @Test
    @DisplayName("isEmpty returns false if deck is not empty")
    void isEmptyShouldReturnFalseForNonEmptyDeck() {
        assertFalse(fullDeck.isEmpty());
    }

    @Test
    @DisplayName("Draw method returns an exception when Deck is empty")
    void drawFromEmptyDeckShouldThrowException() {
        Exception exception = assertThrows(RuntimeException.class, emptyDeck::draw);
        assertEquals("The deck is empty", exception.getMessage());
    }

    @Test
    @DisplayName("Draw method returns a GameCard if it's not empty")
    void drawFromNonEmptyDeckShouldReturnCard() {
        assertEquals(mockGameCard, fullDeck.draw());
    }

    @Test
    @DisplayName("Draw method should remove card from deck")
    void drawShouldRemoveCardFromDeck() {
        fullDeck.draw();
        assertTrue(fullDeck.isEmpty());
    }

    @Test
    @DisplayName("addCard method should add a card to the deck")
    void addCardShouldAddCardToDeck() {
        GameCard newCard = Mockito.mock(GameCard.class);
        emptyDeck.addCard(newCard);
        assertFalse(emptyDeck.isEmpty());
        assertEquals(emptyDeck.draw(), newCard);
    }

    @Test
    @DisplayName("addCard method should throw exception when adding a null card")
    void addCardShouldThrowExceptionWhenAddingNullCard() {
        assertThrows(NullPointerException.class, () -> emptyDeck.addCard(null));
    }

    @Test
    @DisplayName("addCard method should throw exception when adding a duplicate card")
    void addCardShouldThrowExceptionWhenAddingDuplicateCard() {
        assertThrows(IllegalArgumentException.class, () -> fullDeck.addCard(mockGameCard));
    }

}


