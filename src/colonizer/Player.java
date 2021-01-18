package colonizer;

import colonizer.enums.ActionCardType;
import colonizer.enums.HexagonType;
import colonizer.enums.ResourceCardType;
import colonizer.map.Hexagon;
import colonizer.map.Intersection;
import colonizer.map.Road;

import java.util.ArrayList;
import java.util.List;

import static colonizer.enums.ActionCardType.YEAR_OF_PLENTY;

public class Player {
    // Village - 🏠
    // City - 🏭


    // Player 1 : (), (⌂)
    // Player 2 : []
    // Player 3 : {}
    // Player 4 : <>

    // AI / sysinput Player
//    boolean isHuman;
//    PlayerAI ai;

    int id;

    //    Game* game;
    GameTable table;

    // Remaining

    //#toDO seems redundant
    int leftRoads;
    int leftVillages;
    int leftCities;

    // Placed
    List<Road> placedRoads;
    List<Intersection> placedVillages;
    List<Intersection> placedCities;

    // Longest, Biggest

    int longestRoute;
    int playedKnights;

    boolean hasLongestRoute;
    boolean hasBiggestArmy;

    // Resources

    int[] resources = new int[5];

    // Trading

    boolean[] canTrade2for1 = new boolean[5];
    boolean canTrade3for1;

    // Action Cards

    int[] actionCards = new int[5];

    // Resource gain
    /*
     * 2 - 0 -> WOOD PRODUCTION IF 2 is rolled
     */

    int[][] resourceGainingNumbers = new int[13][5];
    // Number of options that can give u the specific resource
    /*
     * U have a village in 2-WOOD, 6-WOOL, 10-STONE place
     * -> WOOD: 1, WOOL: 5, STONE: 3
     */
    int[] resourceGain = new int[5];

    // Points

    int calculatedPoints; // #mb function?

    Player (int id, GameTable gameTable) {
        this.id = id;
        this.table = gameTable;

        leftRoads = 15;
        leftVillages = 5;
        leftCities = 4;

        resources[ResourceCardType.RES_WOOD.id] = 0;
        resources[ResourceCardType.RES_WOOL.id] = 0;
        resources[ResourceCardType.RES_WHEAT.id] = 0;
        resources[ResourceCardType.RES_STONE.id] = 0;
        resources[ResourceCardType.RES_CLAY.id] = 0;

        canTrade2for1[ResourceCardType.RES_WOOD.id] = false;
        canTrade2for1[ResourceCardType.RES_WOOL.id] = false;
        canTrade2for1[ResourceCardType.RES_WHEAT.id] = false;
        canTrade2for1[ResourceCardType.RES_STONE.id] = false;
        canTrade2for1[ResourceCardType.RES_CLAY.id] = false;

        canTrade3for1 = false;

        actionCards[ActionCardType.KNIGHT.id] = 0;
        actionCards[YEAR_OF_PLENTY.id] = 0;
        actionCards[ActionCardType.MONOPOLY.id] = 0;
        actionCards[ActionCardType.ROUTE_BUILDING.id] = 0;
        actionCards[ActionCardType.POINT_CARD.id] = 0;

        for (int i = 0; i < 13; ++i)
            for (int j = 0; j < 5; ++j)
                resourceGainingNumbers[i][j] = 0;

        resourceGain[ResourceCardType.RES_WOOD.id] = 0;
        resourceGain[ResourceCardType.RES_WOOL.id] = 0;
        resourceGain[ResourceCardType.RES_WHEAT.id] = 0;
        resourceGain[ResourceCardType.RES_STONE.id] = 0;
        resourceGain[ResourceCardType.RES_CLAY.id] = 0;

        placedVillages = new ArrayList<>();
        placedCities = new ArrayList<>();

    }

    //Not actual action
    final static int[] VILLAGE_COST = {1, 1, 1, 1, 0};
    final static int[] CITY_COST    = {0, 0, 0, 2, 3};
    final static int[] ROAD_COST    = {1, 1, 0, 0, 0};
    final static int[] CARD_COST    = {0, 0, 1, 1, 1};

    static int[] NUM_OF (ResourceCardType type, int num) {
        int[] ret = {0, 0, 0, 0, 0};
        ret[type.id] = num;
        return ret;
    }
    static int[] FOUR_OF (ResourceCardType type) {
        return NUM_OF (type, 4);
    }

    static int[] THREE_OF (ResourceCardType type) {
        return NUM_OF (type, 3);
    }

    static int[] TWO_OF (ResourceCardType type) {
        return NUM_OF (type, 2);
    }


    boolean canPay (int[] cost) {
        for(int i = 0; i < 5; ++i) {
            if(resources[i] < cost[i])
                return false;
        }
        return true;
    }
    void pay (int[] cost) {
        table.pay(cost);
        for(int i = 0; i < 5; ++i) {
            resources[i] -= cost[i];
        }
    }
    void setResourceGain (int type, int production, int rollNumber) {
        resourceGainingNumbers[rollNumber][type] += production;
        // 2 - 1 ... 6 - 5 7 - 6 8 - 5 ... 12 - 1
        // 6 - |rollnumber - 7|
        resourceGain[type] += (6 - Math.abs(rollNumber - 7)) * production;
    }

    int getHandSize () {
        return resources[0] + resources[1] + resources[2] + resources[3] + resources[4];
    }
    int getNumOfResourceType (ResourceCardType type) {
        return resources[type.id];
    }

    // ACTIONS

