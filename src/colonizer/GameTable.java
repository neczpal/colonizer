package colonizer;

import colonizer.enums.ActionCardType;
import colonizer.enums.GameStance;
import colonizer.enums.ResourceCardType;
import colonizer.map.Road;
import colonizer.enums.HexagonType;
import colonizer.map.*;
import dk.ilios.asciihexgrid.AsciiBoard;
import dk.ilios.asciihexgrid.printers.LargeFlatAsciiHexPrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static colonizer.enums.ActionCardType.*;

public class GameTable {


    //Map properties
    private Hexagon[][] hexagons;
    private Intersection[][] intersections;
    private Road[][] roads;
    private Hexagon robberPosition;

    //Card properties
    private List<ActionCard> actionCardDeck;
    private int[] resourceCards;

    //Player properties
    private List<Player> players;
    private int currentPlayer;
    private int firstPlayer, lastPlayer;

    //Game properties
    private GameStance gameStance = GameStance.PREPARING;
    private int turnNumber;
    private boolean endOfGame = false;


    public GameTable() {
        initResourceCards();
        initActionCards();
        initTestMap();
        initPlayers();
    }

    private void initResourceCards() {
        resourceCards = new int[5];
        resourceCards[ResourceCardType.RES_CLAY.id] = 19;
        resourceCards[ResourceCardType.RES_WOOD.id] = 19;
        resourceCards[ResourceCardType.RES_WOOL.id] = 19;
        resourceCards[ResourceCardType.RES_WHEAT.id] = 19;
        resourceCards[ResourceCardType.RES_STONE.id] = 19;
    }

    private void initActionCards() {
        actionCardDeck = new ArrayList<>();

        for (int i = 0; i < 14; ++i)
            actionCardDeck.add(new ActionCard(KNIGHT));
        for (int i = 0; i < 5; ++i)
            actionCardDeck.add(new ActionCard(POINT_CARD));
        for (int i = 0; i < 2; ++i)
            actionCardDeck.add(new ActionCard(MONOPOLY));
        for (int i = 0; i < 2; ++i)
            actionCardDeck.add(new ActionCard(YEAR_OF_PLENTY));
        for (int i = 0; i < 2; ++i)
            actionCardDeck.add(new ActionCard(ROUTE_BUILDING));

        Collections.shuffle(actionCardDeck);
    }

