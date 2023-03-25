package de.blueteawolf.riotstats;

/**
 * @author BlueTeaWolf
 */
public class ApiKey {
    private static final String P_API_KEY = "";

    public String getAPI_KEY() {
        String API_KEY = "?api_key=" + P_API_KEY;
        return API_KEY;
    }

    public String getMATCH_API_KEY() {
        String MATCH_API_KEY = "&api_key=" + P_API_KEY;
        return MATCH_API_KEY;
    }
}
