package colonizer;

import colonizer.enums.ActionCardType;
import colonizer.enums.GameStance;
import colonizer.enums.ResourceCardType;
import colonizer.map.*;
import dk.ilios.asciihexgrid.AsciiBoard;
import dk.ilios.asciihexgrid.printers.LargeFlatAsciiHexPrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static colonizer.enums.ResourceCardType.*;
import static colonizer.enums.ActionCardType.*;
import static colonizer.enums.HexagonType.*;

public class GameTable {

    private int[] resourceCards = new int[5];

    private List<ActionCard> actionCardDeck;

    private Hexagon[][] hexagons;
    private Intersection[][] intersections;
    private Road[][] roads;

    private List<Road> routes;

    private Hexagon robberPosition;

    private List<Player> players;
    private int currentPlayer;

    private GameStance gameStance = GameStance.PREPARING;
    private int firstPlayer, lastPlayer;
    private boolean endOfGame = false;

    void generateConstants () {
        //Resource Cards

        resourceCards[RES_CLAY.id] = 19;
        resourceCards[RES_WOOD.id] = 19;
        resourceCards[RES_WOOL.id] = 19;
        resourceCards[RES_WHEAT.id] = 19;
        resourceCards[RES_STONE.id] = 19;

        //Action Cards

        actionCardDeck = new ArrayList<> ();

        for (int i = 0; i < 14; ++i)
            actionCardDeck.add (new ActionCard (KNIGHT));
        for (int i = 0; i < 5; ++i)
            actionCardDeck.add (new ActionCard (POINT_CARD));
        for (int i = 0; i < 2; ++i)
            actionCardDeck.add (new ActionCard (MONOPOLY));
        for (int i = 0; i < 2; ++i)
            actionCardDeck.add (new ActionCard (YEAR_OF_PLENTY));
        for (int i = 0; i < 2; ++i)
            actionCardDeck.add (new ActionCard (ROUTE_BUILDING));

        Collections.shuffle (actionCardDeck);

    }