    private void initTestMap() {
        //Hexagons
        hexagons = new Hexagon[7][7];

        hexagons[0][0] = new Hexagon();
        hexagons[0][1] = new Hexagon();
        hexagons[0][2] = new Hexagon(0, 2, HexagonType.STONE_HARBOR);
        hexagons[0][3] = new Hexagon(0, 3, HexagonType.WATER);
        hexagons[0][4] = new Hexagon(0, 4, HexagonType.WOOL_HARBOR);
        hexagons[0][5] = new Hexagon(0, 5, HexagonType.WATER);
        hexagons[0][6] = new Hexagon();

        hexagons[1][0] = new Hexagon();
        hexagons[1][1] = new Hexagon(1, 1, HexagonType.WATER);
        hexagons[1][2] = new Hexagon(1, 2, HexagonType.WHEAT, 4);
        hexagons[1][3] = new Hexagon(1, 3, HexagonType.WOOD, 6);
        hexagons[1][4] = new Hexagon(1, 4, HexagonType.WHEAT, 9);
        hexagons[1][5] = new Hexagon(1, 5, HexagonType.ANY_HARBOR);
        hexagons[1][6] = new Hexagon();

        hexagons[2][0] = new Hexagon();
        hexagons[2][1] = new Hexagon(2, 1, HexagonType.WOOD_HARBOR);
        hexagons[2][2] = new Hexagon(2, 2, HexagonType.CLAY, 2);
        hexagons[2][3] = new Hexagon(2, 3, HexagonType.WOOD, 5);
        hexagons[2][4] = new Hexagon(2, 4, HexagonType.WOOL, 12);
        hexagons[2][5] = new Hexagon(2, 5, HexagonType.WOOL, 4);
        hexagons[2][6] = new Hexagon(2, 6, HexagonType.WATER);

        hexagons[3][0] = new Hexagon(3, 0, HexagonType.WATER);
        hexagons[3][1] = new Hexagon(3, 1, HexagonType.WOOL, 9);
        hexagons[3][2] = new Hexagon(3, 2, HexagonType.CLAY, 8);
        hexagons[3][3] = new Hexagon(3, 3, HexagonType.DESERT);
        hexagons[3][4] = new Hexagon(3, 4, HexagonType.STONE, 8);
        hexagons[3][5] = new Hexagon(3, 5, HexagonType.WOOL, 10);
        hexagons[3][6] = new Hexagon(3, 6, HexagonType.ANY_HARBOR);

        hexagons[4][0] = new Hexagon();
        hexagons[4][1] = new Hexagon(4, 1, HexagonType.ANY_HARBOR);
        hexagons[4][2] = new Hexagon(4, 2, HexagonType.WOOD, 3);
        hexagons[4][3] = new Hexagon(4, 3, HexagonType.STONE, 5);
        hexagons[4][4] = new Hexagon(4, 4, HexagonType.CLAY, 10);
        hexagons[4][5] = new Hexagon(4, 5, HexagonType.WOOD, 11);
        hexagons[4][6] = new Hexagon(4, 6, HexagonType.WATER);

        hexagons[5][0] = new Hexagon();
        hexagons[5][1] = new Hexagon(5, 1, HexagonType.WATER);
        hexagons[5][2] = new Hexagon(5, 2, HexagonType.WHEAT, 3);
        hexagons[5][3] = new Hexagon(5, 3, HexagonType.WHEAT, 6);
        hexagons[5][4] = new Hexagon(5, 4, HexagonType.STONE, 11);
        hexagons[5][5] = new Hexagon(5, 5, HexagonType.WHEAT_HARBOR);
        hexagons[5][6] = new Hexagon();

        hexagons[6][0] = new Hexagon();
        hexagons[6][1] = new Hexagon();
        hexagons[6][2] = new Hexagon(6, 2, HexagonType.CLAY_HARBOR);
        hexagons[6][3] = new Hexagon(6, 3, HexagonType.WATER);
        hexagons[6][4] = new Hexagon(6, 4, HexagonType.ANY_HARBOR);
        hexagons[6][5] = new Hexagon(6, 5, HexagonType.WATER);
        hexagons[6][6] = new Hexagon();

        // Intersections
        intersections = new Intersection[8][16];

        for (int i = 0; i < 8; ++i) { //x
            for (int j = 0; j < 16; ++j) { //y
                intersections[i][j] = new Intersection(i, j);
            }
        }

        roads = new Road[14][15];

        for (int i = 0; i < 14; ++i) { //x
            for (int j = 0; j < 15; ++j) { //y
                //Holes
                if (!((j % 2 == 0 && i % 4 == 3) || (j % 2 == 1 && i % 4 == 1))) {
                    roads[i][j] = new Road(i, j);
                }
            }
        }

        //Robber position

        robberPosition = hexagons[3][3];

        initConnections();
    }

