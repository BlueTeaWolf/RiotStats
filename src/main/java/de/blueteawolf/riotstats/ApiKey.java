package de.blueteawolf.riotstats;

/**
 * @author BlueTeaWolf
 */
public class ApiKey {
    private static final String P_API_KEY = "";

    public String getAPI_KEY() {
        return "?api_key=" + P_API_KEY;
    }

    public String getMATCH_API_KEY() {
        return "&api_key=" + P_API_KEY;
    }
}
