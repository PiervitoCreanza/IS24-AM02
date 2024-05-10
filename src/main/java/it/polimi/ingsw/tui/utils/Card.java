package it.polimi.ingsw.tui.utils;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;

import java.util.HashMap;

public class Card {
    private final GameCard card;

    public Card(GameCard card) {
        this.card = card;
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.insert(10, "a");
        System.out.println(sb);
        GameCard card = new Parser().getGoldDeck().getCards().getFirst();
        Card c = new Card(card);
        HashMap<Coordinate, Character> map = new HashMap<>();

        //c.print();
    }

    public void print() {
        System.out.print("""
                ╔══════════════╗
                ║ F │
                ║────┘
                ║
                ║
                ╚══════════════╝
                ┌───┬───────┬───┐
                │ F │  1 P  │ P │
                ├───┘   F   └───┤
                │       A       │
                ├───┐   A   ┌───┤
                │ A │       │ A │
                └───┴───────┴───┘
                ┌───┬─────────────┬───┐
                │ F │             │ P │
                ├───┘      F      └───┤
                │          A          │
                ├───┐      A      ┌───┤
                │ A │             │ A │
                └───┴─────────────┴───┘
                ┌───┬───────┬───┐
                │ F │  2-P  │ P │
                ├───┘       └───┤
                ├───┐       ┌───┤───────┬───┐
                │   │ CCAAF │ A │       │ P │
                └───┴───────┴───┘   F   └───┤
                            │       A   ┌───┤
                            │           │ A │
                            └───────────┴───┘
                     ───────┬───┐
                       1 P  │ P │
                        F   └───┤
                │       A   ┌───┤
                │       A   │ A │
                └───────────┴───┘
                ┌───────────────┐
                │  ┌───┐        │
                │  │ 2 │  ■     │
                │  └───┘  ■  ■  │
                │               │
                └───────────────┘
                ┌───────────────┐
                │   2           │
                │        ▀      │
                │        ▀  ▀   │
                │               │
                └───────────────┘
                ┌───┬─┬─────┬─┬───┐
                │ F │ │ 1 P │ │ P │
                ├───┘ └─────┘ └───┤
                │        A        │
                ├───┐    A    ┌───┤
                │ A │    F    │ A │
                └───┴─────────┴───┘
                """);
    }
}