    private void initConnections() {
        //INTERSECTIONS :
        for (int i = 0; i < 8; ++i) { //x
            for (int j = 0; j < 16; ++j) { //y
                Intersection intersection = intersections[i][j];

                for (Position position : intersection.getNeighborHexagonPositions()) {
                    int pos_x = position.x;
                    int pos_y = position.y;

                    if (pos_x >= 0 && pos_x < 7 && pos_y >= 0 && pos_y < 7) {
                        intersection.addNeighborHexagon(hexagons[pos_x][pos_y]);
                    }
                }
                for (Position position : intersection.getNeighborIntersectionPositions()) {
                    int pos_x = position.x;
                    int pos_y = position.y;

                    if (pos_x >= 0 && pos_x < 7 && pos_y >= 0 && pos_y < 15) {
                        intersection.addNeighborIntersection(intersections[pos_x][pos_y]);
                    }
                }

                for (Position position : intersection.getNeighborRoadPositions()) {
                    int pos_x = position.x;
                    int pos_y = position.y;

                    if (pos_x >= 0 && pos_x < 13 && pos_y >= 0 && pos_y < 14) {
                        intersection.addNeighborRoad(roads[pos_x][pos_y]);
                    }
                }
            }
        }
        //HEXAGONS :
        for (int i = 0; i < 7; ++i) { //x
            for (int j = 0; j < 7; ++j) { //y
                Hexagon hexagon = hexagons[i][j];

                for (Position position : hexagon.getNeighborRoadPositions()) {
                    int pos_x = position.x;
                    int pos_y = position.y;

                    if (pos_x >= 0 && pos_x < 13 && pos_y >= 0 && pos_y < 14) {
                        hexagon.addNeighborRoad(roads[pos_x][pos_y]);
                    }
                }
                for (Position position : hexagon.getNeighborIntersectionPositions()) {
                    int pos_x = position.x;
                    int pos_y = position.y;

                    if (pos_x >= 0 && pos_x < 13 && pos_y >= 0 && pos_y < 14) {
                        hexagon.addNeighborIntersection(intersections[pos_x][pos_y]);
                    }
                }
            }
        }
        //ROADS :
        for (int i = 0; i < 14; ++i) { //x
            for (int j = 0; j < 15; ++j) { //y
                //Holes
                if (roads[i][j] != null) {

                    Road road = roads[i][j];

                    Position fromPos = road.getFromIntersectionPosition();
                    Position toPos = road.getToIntersectionPosition();

                    road.from = intersections[fromPos.x][fromPos.y];
                    road.to = intersections[toPos.x][toPos.y];
                }
            }
        }
    }

    //#TODO random map
    private void initRandomMap() {

        initActionCards();

        //Hexagons

        //Ground hexagons + intersections
        List<Hexagon> groundHexagons = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
            groundHexagons.add(new Hexagon(HexagonType.STONE));
        for (int i = 0; i < 3; ++i)
            groundHexagons.add(new Hexagon(HexagonType.CLAY));
        for (int i = 0; i < 4; ++i)
            groundHexagons.add(new Hexagon(HexagonType.WOOD));
        for (int i = 0; i < 4; ++i)
            groundHexagons.add(new Hexagon(HexagonType.WHEAT));
        for (int i = 0; i < 4; ++i)
            groundHexagons.add(new Hexagon(HexagonType.WOOL));

        Collections.shuffle(groundHexagons);

    }

    private void initPlayers() {
        players = new ArrayList<>();

        players.add(new Player(0, this));
        players.add (new Player (1, this));
//        players.add (new Player (2));
//        players.add (new Player (3));

        //Randomizing starting player

        firstPlayer = (int) (Math.random() * players.size());
        lastPlayer = firstPlayer > 0 ? firstPlayer - 1 : players.size() - 1;

        currentPlayer = firstPlayer;

        gameStance = GameStance.PREPARING;
        turnNumber = 0;
    }

    //@depricated
    void startGame() {
        //Randomizing starting player

        firstPlayer = (int) (Math.random() * players.size());
        lastPlayer = firstPlayer > 0 ? firstPlayer - 1 : players.size() - 1;

        currentPlayer = firstPlayer;

        System.out.println("Player " + currentPlayer + " chose your first village!");

        gameStance = GameStance.PICK_1ST_VILLAGE;
    }

    void nextPickTurn() {
        if (gameStance == GameStance.PRE_ROLL) {
            currentPlayer = firstPlayer;
            System.out.println("Player " + currentPlayer + " starts its turn!");
        } else if (gameStance == GameStance.PICK_1ST_VILLAGE) {
            currentPlayer = (currentPlayer + 1) % players.size();

            //If the first player would come we set the last player
            if (currentPlayer == firstPlayer) {
                currentPlayer = lastPlayer;
                gameStance = GameStance.PICK_2ND_VILLAGE;

                System.out.println("Player " + currentPlayer + " chose your second village!");

            } else {
                System.out.println("Player " + currentPlayer + " chose your first village!");
            }
        } else if (gameStance == GameStance.PICK_2ND_VILLAGE) {
            currentPlayer = currentPlayer > 0 ? currentPlayer - 1 : players.size() - 1;

            //If the last player would come we set the first player
            if (currentPlayer == lastPlayer) {
                currentPlayer = firstPlayer;
                gameStance = GameStance.PRE_ROLL;
                System.out.println("Player " + currentPlayer + " starts its turn!");
            } else {
                System.out.println("Player " + currentPlayer + " chose your second village!");
            }
        }
    }