    public void generateTestMap () {
        generateConstants ();

        //Hexagons

        hexagons = new Hexagon[7][7];

        hexagons[0][0] = new Hexagon ();
        hexagons[0][1] = new Hexagon ();
        hexagons[0][2] = new Hexagon (2, 0, STONE_HARBOR);
        hexagons[0][3] = new Hexagon (3, 0, WATER);
        hexagons[0][4] = new Hexagon (4, 0, WOOL_HARBOR);
        hexagons[0][5] = new Hexagon (5, 0, WATER);
        hexagons[0][6] = new Hexagon ();

        hexagons[1][0] = new Hexagon ();
        hexagons[1][1] = new Hexagon (1, 1, WATER);
        hexagons[1][2] = new Hexagon (2, 1, WHEAT, 4);
        hexagons[1][3] = new Hexagon (3, 1, WOOD, 6);
        hexagons[1][4] = new Hexagon (4, 1, WHEAT, 9);
        hexagons[1][5] = new Hexagon (5, 1, ANY_HARBOR);
        hexagons[1][6] = new Hexagon ();

        hexagons[2][0] = new Hexagon ();
        hexagons[2][1] = new Hexagon (1, 2, WOOD_HARBOR);
        hexagons[2][2] = new Hexagon (2, 2, CLAY, 2);
        hexagons[2][3] = new Hexagon (3, 2, WOOD, 5);
        hexagons[2][4] = new Hexagon (4, 2, WOOL, 12);
        hexagons[2][5] = new Hexagon (5, 2, WOOL, 4);
        hexagons[2][6] = new Hexagon (6, 2, WATER);

        hexagons[3][0] = new Hexagon (0, 3, WATER);
        hexagons[3][1] = new Hexagon (1, 3, WOOL, 9);
        hexagons[3][2] = new Hexagon (2, 3, CLAY, 8);
        hexagons[3][3] = new Hexagon (3, 3, DESERT);
        hexagons[3][4] = new Hexagon (4, 3, STONE, 8);
        hexagons[3][5] = new Hexagon (5, 3, WOOL, 10);
        hexagons[3][6] = new Hexagon (6, 3, ANY_HARBOR);

        hexagons[4][0] = new Hexagon ();
        hexagons[4][1] = new Hexagon (1, 4, ANY_HARBOR);
        hexagons[4][2] = new Hexagon (2, 4, WOOD, 3);
        hexagons[4][3] = new Hexagon (3, 4, STONE, 5);
        hexagons[4][4] = new Hexagon (4, 4, CLAY, 10);
        hexagons[4][5] = new Hexagon (5, 4, WOOD, 11);
        hexagons[4][6] = new Hexagon (6, 4, WATER);

        hexagons[5][0] = new Hexagon ();
        hexagons[5][1] = new Hexagon (1, 5, WATER);
        hexagons[5][2] = new Hexagon (2, 5, WHEAT, 3);
        hexagons[5][3] = new Hexagon (3, 5, WHEAT, 6);
        hexagons[5][4] = new Hexagon (4, 5, STONE, 11);
        hexagons[5][5] = new Hexagon (5, 5, WHEAT_HARBOR);
        hexagons[5][6] = new Hexagon ();

        hexagons[6][0] = new Hexagon ();
        hexagons[6][1] = new Hexagon ();
        hexagons[6][2] = new Hexagon (5, 0, CLAY_HARBOR);
        hexagons[6][3] = new Hexagon (5, 0, WATER);
        hexagons[6][4] = new Hexagon (5, 0, ANY_HARBOR);
        hexagons[6][5] = new Hexagon (5, 0, WATER);
        hexagons[6][6] = new Hexagon ();

        // Intersections
        intersections = new Intersection[8][16];

        for (int i = 0; i < 8; ++i) { //x
            for (int j = 0; j < 16; ++j) { //y
                Intersection intersection = new Intersection (i, j);

                for (Position position : intersection.getNeighborHexagonPositions ()) {
                    int pos_x = position.x;
                    int pos_y = position.y;

                    if (pos_x >= 0 && pos_x < 7 && pos_y >= 0 && pos_y < 7)
                        intersection.addNeighbour (hexagons[pos_x][pos_y]);
                }

                intersections[i][j] = intersection;
            }
        }

        roads = new Road[14][15];

        for (int i = 0; i < 14; ++i) { //x
            for (int j = 0; j < 15; ++j) { //y
                //Holes
                if (!((j % 2 == 0 && i % 4 == 3) ||  (j % 2 == 1 && i % 4 == 1))) {

                    Road road = new Road (i, j);

                    Position fromPos = road.getFromIntersectionPosition ();
                    Position toPos = road.getToIntersectionPosition ();

                    road.from = intersections[fromPos.x][fromPos.y];
                    road.to = intersections[toPos.x][toPos.y];

                    roads[i][j] = road;

                } else {
//                    System.out.println ("HOLE: " + i + " :: " + j); it seem to work
                }
            }
        }

        //Robber position

        robberPosition = hexagons[3][3];


    }

    void generateRandomMap () {

        generateConstants ();

        //Hexagons

        //Ground hexagons + intersections
        List<Hexagon> groundHexagons = new ArrayList<> ();
        for (int i = 0; i < 3; ++i)
            groundHexagons.add (new Hexagon (STONE));
        for (int i = 0; i < 3; ++i)
            groundHexagons.add (new Hexagon (CLAY));
        for (int i = 0; i < 4; ++i)
            groundHexagons.add (new Hexagon (WOOD));
        for (int i = 0; i < 4; ++i)
            groundHexagons.add (new Hexagon (WHEAT));
        for (int i = 0; i < 4; ++i)
            groundHexagons.add (new Hexagon (WOOL));

        Collections.shuffle (groundHexagons);

    }

    void startGame () {
        //Randomizing starting player

        firstPlayer = (int) (Math.random () * 4);
        lastPlayer = firstPlayer > 0 ? firstPlayer - 1 : 3;

        currentPlayer = firstPlayer;

        System.out.println ("Player " + currentPlayer + " chose your first village!");

        gameStance = GameStance.PICK_1ST_VILLAGE;
    }

