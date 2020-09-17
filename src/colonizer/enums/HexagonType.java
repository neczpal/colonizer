package colonizer.enums;

public enum HexagonType {
    // --- Resources
    WOOD    (0, '&'),
    CLAY    (1, '='),
    WOOL    (2, '@'),
    WHEAT   (3, '!'),
    STONE   (4,'#'),

    // --- Trading (2 for 1)
    WOOD_HARBOR (20, '~'),
    CLAY_HARBOR (21,'~'),
    WOOL_HARBOR (22,'~'),
    WHEAT_HARBOR(23,'~'),
    STONE_HARBOR(24,'~'),

    // --- Trading (3 for 1)
    ANY_HARBOR  (30, '~'),

    // --- Nothing
    WATER   (60, '~'),
    DESERT  (61, ' '),
    NONE    (62, ' ');

    public int id;
    public char fillerChar;

    HexagonType(int id, char fillerChar) {
        this.id = id;
        this.fillerChar = fillerChar;
    }
};
