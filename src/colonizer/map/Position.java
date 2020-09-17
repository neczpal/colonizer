package colonizer.map;

import colonizer.enums.Direction;
import colonizer.enums.NodeType;

import java.util.Objects;

public class Position {
    public int id = -1;
    public int x, y;
    public NodeType nodeType = NodeType.POINT;

    Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    Position (int id, int x, int y, NodeType nodeType) {
        this (x, y);
        this.id = id;
        this.nodeType = nodeType;
    }

    public Position getPositionInDirection (Direction dir) {
        switch (nodeType) {
            case HEXAGON:
                return moveToHexagon (dir);
            case POINT:
            default:
                return moveToTriangle (dir);
        }
    }

    private Position moveToHexagon (Direction dir) {
        //SHARED PART
        switch (dir) {
            case TOP:
                return new Position (x, y - 1);
            case BOT:
                return new Position (x, y + 1);
        }

        //EVEN COLUMN
        if (x % 2 == 0) {
            switch (dir) {
                case TOP_LEFT:
                    return new Position (x - 1, y - 1);
                case TOP_RIGHT:
                    return new Position (x + 1, y - 1);
                case BOT_LEFT:
                    return new Position (x - 1, y);
                case BOT_RIGHT:
                    return new Position (x + 1, y);
            }
        }
        //ODD COLUMN
        else {
            switch (dir) {
                case TOP_LEFT:
                    return new Position (x - 1, y);
                case TOP_RIGHT:
                    return new Position (x + 1, y);
                case BOT_LEFT:
                    return new Position (x - 1, y + 1);
                case BOT_RIGHT:
                    return new Position (x + 1, y + 1);
            }
        }

        System.err.println ("Invalid direction in moveToHexagon!!!");
        return new Position (Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    private Position moveToTriangle (Direction dir) {
        if ((x + y) % 2 == 0) {
            switch (dir) {
                case RIGHT:
                    return new Position (x + 1, y);
                case TOP_LEFT:
                    return new Position (x, y - 1);
                case BOT_LEFT:
                    return new Position (x, y + 1);
                default:
                    System.err.println ("Invalid direction in moveToTriangle!!!");
                    return new Position (Integer.MIN_VALUE, Integer.MIN_VALUE);
            }
        } else {
            switch (dir) {
                case LEFT:
                    return new Position (x - 1, y);
                case TOP_RIGHT:
                    return new Position (x, y - 1);
                case BOT_RIGHT:
                    return new Position (x, y + 1);
                default:
                    System.err.println ("Invalid direction in moveToTriangle!!!");
                    return new Position (Integer.MIN_VALUE, Integer.MIN_VALUE);
            }
        }
    }


    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y &&
                nodeType == position.nodeType;
    }

    @Override
    public int hashCode () {
        return Objects.hash (x, y, nodeType);
    }
}
