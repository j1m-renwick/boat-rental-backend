package configuration.service.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class TripCreateRequest {

    private String junkId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String notes;

    public String getJunkId() {
        return junkId;
    }

    public void setJunkId(String junkId) {
        this.junkId = junkId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
