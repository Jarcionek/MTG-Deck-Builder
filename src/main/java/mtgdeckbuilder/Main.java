package mtgdeckbuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:/Users/Jarcionek/Desktop/all-i-need.info");
         // http://api.mtgdb.info/cards/?fields=id,name,description,manacost,colors,type,subtype,power,toughness,rarity
        //TODO Jarek: change manacost to converted mana cost

        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        scanner.close();

        JSONArray json = new JSONArray(line);

        String color = "black";
        String manaCostOne = "1";
        String manaCostTwo = "B";
        String type = "Instant";


        outer:
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = (JSONObject) json.get(i);

            JSONArray colors = jsonObject.getJSONArray("colors");
            boolean colorMatches = false;
            inner:
            for (int j = 0; j < colors.length(); j++) {
                if (colors.getString(j).equals(color)) {
                    colorMatches = true;
                    break inner;
                }
            }
            if (!colorMatches) {
                continue outer;
            }

            if (jsonObject.getString("manaCost").equals(manaCostOne) || jsonObject.getString("manaCost").equals(manaCostTwo)) {
                System.out.println(jsonObject);
            }

        }

//        System.out.println(line);
//        int count = 0;
//        for (char c : line.toCharArray()) {
//            if (c == '{') {
//                count++;
//            }
//        }
//
//        System.out.println(count);
    }

}