    void nextPickTurn () {
        if (gameStance == GameStance.PRE_ROLL) {
            currentPlayer = firstPlayer;
            System.out.println ("Player " + currentPlayer + " starts its turn!");
        } else
        if (gameStance == GameStance.PICK_1ST_VILLAGE){
            currentPlayer = (currentPlayer + 1) % 4;

            //If the first player would come we set the last player
            if (currentPlayer == firstPlayer) {
                currentPlayer = lastPlayer;
                gameStance = GameStance.PICK_2ND_VILLAGE;

                System.out.println ("Player " + currentPlayer + " chose your second village!");

            } else {
                System.out.println ("Player " + currentPlayer + " chose your first village!");
            }
        } else
        if (gameStance == GameStance.PICK_2ND_VILLAGE){
            currentPlayer = currentPlayer > 0 ? currentPlayer - 1 : 3;

            //If the last player would come we set the first player
            if(currentPlayer == lastPlayer) {
                currentPlayer = firstPlayer;
                gameStance = GameStance.PRE_ROLL;
                System.out.println ("Player " + currentPlayer + " starts its turn!");
            } else {
                System.out.println ("Player " + currentPlayer + " chose your second village!");
            }
        }
    }

    void nextTurn () {
        System.out.println ("Player " + currentPlayer + " ends its turn!");

        currentPlayer = (currentPlayer + 1) % 4;

        gameStance = GameStance.PRE_ROLL;

        System.out.println ("Player " + currentPlayer + " starts its turn!");
    }

    List<Intersection> getNeighbourIntersections (Hexagon hexagon) {
        List<Intersection> ret = new ArrayList<> ();

        int x = hexagon.x;
        int y = hexagon.y;

        if (x % 2 == 0) {
            ret.add (intersections[y][2 * x + 0]);
            ret.add (intersections[y][2 * x + 1]);
            ret.add (intersections[y][2 * x + 2]);
            ret.add (intersections[y + 1][2 * x + 0]);
            ret.add (intersections[y + 1][2 * x + 1]);
            ret.add (intersections[y + 1][2 * x + 2]);
        } else {
            ret.add (intersections[y][2 * x + 1]);
            ret.add (intersections[y][2 * x + 2]);
            ret.add (intersections[y][2 * x + 3]);
            ret.add (intersections[y + 1][2 * x + 1]);
            ret.add (intersections[y + 1][2 * x + 2]);
            ret.add (intersections[y + 1][2 * x + 3]);

        }

        return ret;
    }

    boolean canDrawActionCard () {
        return !actionCardDeck.isEmpty ();
    }

    ActionCardType drawActionCard () {
        int lastIndex = actionCardDeck.size () - 1;

        ActionCard card = actionCardDeck.get (actionCardDeck.size () - 1);

        actionCardDeck.remove (lastIndex);

        return card.type;
    }

    void buildRoad (Road road) {
        routes.add (road);
    }//#TODO

    boolean canPlaceRobber (Hexagon hexagon) {
        return !(robberPosition == hexagon) && hexagon.canBeRobbed;
    }

    void robberMoves (Hexagon hexagon, boolean in) {
        robberPosition.setRobber (in);
        int minusOrPlus = (in) ? -1 : 1;

        List<Intersection> neighbors = getNeighbourIntersections (robberPosition);

        for (Intersection currentIntersection : neighbors) {

            int production = currentIntersection.getProduction ();
            int owner = currentIntersection.getOwner ();
            if (owner >= 0 && production > 0) {
                players.get (owner).setResourceGain (robberPosition.hexagonType.id,
                        production * minusOrPlus,
                        robberPosition.getRollNumber ());
            }
        }
    }

