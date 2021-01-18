package colonizer.enums;

public enum ActionCardType {
    KNIGHT(0, "knight"),
    YEAR_OF_PLENTY(1, "year-of-plenty"),
    ROUTE_BUILDING(2, "route-building"),
    MONOPOLY(3, "monopoly"),
    POINT_CARD(4, "point-card");

    public int id;
    public String name;

    ActionCardType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ActionCardType parseString(String string) {
        for(ActionCardType type : values()) {
            if (type.name.equals(string)) {
                return type;
            }
        }
        return null;
    }
};
