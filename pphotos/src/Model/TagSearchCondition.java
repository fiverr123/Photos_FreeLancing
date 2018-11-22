package Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * This class helps to search through tags.
 * @author AlexMandez
 */
public class TagSearchCondition implements Serializable {
	private static final long serialVersionUID = 1L;
	public void writeToFile(boolean isStore) {
		if (isStore) {
			listOfTagsKeep = new ArrayList<>(listOfTags);
			listOfTags 		= null;
		}
		else {
			listOfTags 		= FXCollections.observableList(listOfTagsKeep);
			listOfTagsKeep = null;
		}
	}
	private ObservableList<Tag> listOfTags;
	private ArrayList<Tag> listOfTagsKeep;
	public TagSearchCondition() {
		listOfTags = FXCollections.observableArrayList();
	}
	public ObservableList<Tag> getTags() {
		return listOfTags;
	}
	public boolean addTag(String tagName, String tagValue) {
		Tag t = new Tag(tagName, tagValue);
		return listOfTags.add(t);
	}
}