    void placeRobber (Hexagon hexagon, boolean isKnight) {
        if (canPlaceRobber (hexagon)) {
            // MOVING
            robberMoves (robberPosition, false);
            robberPosition = hexagon;
            robberMoves (hexagon, true);

            //STEALING
            //#TODO Steal specific person
            List<Intersection> neighbors = getNeighbourIntersections (hexagon);
            int rand = (int) (Math.random () * 6);
            for (int i = 0; i < 6; ++i) {
                int index = (i + rand) % 6;
                Intersection currentIntersection = neighbors.get (index);

                int owner = currentIntersection.getOwner ();
                if (owner >= 0) {
                    ResourceCardType type = players.get (owner).loseRandomResource (1);
                    players.get (currentPlayer).getResource (type, 1);
                    break;
                }
            }

            if (isKnight) {
                return;
            }

            //7+ # of cards in hand rule
            //#TODO Drop specific resources
            for (int i = 0; i < 4; i++) {
                if (players.get (i).getHandSize () > 7) {
                    players.get (i).loseRandomResource (players.get (i).getHandSize () / 2);
                }
            }
        }
    }

    void playMonopoly (int id, ResourceCardType type) {
        for (int i = 0; i < 4; i++) {
            if (i != id) {
                int num = players.get (i).getNumOfResourceType (type);
                if (num > 0) {
                    players.get (i).loseResource (type, num);
                    players.get (id).getResource (type, num);
                }
            }
        }
    }

    public void rollDice (int diceValue) {
        for (Player player : players) {
            for (ResourceCardType cardType : ResourceCardType.values ()) {
                int numOfRes = player.resourceGainingNumbers[diceValue][cardType.id];
                player.getResource (cardType, numOfRes);
            }
        }
    }

    public GameTable () {
        players = new ArrayList<> ();

        players.add (new Player (0));
        players.add (new Player (1));
        players.add (new Player (2));
        players.add (new Player (3));
    }

    public Player getCurrentPlayer () {
        return players.get (currentPlayer);
    }

    public Intersection parseIntersectionString (String intersectionString) {
        String[] coords = intersectionString.split ("-");
        int x = Integer.parseInt (coords[0]);
        int y = Integer.parseInt (coords[1]);

        return intersections[x][y];
    }

    //                                    LEFT
//            ______________________________________________________
//            |         _____         _____         _____
//            |        /     \       /     \       /     \
//            |  _____/ -2,-1 \_____/  0,-1 \_____/  2,-1 \_____
//            | /     \       /     \       /     \       /     \
//            |/ -3,-1 \_____/ -1,-1 \_____/  1,-1 \_____/  3,-1 \
//            |\       /     \       /     \       /     \       /
//            | \_____/ -2,0  \_____/  0,0  \_____/  2,0  \_____/
// BOT        | /     \       /     \       /     \       /     \           TOP
//            |/ -3,0  \_____/ -1,0  \_____/  1,0  \_____/  3,0  \
//            |\       /     \       /     \       /     \       /
//            | \_____/ -2,1  \_____/  0,1  \_____/  2,1  \_____/
//            | /     \       /     \       /     \       /     \
//            |/ -3,1  \_____/ -1,1  \_____/  1,1  \_____/  3,1  \
//            |\       /     \       /     \       /     \       /
//            | \_____/       \_____/       \_____/       \_____/
//
//                                   RIGHT
    public void draw () {
        AsciiBoard board = new AsciiBoard (0, 7, 0, 7, new LargeFlatAsciiHexPrinter ());
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                Hexagon cur = hexagons[j][i];

//                if (cur.hexagonType != NONE) {
                if (cur.equals (robberPosition)) {//#TODO
                    board.printHex (cur.hexagonType.name () + "®", j + "," + i, cur.hexagonType.fillerChar, j, i - j / 2);
                } else {
                    board.printHex (cur.hexagonType.name () + String.valueOf (cur.rollNumber), j + "," + i, cur.hexagonType.fillerChar, j, i - j / 2);
                }
//                }
            }
        }
        System.out.print (board.prettPrint (true));
    }
