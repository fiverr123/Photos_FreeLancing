package Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class represents an user.
 * @author AlexMandez
 */
public class User implements Comparable<User>, Serializable {


	private String username;
	private ObservableList<Album> listAlbums;
	private ArrayList<Album> listOfAlbumsToKeep;
	private Album currentAlbum;
	private TagSearchCondition tags;
	private TimeSearchCondition dates;
	private static final long serialVersionUID = -1L;
	public void writeToFile(boolean isStore) {
		if (isStore) {
			listOfAlbumsToKeep = new ArrayList<>(listAlbums);
			listAlbums = null;
			//
			for (Album a : listOfAlbumsToKeep) {
				a.WriteToFile(true);
			}
		}
		else {
			listAlbums = FXCollections.observableList(listOfAlbumsToKeep);
			listOfAlbumsToKeep = null;
			//
			for (Album a : listAlbums) {
				a.WriteToFile(false);
			}
		}
		//
		tags.writeToFile(isStore);
	}
	public User(String _username) {
		username 		= _username;
		listAlbums 		= FXCollections.observableArrayList();
		listOfAlbumsToKeep = null;
		currentAlbum 	= null;
		tags 			= new TagSearchCondition();
		dates 			= new TimeSearchCondition();
	}
	public Album addAlbum(String username) {
		Album album = new Album(username);
		boolean isFound=false;
		for(Album al:listAlbums)
		{
			if(al.getAlbumName().equals(username))
			{
				isFound=true;
			}
		}
		if (!isFound) {
			listAlbums.add(album);
			return album;
		}
		else {
			return null;
		}
	}
	public void addOrOverwriteAlbum(Album album) {

		for(Album al:listAlbums)
		{
			if(al.getAlbumName().equals(username))
			{
				listAlbums.remove(al);
				break;
			}
		}
		listAlbums.add(album);
	}

	public void updateAlbumName(int index, String albumName) {
		if (index >= 0 && index < listAlbums.size()) {
			boolean isFound=false;
			for(Album al:listAlbums)
			{
				if(al.getAlbumName().equals(username))
				{
					isFound=true;
				}
			}
			if (!isFound) {
				listAlbums.get(index).setAlbumName(albumName);
			}
		}
	}
	public Album getCurrentAlbum() {
		return currentAlbum;
	}
	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}
	public TagSearchCondition getTags() {
		return tags;
	}
	public void deleteTagSearchConditionTag(int index) {
		ObservableList<Tag> tagList = tags.getTags();
		if (tagList.size() > 0 && index >= 0 && index < tagList.size()) {
			tagList.remove(index);
		}
	}
	public TimeSearchCondition getDates() {
		return dates;
	}
	public ObservableList<Album> getListAlbums() {
		return listAlbums;
	}
	public String getUsername() {
		return username;
	}
	public Album deleteAlbumFromList(String albumName) {
		for(Album al:listAlbums)
		{
			if(al.getAlbumName().equals(albumName))
			{
				listAlbums.remove(al);
				break;
			}
		}

		return null;
	}
	public Album getAlbumFromList(String albumName) {
		for(Album al:listAlbums)
		{
			if(al.getAlbumName().equals(albumName))
			{
				return al;

			}
		}
		return null;
	}
	public void copyPhotoToNewAlbum(Photo photo, String targetAlbumName) {
		Photo newOne = new Photo(photo);
		Album targetAlbum = getAlbumFromList(targetAlbumName);
		targetAlbum.addToAlbum(newOne);
	}
	public void movePhotoToNewAlbum(Photo photo, String targetAlbumName) {
		Album targetAlbum = getAlbumFromList(targetAlbumName);
		currentAlbum.deletePhotoFromAlbum(photo);
		targetAlbum.addToAlbum(photo);
	}
	@Override
	public String toString() {
		return username;
	}
	@Override
	public int compareTo(User arg0) {
		return username.compareToIgnoreCase(arg0.username);
	}
	public List<Photo> searchByTag() {
		List<Photo> lst = new ArrayList<>();
		for (Album a : listAlbums) {
			if (!a.getAlbumName().equalsIgnoreCase(Register.getRegister().getALBUM_NAME_SEARCH_BY_DATE()) && !a.getAlbumName()
					.equalsIgnoreCase(Register.getRegister().getALBUM_NAME_SEARCH_BY_TAG())) {
				lst.addAll(a.doSearchTag(tags));
			}
		}
		return lst;
	}
	public List<Photo> searchByDate() {
		List<Photo> lst = new ArrayList<>();
		for (Album a : listAlbums) {
			if (!a.getAlbumName().equalsIgnoreCase(Register.getRegister().getALBUM_NAME_SEARCH_BY_DATE()) && !a.getAlbumName()
					.equalsIgnoreCase(Register.getRegister().getALBUM_NAME_SEARCH_BY_TAG())) {
				lst.addAll(a.doSearchDate(dates));
			}
		}
		return lst;
	}
}