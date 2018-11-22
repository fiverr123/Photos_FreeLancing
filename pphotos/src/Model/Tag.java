package Model;


import java.io.Serializable;

/**
 * This class represents a tag.
 * @author AlexMandez
 */
public class Tag implements Comparable<Tag>, Serializable {
	private static final long serialVersionUID = -1L;
	private String tagName;
	private String tagValue;
	public Tag(String _tagName, String _tagValue) {
		tagName = _tagName;
		tagValue = _tagValue;
	}
	public Tag(Tag t) {
		tagName = t.tagName;
		tagValue = t.tagValue;
	}
	public String toString() {
		return tagName + "=" + tagValue;
	}
	@Override
	public int compareTo(Tag tag) {
		return tagName.compareToIgnoreCase(tag.tagName);
	}
}