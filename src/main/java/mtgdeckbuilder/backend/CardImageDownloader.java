package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.CardImageInfo;

import java.util.Set;

//TODO Jarek: implement!
/*
 * - searches for missing cards in directory
 * - downloads missing cards and saves to directory
 */
public class CardImageDownloader {

    public void download(Set<CardImageInfo> cardImageInfos) {
        System.out.println("=======================================");
        int count = 0;
        for (CardImageInfo cardImageInfo : cardImageInfos) {
            System.out.println(++count + "\t" + cardImageInfo.getName() + "\t" + cardImageInfo.getId());
        }
        System.out.println("=======================================");
    }

}