//    | = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |
//    |                                  _ _ _ _                                  |
//    |                                 / ~ ~ ~ \                                 |
//    |                                /~ ~ ~ ~ ~\                                |
//    |                       { }{}{}_/~ WATER-1 ~\_ _ _ _                        |
//    |                       / ~ ~ ~ {}   3,0   ~/ ~ ~ ~ \                       |
//    |                      /~ ~ ~ ~ ~{} ~ ~ ~ ~/~ ~ ~ ~ ~\                      |
//    |              _ _ _ _/~ WOOD_HA {V}~_~_~_/~ ANY_HAR ~\_ _ = _              |
//    |             / ~ ~ ~ \~   2,1   ~/ @ @ @ \~   4,1   ~/ ~ ~ ~ \             |
//    |            /~ ~ ~ ~ ~\~ ~ ~ ~ ~/@ @ @ @ @\~ ~ ~ ~ ~/~ ~ ~ ~ \\            |
//    |    _ _ _ _/~ WATER-1 ~\_~_~_~_/@  WOOL9  @\_~_~_~_/~ WATER-1 \\_ _ _ _    |
//    |   / ~ ~ ~ \~   1,1   ~/ = = = \@   3,1   @/ & & & \~   5,1   ~/ ~ ~ ~ \   |
//    |  /~ ~ ~ ~ ~\~ ~ ~ ~ ~/= = = = =\@ @ @ @ @/& & & & &\~ ~ ~ ~ ~// ~ ~ ~ ~\  |
//    | /~ STONE_H ~\_~_~_~_/=  CLAY2  =\_@_@_@_/&  WOOD3  &\_~_~_~_// CLAY_HA ~\ |
//    | \~   0,2   ~/ ! ! ! \=   2,2   =/ = = = \&   4,2   &/ ! ! ! \~   6,2   ~/ |
//    |  \~ ~ ~ ~ ~/! ! ! ! !\= = = = =/= = = = =\& & & & &/! ! ! ! \\~ ~ ~ ~ ~/  |
//    |   \_~_~_~_/! WHEAT4  !\_=_=_=_/=  CLAY8  =\_&_&_&_/! WHEAT3  \\_~_~_~_/   |
//    |   / ~ ~ ~ \!   1,2   !/ & & & \=   3,2   =/ # # # \!   5,2   !/ ~ ~ ~ \   |
//    |  /~ ~ ~ ~ ~\! ! ! ! !/& & & & &\= = = = =/# # # # #\! ! ! ! !// ~ ~ ~ ~\  |
//    | /~ WATER-1 ~\_!_!_!_/&  WOOD5  &\_=_=_=_/# STONE5  #\_!_!_!_// WATER-1 ~\ |
//    | \~   0,3   ~/ & & & \&   2,3   &/       \#   4,3   #/ ! ! ! \~   6,3   ~/ |
//    |  \~ ~ ~ ~ ~/& & & & &\& & & & &/         \# # # # #/! ! ! ! \\~ ~ ~ ~ ~/  |
//    |   \_~_~_~_/&  WOOD6  &\_&_&_&_/  DESERT®  \_#_#_#_/! WHEAT6  \\_~_~_~_/   |
//    |   / ~ ~ ~ \&   1,3   &/ @ @ @ \    3,3    / = = = \!   5,3   !/ ~ ~ ~ \   |
//    |  /~ ~ ~ ~ ~\& & & & &/@ @ @ @ @\         /= = = = =\! ! ! ! !// ~ ~ ~ ~\  |
//    | /~ WOOL_HA ~\_&_&_&_/@ WOOL12  @\_ _ _ _/= CLAY10  =\_!_!_!_// ANY_HAR ~\ |
//    | \~   0,4   ~/ ! ! ! \@   2,4   @/ # # # \=   4,4   =/ # # #\\~   6,4   ~/ |
//    |  \~ ~ ~ ~ ~/! ! ! ! !\@ @ @ @ @/# # # # #\= = = = =/# # # # \\~ ~ ~ ~ ~/  |
//    |   \_~_~_~_/! WHEAT9  !\_@_@_@_/# STONE8  #\_=_=_=_/# STONE11 #\_=_=_=_/   |
//    |   / ~ ~ ~ \!   1,4   !/ @ @ @ \#   3,4   #/ & & & \#   5,4   // ~ ~ ~ \   |
//    |  /~ ~ ~ ~ ~\! ! ! ! !/@ @ @ @ @\# # # # #/& & & & &\# # # # //~ ~ ~ ~ ~\  |
//    | /~ WATER-1 ~\_!_!_!_/@  WOOL4  @\_#_#_#_/& WOOD11  &[]#=#=#_/~ WATER-1 ~\ |
//    | \~   0,5   ~/ ~ ~ ~ \@   2,5   @/ @ @ @ \&   4,5   // ~ ~ ~ \~   6,5   ~/ |
//    |  \~ ~ ~ ~ ~/~ ~ ~ ~ ~\@ @ @ @ @/@ @ @ @ @\& & & & //~ ~ ~ ~ ~\~ ~ ~ ~ ~/  |
//    |   \_~_~_~_/~ ANY_HAR ~\_@_@_@_/@ WOOL10  @[]&=&=&_/~ WHEAT_H ~\_~_~_~_/   |
//    |           \~   1,5   ~/ ~ ~ ~ \\   3,5   // ~ ~ ~ \~   5,5   ~/           |
//    |            \~ ~ ~ ~ ~/~ ~ ~ ~ ~\\ @ @ @ //~ ~ ~ ~ ~\~ ~ ~ ~ ~/            |
//    |             \_~_~_~_/~ WATER-1 ~\_@=@=@_/~ WATER-1 ~\_~_~_~_/             |
//    |                     \~   2,6   ~/ ~ ~ ~ \\   4,6   ~/                     |
//    |                      \~ ~ ~ ~ ~/~ ~ ~ ~ ~\\ ~ ~ ~ ~/                      |
//    |                       \_~_~_~_/~ ANY_HAR ~[]~_~_~_/                       |
//    |                               \~   3,6   ~/                               |
//    |                                \~ ~ ~ ~ ~/                                |
//    |                                 \_~_~_~_/                                 |
//    |                                                                           |
//    | = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |

    // DICE
