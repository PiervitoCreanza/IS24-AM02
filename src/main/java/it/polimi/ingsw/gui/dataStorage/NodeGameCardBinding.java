package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.gui.GameCardImage;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

public class NodeGameCardBinding extends NodeDataBinding {
    GameCardImage gameCardImage;
    Consumer<ImageView> updateCallback;

    public NodeGameCardBinding(Pane boundNode, Consumer<ImageView> updateCallback) {
        super(boundNode);
        this.updateCallback = updateCallback;
    }

    public NodeGameCardBinding(Pane boundNode) {
        super(boundNode);
    }

    private void update() {
        ImageView gameCardNode = gameCardImage.getCurrentSide();
        gameCardNode.setUserData(this);
        super.update(gameCardNode);
        updateCallback.accept(gameCardNode);
    }

    public void switchSide() {
        gameCardImage.switchSide();
        update();
    }

    public GameCard getGameCard() {
        if (gameCardImage == null) {
            return null;
        }
        return gameCardImage.getGameCard();
    }

    public void setGameCard(GameCard gameCard) {
        this.gameCardImage = new GameCardImage(gameCard);
        update();
    }

    public void removeGameCard() {
        this.gameCardImage = null;
        super.update(null);
    }

    @Override
    public ImageView getCurrentlyDisplayedNode() {
        return (ImageView) super.getCurrentlyDisplayedNode();
    }

    public GameCardImage getGameCardImage() {
        return gameCardImage;
    }
}
