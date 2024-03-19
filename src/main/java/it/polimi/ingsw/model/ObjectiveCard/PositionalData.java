package it.polimi.ingsw.model.ObjectiveCard;
import it.polimi.ingsw.model.CardColor;
import java.awt.*;

/**
 * This is a supporter class used to represent the pattern contained in the target location card
 */
public class PositionalData {
    private Point point;
    private CardColor color;

    public PositionalData(Point point, CardColor color) {
        this.point = point;
        this.color = color;
    }

    public Point getPoint() {
        return point;
    }

    public CardColor getColor() {
        return color;
    }

    public void setPoint(int x, int y) {
        this.point.x = x;
        this.point.y = y;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }
}
