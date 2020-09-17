package colonizer.map;

import static colonizer.enums.Direction.*;

import colonizer.enums.Direction;
import colonizer.enums.NodeType;

import java.util.ArrayList;
import java.util.List;

public class Intersection extends Position {

    int owned = -1;
    int production = 0;// 1 - Village, 2 - City

    private boolean isEmpty;
    private final List<Direction> availableIntersectionDirections = new ArrayList<> ();
    private final List<Direction> availableHexagonDirections = new ArrayList<> ();

    private final List<Position> neighborIntersectionPositions = new ArrayList<> ();
    private final List<Position> neighborHexagonPositions = new ArrayList<> ();
    private final List<Position> neighborRoadPositions = new ArrayList<> ();

    public List<Hexagon> neighborIntersections = new ArrayList<> ();
    public List<Hexagon> neighborHexagons = new ArrayList<> ();
    public List<Hexagon> neighborRoads = new ArrayList<> ();


    Intersection () {
        super (-1, 0, 0, NodeType.POINT);
        isEmpty = false;
    }

    public Intersection (int x, int y) {
        super (-1, x, y, NodeType.POINT);
        isEmpty = true;

        if (isCoordSumEven ()) {
            Position rightIntersection = getPositionInDirection (RIGHT);
            Position topLeftIntersection = getPositionInDirection (TOP_LEFT);
            Position botLeftIntersection = getPositionInDirection (BOT_LEFT);

            Position rightRoad = getRoadBetweenIntersection (rightIntersection);
            Position topLeftRoad = getRoadBetweenIntersection (topLeftIntersection);
            Position botLeftRoad = getRoadBetweenIntersection (botLeftIntersection);

            availableIntersectionDirections.add (RIGHT);
            neighborIntersectionPositions.add (rightIntersection);
            neighborRoadPositions.add (rightRoad);

            availableIntersectionDirections.add (TOP_LEFT);
            neighborIntersectionPositions.add (topLeftIntersection);
            neighborRoadPositions.add (topLeftRoad);

            availableIntersectionDirections.add (BOT_LEFT);
            neighborIntersectionPositions.add (botLeftIntersection);
            neighborRoadPositions.add (botLeftRoad);

            availableHexagonDirections.add (LEFT);
            availableHexagonDirections.add (TOP_RIGHT);
            availableHexagonDirections.add (BOT_RIGHT);

            if (x % 2 == 0) {
                neighborHexagonPositions.add (new Position (x - 1, (y - 2) / 2));
                neighborHexagonPositions.add (new Position (x, (y - 2) / 2));
                neighborHexagonPositions.add (new Position (x, y / 2));
            } else {
                neighborHexagonPositions.add (new Position (x - 1, (y - 1) / 2));
                neighborHexagonPositions.add (new Position (x, (y - 3) / 2));
                neighborHexagonPositions.add (new Position (x, (y - 1) / 2));
            }

        } else {
            Position leftIntersection = getPositionInDirection (LEFT);
            Position topRightIntersection = getPositionInDirection (TOP_RIGHT);
            Position botRightIntersection = getPositionInDirection (BOT_RIGHT);

            Position leftRoad = getRoadBetweenIntersection (leftIntersection);
            Position topRightRoad = getRoadBetweenIntersection (topRightIntersection);
            Position botRightRoad = getRoadBetweenIntersection (botRightIntersection);
            
            availableIntersectionDirections.add (LEFT);
            neighborIntersectionPositions.add (leftIntersection);
            neighborRoadPositions.add (leftRoad);

            availableIntersectionDirections.add (TOP_RIGHT);
            neighborIntersectionPositions.add (topRightIntersection);
            neighborRoadPositions.add (topRightRoad);

            availableIntersectionDirections.add (BOT_RIGHT);
            neighborIntersectionPositions.add (botRightIntersection);
            neighborRoadPositions.add (botRightRoad);

            availableHexagonDirections.add (RIGHT);
            availableHexagonDirections.add (BOT_RIGHT);
            availableHexagonDirections.add (BOT_LEFT);

            if (x % 2 == 0) {
                neighborHexagonPositions.add (new Position (x, (y - 1) / 2));
                neighborHexagonPositions.add (new Position (x - 1, (y - 3) / 2));
                neighborHexagonPositions.add (new Position (x - 1, (y - 1) / 2));
            } else {
                neighborHexagonPositions.add (new Position (x, (y - 2) / 2));
                neighborHexagonPositions.add (new Position (x - 1, (y - 2) / 2));
                neighborHexagonPositions.add (new Position (x - 1, y / 2));
            }
        }
    }

    private Position getRoadBetweenIntersection (Position other) {
        if (x == other.x) {
            int min_y = Math.min (y, other.y);
            return new Position (2 * x, min_y);
        } else if (y == other.y) {
            int min_x = Math.min (x, other.x);
            return new Position (2 * min_x + 1, y);
        } else {
            System.out.println ("something is bad");
            return new Position (-1, -1);
        }
    }

    public void buildVillage (int playerId) {
        owned = playerId;
        production = 1;
    }

    public void buildCity (int playerId) {
        production = 2;
    }

    public void addNeighbour (Hexagon neighbor) {
        neighborHexagons.add (neighbor);
    }

    public boolean canBuildVillage () {
        return isEmpty && noNeighboorRule ();
    }

    public boolean canBuildCity (int playerId) {
        return owned == playerId;
    }

    public int getProduction () {
        return production;
    }

    public int getOwner () {
        return owned;
    }

    public boolean isCoordSumEven () {
        return (x + y) % 2 == 0;
    }

    public List<Direction> getAvailableHexagonDirections () {
        return availableHexagonDirections;
    }

    public List<Direction> getAvailableIntersectionDirections () {
        return availableHexagonDirections;
    }

    public List<Position> getNeighborHexagonPositions () {
        return neighborHexagonPositions;
    }

    public List<Position> getNeighborIntersectionPositions () {
        return neighborIntersectionPositions;
    }


    private boolean noNeighboorRule () {
        return true;//#TODOOO
    }
}
