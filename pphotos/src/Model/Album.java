package Model;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

/**
 * This class represents an album.
 * @author AlexMandez
 */
public class Album implements Comparable<Album>, Serializable {
	private List<Photo> photoList;
	private  SimpleStringProperty albumName;
	private int indexOFCurrentPhoto;
	private  SimpleStringProperty eTime;
	private static final long serialVersionUID = -1L;
	private  SimpleIntegerProperty photo;
	private String albumToKeep;
	private  SimpleStringProperty sTime;
	public void WriteToFile(boolean isStore) {
		if (isStore) {
			albumToKeep = getAlbumName();
			albumName 	= null;
			photo 	= null;
			sTime 	= null;
			eTime 	= null;
		}
		else {
			albumName 	= new SimpleStringProperty(albumToKeep);
			photo 	= new SimpleIntegerProperty();
			sTime 	= new SimpleStringProperty();
			eTime 	= new SimpleStringProperty();
			albumToKeep = null;
		}
		for (Photo p: photoList) {p.writeToFile(isStore);
		}
	}
	public Album(String _albumName) {
		albumName 			= new SimpleStringProperty(_albumName);
		photo 			= new SimpleIntegerProperty(0);
		sTime 			= new SimpleStringProperty("N/A");
		eTime 			= new SimpleStringProperty("N/A");
		photoList 			= new ArrayList<>();
		indexOFCurrentPhoto 	= -1;
	}
	public Album(String _albumName, List<Photo> photoLst) {
		this(_albumName);
		photoList.addAll(photoLst);
		if (!photoList.isEmpty()) {
			indexOFCurrentPhoto = 0;
		}
	}
	public int setCurrentPhotoIndex(int input) {
		indexOFCurrentPhoto = input;
		return resetIndex();
	}
	private int resetIndex() {
		if (photoList.isEmpty()) {
			indexOFCurrentPhoto 	= -1;
		}
		else {
			if (indexOFCurrentPhoto > photoList.size() - 1) {
				indexOFCurrentPhoto  = photoList.size() - 1;
			}
			else if (indexOFCurrentPhoto < 0) {
				indexOFCurrentPhoto = 0;
			}
		}
		return indexOFCurrentPhoto;
	}
	public int getCurrentIndex() {
		return 	resetIndex();
	}
	public void addToAlbum(Photo photo) {
		photoList.add(photo);
		indexOFCurrentPhoto = photoList.size() - 1;
	}
	public Photo getFromAlbum(int index) {
		if (index < photoList.size() && index>=0) {
			return photoList.get(index);
		}
		return null;
	}
	public int getPhotoIndex(Photo photo) {
		for(int i=0;i<photoList.size();i++)
		{
			if(photoList.get(i)==photo)
			{
				return i;
			}
		}
		return -1;		
	}
	public int deletePhotoFromAlbum(Photo photo) {
		int index = -1;
		for (int i=0; i<photoList.size(); i++) {
			if (photo==photoList.get(i)) {
				index = i;
				photoList.remove(index);
				break;
			}
		}
		return index;
	}
	public int addPhotoToAlbum(Photo photoAt, Photo photoAdd) {
		int index=-1;;
		if (photoAt != null) {
			for(int i=0;i<photoList.size();i++)
			{
				if(photoList.get(i)==photoAt)
				{
					index=i;
				}
			}
		} else index = photoList.size();
		photoList.add(index, photoAdd);
		return index;
	}
	public List<Photo> getPhotoList() {
		return photoList;
	}
	public int getSize() {
		return photoList.size();
	}
	public Photo getCurrentPhoto() {
		resetIndex();
		return indexOFCurrentPhoto >= 0 ? photoList.get(indexOFCurrentPhoto) : null;
	}
	@Override
	public int compareTo(Album album) {
		return getAlbumName().compareToIgnoreCase(album.getAlbumName());
	}
	@Override
	public String toString() {
		return albumName.get();
	}
	public List<Photo> doSearchTag(TagSearchCondition tags) {
		BiPredicate<Photo,TagSearchCondition> bp = (p, c)->Register.getRegister().search(p.getTags(), c.getTags(), (t1,t2)->t1.compareTo(t2)==0);
		return Register.getRegister().filter(photoList, tags, bp);
	}
	public List<Photo> doSearchDate(TimeSearchCondition dates) {
		BiPredicate<Photo,TimeSearchCondition> bp = Photo::isWithinRange;
		return Register.getRegister().filter(photoList, dates, bp);
	}    
	public void setCounterDatetime() {
		if (photoList.isEmpty()) {
			setPhotoCount(0);
			setStartTime("N/A");
			setEndTime("N/A");
		}
		else {
			setPhotoCount(photoList.size());
			long min	= photoList.get(0).getDateOfPhoto();
			long max	= photoList.get(0).getDateOfPhoto();
			for (int i = 1; i < photoList.size(); i++) {
				long pd = photoList.get(0).getDateOfPhoto();
				if (pd > max) {
					max = pd;
				}
				if (pd < min) {
					min = pd;
				}
			}
			setStartTime(Register.getRegister().epochToLocalTime(min));
			setEndTime(Register.getRegister().epochToLocalTime(max));
		}
	}
	public String getAlbumName() {return albumName.get();
	}
	public void setAlbumName(String albumName) {this.albumName.set(albumName);
	}
	public int getPhotoCount() {return photo.get();
	}
	public void setPhotoCount(int photoCount) {this.photo.set(photoCount);
	}
	public String getStartTime() {return sTime.get();
	}
	public void setStartTime(String startTime) {this.sTime.set(startTime);
	}
	public String getEndTime() {return eTime.get();
	}
	public void setEndTime(String endTime) {this.eTime.set(endTime);
	}
}