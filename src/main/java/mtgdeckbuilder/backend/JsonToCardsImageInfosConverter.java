package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.CardImageInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonToCardsImageInfosConverter {

    public Set<CardImageInfo> convert(String json) {
        JSONArray jsonArray = new JSONArray(json);

        Map<String, Integer> nameToIdMap = new HashMap<>();
        Map<String, String> nameToReleaseMap = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            if (!nameToIdMap.containsKey(name)  || jsonObject.getString("releasedAt").compareTo(nameToReleaseMap.get(name)) > 0) {
                nameToIdMap.put(name, id);
                nameToReleaseMap.put(name, jsonObject.getString("releasedAt"));
            }
        }

        Set<CardImageInfo> set = new HashSet<>();

        for (Map.Entry<String, Integer> entry : nameToIdMap.entrySet()) {
            set.add(new CardImageInfo(entry.getValue(), entry.getKey()));
        }

        return set;
    }

}
