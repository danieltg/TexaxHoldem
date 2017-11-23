package Engine.Players;

public enum PlayerType {
    Human ("H"),
    Computer ("C");

    private String type;

    PlayerType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
