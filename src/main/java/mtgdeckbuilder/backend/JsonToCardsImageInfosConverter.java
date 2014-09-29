package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.CardImageInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class JsonToCardsImageInfosConverter {

    public Set<CardImageInfo> convert(String json) {
        JSONArray jsonArray = new JSONArray(json);

        Map<String, Integer> nameToIdMap = new HashMap<String, Integer>();
        Map<String, String> nameToReleaseMap = new HashMap<String, String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            if (!nameToIdMap.containsKey(name)  || currentReleasedAtIsYoungerThanTheOneInTheMap(jsonObject.getString("releasedAt"), nameToReleaseMap.get(name))) {
                nameToIdMap.put(name, id);
                nameToReleaseMap.put(name, jsonObject.getString("releasedAt"));
            }
        }

        Set<CardImageInfo> set = new HashSet<CardImageInfo>();

        for (Map.Entry<String, Integer> entry : nameToIdMap.entrySet()) {
            set.add(new CardImageInfo(entry.getValue(), entry.getKey()));
        }

        return set;
    }

    private boolean currentReleasedAtIsYoungerThanTheOneInTheMap(String currentReleasedAt, String releasedAtInTheMap) {
        String[] currentSplit = currentReleasedAt.split("-");
        String[] inTheMapSplit = releasedAtInTheMap.split("-");
        int currentValue = 10000 * parseInt(currentSplit[0]) + 100 * parseInt(currentSplit[1]) + parseInt(currentSplit[2]);
        int inTheMapValue = 10000 * parseInt(inTheMapSplit[0]) + 100 * parseInt(inTheMapSplit[1]) + parseInt(inTheMapSplit[2]);
        return currentValue > inTheMapValue;
    }

}
