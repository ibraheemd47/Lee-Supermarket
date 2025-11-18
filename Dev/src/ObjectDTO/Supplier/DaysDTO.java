package ObjectDTO.Supplier;

public enum DaysDTO {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    public String toString() {
        return this.name();
    }

    public static DaysDTO fromString(String text) {
        for (DaysDTO d : DaysDTO.values()) {
            if (d.name().equalsIgnoreCase(text)) {
                return d;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
