package colonizer.enums;

public enum ActionCardType {
    KNIGHT(0),
    YEAR_OF_PLENTY(1),
    ROUTE_BUILDING(2),
    MONOPOLY(3),
    POINT_CARD(4);

    public int id;

    ActionCardType(int id) {
        this.id = id;
    }
};
