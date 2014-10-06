package mtgdeckbuilder.backend;

import java.util.Collections;
import java.util.List;

public class TagsManager {

    private final TagFilesManager tagFilesManager;

    public TagsManager(TagFilesManager tagFilesManager) {
        this.tagFilesManager = tagFilesManager;
    }

    public void tag(String tagName, String cardName) {
        List<String> loadedCards = tagFilesManager.load(tagName);

        if (loadedCards.contains(cardName)) {
            return;
        }

        loadedCards.add(cardName);

        tagFilesManager.save(tagName, loadedCards);
    }

    public void untag(String tagName, String cardName) {
        List<String> loadedCards = tagFilesManager.load(tagName);

        loadedCards.remove(cardName);

        tagFilesManager.save(tagName, loadedCards);
    }

    public List<String> getCards(String tagName) {
        return tagFilesManager.load(tagName);
    }

    public List<String> getAvailableTags() {
        return tagFilesManager.loadAvailableTags();
    }

    public void createEmptyTag(String tagName) {
        tagFilesManager.save(tagName, Collections.<String>emptyList());
    }

}