    void pickStartingVillage (Intersection intersection) {
        if (leftVillages > 0 && intersection.canPickVillage(id)) {
            intersection.buildVillage(id);
            placedVillages.add(intersection);
            leftVillages -= 1;

            //AKA Second starting village
            if(leftVillages == 3) {
                for(Hexagon hexagon : intersection.getNeighborHexagons()) {
                    if (hexagon.rollNumber >= 2) {
                        getResource (ResourceCardType.values ()[hexagon.hexagonType.id], 1);
                    }
                }
            }

            for(Hexagon current : intersection.getNeighborHexagons()) {
                if (current.hexagonType == HexagonType.ANY_HARBOR) {
                    canTrade3for1 = true;
                } else
                if (20 <= current.hexagonType.id && current.hexagonType.id <= 24) {
                    canTrade2for1[current.hexagonType.id-20] = true;
                } else
                if(current.rollNumber >= 2 && !current.isRobbed) {
                    setResourceGain(current.hexagonType.id, 1, current.rollNumber);
                }
            }
        }
    }

    // Build
    void buildVillage (Intersection intersection) {
        if (canPay(VILLAGE_COST) && leftVillages > 0 && intersection.canBuildVillage(id)) {
            pay(VILLAGE_COST);
            intersection.buildVillage(id);
            leftVillages -= 1;
            placedVillages.add(intersection);

            for(Hexagon current : intersection.getNeighborHexagons()) {
                if (current.hexagonType == HexagonType.ANY_HARBOR) {
                    canTrade3for1 = true;
                } else
                if (20 <= current.hexagonType.id && current.hexagonType.id <= 24) {
                    canTrade2for1[current.hexagonType.id-20] = true;
                } else
                if(current.rollNumber >= 2 && !current.isRobbed) {
                    setResourceGain(current.hexagonType.id, 1, current.rollNumber);
                }
            }
        }
    }
    void buildCity (Intersection intersection) {
        if (canPay(CITY_COST) && leftCities > 0 && intersection.canBuildCity(id)) {
            pay(CITY_COST);
            intersection.buildCity(id);
            leftCities -= 1;
            leftVillages += 1;
            placedVillages.remove(intersection);
            placedCities.add(intersection);

            for(Hexagon current : intersection.getNeighborHexagons()) {
                if(current.rollNumber >= 2 && !current.isRobbed) {
                    setResourceGain(current.hexagonType.id, 1, current.rollNumber);
                }
            }
        }
    }
    void buildRoad (Road road) {
        if (canPay(ROAD_COST) && leftRoads > 0 && road.canBuildRoad(id)) {
            pay(ROAD_COST);
            road.buildRoad(id);
            leftRoads -= 1;
        }
    }
    // Buy
    void buyActionCard () {
        if (canPay(CARD_COST) && leftRoads > 0 && table.canDrawActionCard()) {
            pay(CARD_COST);
            actionCards[table.drawActionCard().id] += 1;
        }
    }

    // Trade
    void trade4for1 (ResourceCardType from, ResourceCardType to) {
        int[] cost = FOUR_OF(from);

        if (canPay(cost)) {
            pay(cost);
            table.drawResourceCard(to.id);
            resources[to.id] ++;//#TODO track resouce cards
        }
    }

    void trade3for1 (ResourceCardType from, ResourceCardType to) {
        int[] cost = THREE_OF(from);

        if (canPay(cost) && canTrade3for1) {
            pay(cost);
            table.drawResourceCard(to.id);
            resources[to.id] ++;
        }
    }

    void trade2for1 (ResourceCardType from, ResourceCardType to) {
        int[] cost = TWO_OF(from);

        if (canPay(cost) && canTrade2for1[to.id]) {
            pay(cost);
            table.drawResourceCard(to.id);
            resources[to.id] ++;
        }
    }

    // Play action card
    void playKnight (Hexagon to){
        table.placeRobber(to, true);
    }
    void playYearOfPlenty (ResourceCardType one, ResourceCardType two) {
        if (actionCards[YEAR_OF_PLENTY.id] > 0) {
            actionCards[YEAR_OF_PLENTY.id] -= 1;
            resources[one.id] ++;
            resources[two.id] ++;
        }
    }

    void playRoadBuilding (Intersection from, Intersection to){
//        table.buildRoad(new Road(from, to));
        //#TODO doubleroad
    }
    void playMonopoly (ResourceCardType type){
        table.playMonopoly(id, type);
    }
    // PASS
    void pass () {
        table.nextTurn();
    }

//    void rollDice () {
//        table.rollDice();
//    }


    // Forced Action
    void loseResource (ResourceCardType type, int num){
        resources[type.id] -= num;
    }

    ResourceCardType loseRandomResource (){
        int res_index;
        do {
            res_index = (int) (Math.random() * ResourceCardType.values().length);
        } while(resources[res_index] <= 0);

        resources[res_index] -= 1;

        return ResourceCardType.values()[res_index];
    }

    void loseRandomResource (int num) {
        while (getHandSize() > num) {//Not the nicest solution
            int res_index = (int) (Math.random() * ResourceCardType.values().length);

            if (resources[res_index] <= 0) {
                continue;
            }

            resources[res_index] -= 1;
        }
    }

    void getResource (ResourceCardType type, int num) {
        resources[type.id] += num;
    }
    void dropResources (int[] numOfResources) {
        pay (numOfResources);//#TODO
    }

    void choseRobberPlace (Hexagon to){
        table.placeRobber(to, false);
    }

    public int getPoints() {
        return placedVillages.size() * 1 +
                placedCities.size() * 2 +
                actionCards[ActionCardType.POINT_CARD.id] +
                (hasLongestRoute ? 2 : 0) +
                (hasBiggestArmy ? 2 : 0);
    }
//    void tradeWithAnotherPlayer(); #TODO

}