    void nextTurn() {
        System.out.println("Player " + currentPlayer + " ends its turn!");

        if (getCurrentPlayer().getPoints() >= 10) {
            System.out.println("Player " + currentPlayer + " won the game!");
            System.exit(0);
        }


        currentPlayer = (currentPlayer + 1) % players.size();

        gameStance = GameStance.PRE_ROLL;

        System.out.println("Player " + currentPlayer + " starts its turn!");
    }

    //@depracated
    List<Intersection> getNeighborIntersections(Hexagon hexagon) {
        List<Intersection> ret = new ArrayList<>();

        int x = hexagon.x;
        int y = hexagon.y;

        if (x % 2 == 0) {
            ret.add(intersections[y][2 * x + 0]);
            ret.add(intersections[y][2 * x + 1]);
            ret.add(intersections[y][2 * x + 2]);
            ret.add(intersections[y + 1][2 * x + 0]);
            ret.add(intersections[y + 1][2 * x + 1]);
            ret.add(intersections[y + 1][2 * x + 2]);
        } else {
            ret.add(intersections[y][2 * x + 1]);
            ret.add(intersections[y][2 * x + 2]);
            ret.add(intersections[y][2 * x + 3]);
            ret.add(intersections[y + 1][2 * x + 1]);
            ret.add(intersections[y + 1][2 * x + 2]);
            ret.add(intersections[y + 1][2 * x + 3]);

        }

        return ret;
    }

    boolean canDrawActionCard() {
        return !actionCardDeck.isEmpty();
    }

    ActionCardType drawActionCard() {
        int lastIndex = actionCardDeck.size() - 1;

        ActionCard card = actionCardDeck.get(actionCardDeck.size() - 1);

        actionCardDeck.remove(lastIndex);

        return card.type;
    }

    public void drawResourceCard(int id) {
        resourceCards[id]--;
    }

    public void drawResourceCard(int id, int num) {
        resourceCards[id] -= num;
    }

    boolean canPlaceRobber(Hexagon hexagon) {
        return !(robberPosition == hexagon) && hexagon.canBeRobbed;
    }

    void robberMoves(Hexagon hexagon, boolean in) {
        robberPosition.setRobber(in);
        int minusOrPlus = (in) ? -1 : 1;

        List<Intersection> neighbors = getNeighborIntersections(robberPosition);

        for (Intersection currentIntersection : neighbors) {

            int production = currentIntersection.getProduction();
            int owner = currentIntersection.getOwner();
            if (owner >= 0 && production > 0) {
                players.get(owner).setResourceGain(robberPosition.hexagonType.id,
                        production * minusOrPlus,
                        robberPosition.getRollNumber());
            }
        }
    }

