package it.polimi.ingsw.model.ObjectiveCard;

import it.polimi.ingsw.model.CardColor;
import java.awt.*;

/**
 * This is a supporter record used to represent the pattern contained in the target location card
 */
public record PositionalData(Point point, CardColor cardColor) {}