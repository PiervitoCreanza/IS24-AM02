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
                Resources
                ┌─────────────────────┐
                │  Resources:         │
                │   F [Fungi]  = 11   │
                │   P [Plant]  = 12   │
                │   A [Animal] = 12   │
                │   I [Insect] = 12   │
                └─────────────────────┘
                Items
                ┌─────────────────────────┐
                │  Items:                 │
                │   Q [Quill]      = 12   │
                │   I [Inkwell]    = 12   │
                │   M [Manuscript] = 12   │
                │                         │
                └─────────────────────────┘
                Gold - Item
                ┌───┬───[12,13]───┬───┐
                │ F │ 2 - Item(I) │ P │
                ├───┘             └───┤
                │                     │
                ├───┐  _ _        ┌───┤
                │ A │{ F F F F F ││ A │
                └───┴─────────────┴───┘
                Gold - Corner
                ┌───┬────[2,3]────┬───┐
                │ F │ 2 - Corners │ P │
                ├───┘             └───┤
                │                     │
                ├───┐     F F     ┌───┤
                │ A │    FFF    │ A │
                └───┴─────────────┴───┘
                Gold - solo punti
                ┌───┬────[2,3]────┬───┐
                │ F │      2      │ P │
                ├───┘             └───┤
                │                     │
                ├───┐             ┌───┤
                │ A │             │ A │
                └───┴─────────────┴───┘
                Resource
                ┌───┬────[2,3]────┬───┐
                │ F │      1      │ P │
                ├───┘             └───┤
                │                     │
                ├───┐             ┌───┤
                │ A │             │ A │
                └───┴─────────────┴───┘
                Back
                ┌───┬───[12,13]───┬───┐
                │ F │             │ P │
                ├───┘             └───┤
                │          F          │
                ├───┐             ┌───┤
                │ A │             │ A │
                └───┴─────────────┴───┘
                Starter
                ┌───┬────[2,3]────┬───┐
                │ F │             │ P │
                ├───┘      F      └───┤
                │        F F F        │
                ├───┐             ┌───┤
                │ A │             │ A │
                └───┴─────────────┴───┘
                Positional Objective
                ┌─────────────────────┐
                │    ---              │
                │  ┌─────┐  ██        │
                │  │  2  │     ██     │
                │  └─────┘        ██  │
                │    ---              │
                └─────────────────────┘
                Item Objective card
                ┌─────────────────────┐
                │    ---              │
                │  ┌─────┐     F      │
                │  │  2  │   F   F    │
                │  └─────┘  -------   │
                │    ---              │
                └─────────────────────┘
                                
                  _ \
                 / |
                 | |
                 |_|
                   ____ \s
                   |___ \\\s
                     __) |
                    |__ <\s
                    ___) |
                   |____/\s
                  
                  
                  
                  
                  ____ 
                 |___  \
                   __) |
                  / __/
                 |_____|
                       
                   _____  \s
                  / ___ `.\s
                 |_/___) |\s
                  .'____.'\s
                 / /_____ \s
                 |_______|\s
                         \s
                 
                 _____ 
                |___ 
                  __) |
                 / __/
                |_____|
                      
                                
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
