package it.polimi.ingsw.gui.dataStorage;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Set;

public class ObservedGameCardsGroup {
    private final HashMap<String, ObservedGameCard> cardCells;
    private final Node root;

    public ObservedGameCardsGroup(Node root) {
        this.root = root;
        this.cardCells = new HashMap<>();
    }

    public void addCard(String cardName, ObservedGameCard card) {
        cardCells.put(cardName, card);
    }

    public void addCard(String boundImageViewName) {
        cardCells.put(boundImageViewName, new ObservedGameCard((ImageView) root.lookup("#" + boundImageViewName)));
    }

    public ObservedGameCard getCard(String cardName) {
        return cardCells.get(cardName);
    }

    public Set<ObservedGameCard> getCards() {
        return Set.copyOf(cardCells.values());
    }

    public Set<String> getCardNames() {
        return Set.copyOf(cardCells.keySet());
    }

    public void removeCard(String cardName) {
        cardCells.remove(cardName);
    }

    public void clear() {
        cardCells.clear();
    }

    public boolean containsCard(String cardName) {
        return cardCells.containsKey(cardName);
    }
}