//     _ _ _  _ _ _  _ _ _  _ _ _  _ _ _  _ _ _
//    |     ||x    ||x    ||x   x||x   x||x   x|
//    |  x  ||     ||  x  ||     ||  x  ||x   x|
//    | _ _ || _ _x|| _ _x||x_ _x||x_ _x||x_ _x|
    String dice_1 = " _ _ _ \n" +
            "|     |\n" +
            "|  x  |\n" +
            "| _ _ |";
    String dice_2 = " _ _ _ \n" +
            "|x    |\n" +
            "|     |\n" +
            "| _ _x|";
    String dice_3 = " _ _ _ \n" +
            "|x    |\n" +
            "|  x  |\n" +
            "| _ _x|";
    String dice_4 = " _ _ _ \n" +
            "|x   x|\n" +
            "|     |\n" +
            "|x_ _x|";
    String dice_5 = " _ _ _ \n" +
            "|x   x|\n" +
            "|  x  |\n" +
            "|x_ _x|";
    String dice_6 = " _ _ _ \n" +
            "|x   x|\n" +
            "|x   x|\n" +
            "|x_ _x|";

    String[] dices = {dice_1, dice_2, dice_3, dice_4, dice_5, dice_6};

    public String getDiceString (int diceValue) {
        return dices[diceValue - 1];
    }

    public String getDiceString (int diceValue1, int diceValue2) {
        String[] diceString1 = getDiceString (diceValue1).split ("\n");
        String[] diceString2 = getDiceString (diceValue2).split ("\n");
        String retString = "";

        for (int i = 0; i < diceString1.length; i++) {
            retString += diceString1[i] + " " + diceString2[i] + "\n";
        }

        return retString;
    }

    public boolean isAvailableCommand(String command) {
        return gameStance.isAvailable (command) || err();
    }
    private boolean err() {
        System.out.println ("Not available command");
        return false;
    }
}
