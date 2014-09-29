package mtgdeckbuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonToCardsImageInfosConverter {

    public Set<CardImageInfo> convert(String json) {
        JSONArray jsonArray = new JSONArray(json);

        Map<String, Integer> nameToIdMap = new HashMap<String, Integer>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            if (nameToIdMap.containsKey(name)) {
                throw new UnsupportedOperationException("not yet implemented"); //TODO Jarek: implement!
            } else {
                nameToIdMap.put(name, id);
            }
        }

        Set<CardImageInfo> set = new HashSet<CardImageInfo>();

        for (Map.Entry<String, Integer> entry : nameToIdMap.entrySet()) {
            set.add(new CardImageInfo(entry.getValue(), entry.getKey()));
        }

        return set;
    }

}
