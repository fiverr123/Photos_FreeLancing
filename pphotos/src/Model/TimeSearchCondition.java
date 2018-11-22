package Model;


import java.io.Serializable;
import java.time.LocalDate;


/**
 * This class represents an album.
 * @author AlexMandez
 */
public class TimeSearchCondition implements Serializable {

	private static final long serialVersionUID = 1L;
    private LocalDate sDate = null;
    private LocalDate eDate = null;
    public LocalDate getStartDate() {
		return sDate;
	}
    public void setSDate(LocalDate startDate) {
		this.sDate = startDate;
	}
    public LocalDate getEDate() {
		return eDate;
	}
    public void setEDate(LocalDate endDate) {
		this.eDate = endDate;
	}
}