    void placeRobber(Hexagon hexagon, boolean isKnight) {
        if (canPlaceRobber(hexagon)) {
            // MOVING
            robberMoves(robberPosition, false);
            robberPosition = hexagon;
            robberMoves(hexagon, true);

            gameStance = GameStance.POST_ROLL;

            //STEALING
            //#TODO Steal specific person
            List<Intersection> neighbors = getNeighborIntersections(hexagon);
            int rand = (int) (Math.random() * 6);
            for (int i = 0; i < 6; ++i) {
                int index = (i + rand) % 6;
                Intersection currentIntersection = neighbors.get(index);

                int owner = currentIntersection.getOwner();
                if (owner >= 0) {
                    ResourceCardType type = players.get(owner).loseRandomResource();
                    players.get(currentPlayer).getResource(type, 1);
                    break;
                }
            }

            if (isKnight) {
                return;
            }

            //7+ # of cards in hand rule
            //#TODO Drop specific resources
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getHandSize() > 7) {
                    players.get(i).loseRandomResource(players.get(i).getHandSize() / 2);
                }
            }
        }
    }

    void playMonopoly(int id, ResourceCardType type) {
        for (int i = 0; i < players.size(); i++) {
            if (i != id) {
                int num = players.get(i).getNumOfResourceType(type);
                if (num > 0) {
                    players.get(i).loseResource(type, num);
                    players.get(id).getResource(type, num);
                }
            }
        }
    }

    public void rollDice(int diceValue) {
        for (Player player : players) {
            for (ResourceCardType cardType : ResourceCardType.values()) {
                int numOfRes = player.resourceGainingNumbers[diceValue][cardType.id];

                drawResourceCard(cardType.id, numOfRes);
                player.getResource(cardType, numOfRes);
            }
        }

        gameStance = diceValue == 7 ? GameStance.ROBBER_MOVE : GameStance.POST_ROLL;
    }

    //Moving cards back to the bank
    public void pay(int[] res) {
        for(int i = 0; i < 5; ++i) {
            resourceCards[i] += res[i];
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public Intersection parseIntersectionPosition(Position pos) {
        return intersections[pos.x][pos.y];
    }
    public Road parseRoadPosition(Position pos) {
        return roads[pos.x][pos.y];
    }

    public Intersection parseIntersectionString(String intersectionString) {
        String[] coords = intersectionString.split("-");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        return intersections[x][y];
    }

    public Hexagon parseHexagonString(String hexagonString) {
        String[] coords = hexagonString.split("-");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        return hexagons[x][y];
    }

    public ActionCardType parseActionCardString(String actionCard) {
        return ActionCardType.parseString(actionCard);
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
    public void draw() {
        AsciiBoard board = new AsciiBoard(0, 7, 0, 7, new LargeFlatAsciiHexPrinter());
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                Hexagon cur = hexagons[j][i];

                if (cur.hexagonType != HexagonType.NONE) {
                    if (cur.equals(robberPosition)) {//#TODO
                        board.printHex(cur.hexagonType.name() + "®", j + "," + i, cur.hexagonType.fillerChar, j, i - j / 2);
                    } else {
                        board.printHex(cur.hexagonType.name() + String.valueOf(cur.rollNumber), j + "," + i, cur.hexagonType.fillerChar, j, i - j / 2);
                    }
                }
            }
        }
        System.out.print(board.prettPrint(true));
    }

    public void drawMine(Player player, boolean prodOrGain) {
        AsciiBoard board = new AsciiBoard(0, 7, 0, 7, new LargeFlatAsciiHexPrinter());
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                Hexagon cur = hexagons[j][i];

                int sumProduction = 0;

                for (Intersection placedVillages : player.placedVillages) {
                    if (placedVillages.getNeighborHexagons().contains(cur)) {
                        if (cur.canBeRobbed) {
                            if (prodOrGain)
                                sumProduction += 1;
                            else
                                sumProduction += (6 - Math.abs(cur.rollNumber - 7));
                        }
                    }
                }
                for (Intersection placedCities : player.placedCities) {
                    if (placedCities.getNeighborHexagons().contains(cur)) {
                        if (cur.canBeRobbed) {
                            if (prodOrGain)
                                sumProduction += 1;
                            else
                                sumProduction += (6 - Math.abs(cur.rollNumber - 7)) * 2;
                        }
                    }
                }

                if (cur.hexagonType != HexagonType.NONE) {
                    if (cur.equals(robberPosition)) {//#TODO
                        board.printHex(cur.hexagonType.name() + "®", String.valueOf(sumProduction), cur.hexagonType.fillerChar, j, i - j / 2);
                    } else {
                        board.printHex(cur.hexagonType.name() + String.valueOf(cur.rollNumber), String.valueOf(sumProduction), cur.hexagonType.fillerChar, j, i - j / 2);
                    }
                }
            }
        }
        System.out.print(board.prettPrint(true));
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

    public String getDiceString(int diceValue) {
        return dices[diceValue - 1];
    }

    public String getDiceString(int diceValue1, int diceValue2) {
        String[] diceString1 = getDiceString(diceValue1).split("\n");
        String[] diceString2 = getDiceString(diceValue2).split("\n");
        String retString = "";

        for (int i = 0; i < diceString1.length; i++) {
            retString += diceString1[i] + " " + diceString2[i] + "\n";
        }
        retString += "      (" + (diceValue1 + diceValue2) + ")";

        return retString;
    }

    public boolean isAvailableCommand(String command) {
        return gameStance.isAvailable(command) || err();
    }

    public GameStance getGameStance() {
        return gameStance;
    }

    private boolean err() {
        System.out.println("Not available command");
        return false;
    }

}
