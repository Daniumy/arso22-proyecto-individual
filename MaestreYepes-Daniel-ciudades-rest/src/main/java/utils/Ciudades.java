package utils;

public enum Ciudades {
    LORCA("Lorca", "30800"), MALAGA("Málaga", "29001"), TENERIFE("Santa Cruz de Tenerife", "38001");

    private final String key;
    private final String value;

    Ciudades(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
}
