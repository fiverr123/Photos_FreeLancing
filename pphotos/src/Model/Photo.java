package Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * @author AlexMandez
 */
public class Photo implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String THUMBNAIL_IMAGE_FORMAT 	= "png";
	private static final int THUMBNAIL_WIDTH = 120;
	private static final int THUMBNAIL_HEIGHT= 120;
	private static final String	THUMBNAIL_PATH = "thumbnails";
	private String fileName;
	private String thumbnail;
	private String caption;
	private long dateOfPhoto;
	private ObservableList<Tag> listTags;
	private ArrayList<Tag> listOfTagsToKeep;
	static {
		new File(THUMBNAIL_PATH).mkdir();
	}
	public static String getThumbnailFileName(String input) {
		return THUMBNAIL_PATH + "/" + input + "." + THUMBNAIL_IMAGE_FORMAT;
	}
	public static Photo createPhoto(String filename, File file) {
		if (file==null) {
			file = new File(filename);
		}
		long lastModified = file.lastModified();
		String shortFileName = file.getName();
		int pos = shortFileName.indexOf('.');
		if (pos>0) {
			shortFileName = shortFileName.substring(0, pos);
		}
		String thumbnail = String.valueOf(UUID.randomUUID());
		boolean ret = createThumbNail(filename, getThumbnailFileName(thumbnail), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, THUMBNAIL_IMAGE_FORMAT);
		if (ret) {
			return new Photo(filename, thumbnail, shortFileName, lastModified);
		}
		return null;
	}
	private static boolean createThumbNail(String photoFileName, String thumbnailFileName, int width, int height, String thumbnailFormat) {
		boolean ret = true;
		Image image = null;
		try {
			image = new Image(new FileInputStream(photoFileName), width, height, true, true);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (image!=null) {
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			try {
				FileOutputStream fos = new FileOutputStream(thumbnailFileName);
				ImageIO.write(bufferedImage, thumbnailFormat, fos);
				fos.close();
			}
			catch (IOException e) {
				ret = false;
				e.printStackTrace();
			}
		}
		else {
			ret = false;
		}
		return ret;
	}
	public void writeToFile(boolean isStore) {
		if (isStore) {
			listOfTagsToKeep = new ArrayList<>(listTags);
			listTags = null;
		}
		else {
			listTags = FXCollections.observableList(listOfTagsToKeep);
			listOfTagsToKeep = null;
		}
	}
	private Photo(String _fileName, String _thumbnail, String _caption, long _dateOfPhoto) {
		fileName 		= _fileName;
		thumbnail		= _thumbnail;
		caption 		= _caption;
		dateOfPhoto		= _dateOfPhoto;
		listTags 		= FXCollections.observableArrayList();
		listOfTagsToKeep = null;
	}
	public ObservableList<Tag> getTags() {
		return listTags;
	}
	public Photo(Photo photo) {
		this.fileName = photo.fileName;
		this.thumbnail = String.valueOf(UUID.randomUUID());
		this.caption = photo.caption;
		this.dateOfPhoto = photo.dateOfPhoto;
		listTags 		= FXCollections.observableArrayList();
		for (Tag t: photo.listTags) {
			listTags.add(new Tag(t));
		}
		try {
			Files.copy(new File(photo.getThumbnailFileName()).toPath(), new File(getThumbnailFileName()).toPath());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String getThumbnailFileName() {
		return getThumbnailFileName(thumbnail);
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public long getDateOfPhoto() {
		return dateOfPhoto;
	}
	public String getDateOfPhotoString() {
		return Register.getRegister().epochToLocalTime(dateOfPhoto);
	}
	public boolean addTag(String tagName, String tagValue) {
		Tag t = new Tag(tagName, tagValue);
		return listTags.add(t);
	}
	public Tag deleteTag(String tagName, String tagValue) {
		Tag t = new Tag(tagName, tagValue);
		for(int i=0;i<listTags.size();i++)
		{
			Tag tt=listTags.get(i);
			if(tt.compareTo(t)==0)
			{
				listTags.remove(i);
				return tt;
				
			}
		}
		return null;
	}
	public void deleteTag(int i) {
		if (i>=0 && i<listTags.size()) {
			listTags.remove(i);
		}
	}
	public BorderPane getThumbnailImageView(EventHandler<MouseEvent> handler, String style) {
		Image img = new Image("File:"+getThumbnailFileName(), 0, 0, false, false);
		ImageView viw = new ImageView(img);
		viw.setFitWidth(THUMBNAIL_WIDTH);
		viw.setFitHeight(THUMBNAIL_HEIGHT);
		viw.setOnMouseClicked(handler);
		viw.setUserData(this);
		Photo thisPhoto = this;
		TextField textf = new TextField(getCaption());
		textf.setPrefWidth(THUMBNAIL_WIDTH);
		textf.setOnAction(event -> {
			TextField textField = (TextField) event.getSource();
			String temp = textField.getText().trim();
			if (temp.length()==0) {
				textField.setText(thisPhoto.getCaption());
			}
			else {
				thisPhoto.setCaption(temp);
			}
		});
		VBox vbox = new VBox(4); 	// spacing = 4
		vbox.getChildren().addAll(viw, textf, new Label(getDateOfPhotoString()));
		BorderPane viewWrapper = new BorderPane(vbox);
		viewWrapper.setStyle(style);
		return viewWrapper;
	}
	public Node getImgNode(EventHandler<MouseEvent> hndlr) {
		ImageView imgView;
		Image imgR = null;
		try {
			imgR = new Image(new FileInputStream(getFileName()));
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		imgView = new ImageView();
		imgView.setFitWidth(650);
		imgView.setFitHeight(450);
		imgView.setPreserveRatio(true);
		imgView.setSmooth(true);
		imgView.setImage(imgR);
		imgView.setOnMouseClicked(hndlr);
		VBox vbox = new VBox(2); 	// spacing = 2
		vbox.getChildren().addAll(new Label("Caption: " + getCaption() + ". This photo was taken at " + getDateOfPhotoString()), imgView);
		return vbox;
	}
	public boolean isWithinRange(TimeSearchCondition dates) {
		ZoneId zId = ZoneId.systemDefault();
		LocalDate sDate 	= dates.getStartDate();					//starting of startdate
		LocalDate eDate	 	= dates.getEDate().plusDays(1);		//end of enddate
		long start 	= sDate.atStartOfDay(zId).toEpochSecond();
		long end 	= eDate.atStartOfDay(zId).toEpochSecond();
		Date dateOfPhoto = new Date(this.dateOfPhoto);
		LocalDateTime date = dateOfPhoto.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		long toCheck = date.atZone(zId).toEpochSecond();
		return toCheck >= start && toCheck < end;
	}

}