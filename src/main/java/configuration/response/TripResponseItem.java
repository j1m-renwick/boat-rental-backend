package configuration.response;

import java.time.LocalDateTime;

public class TripResponseItem extends ResponseItem {

    private String name;
    private Integer tickets;
    private String junkName;
    private LocalDateTime departureDttm;
    private String description;

    public TripResponseItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setTickets(Integer tickets) {
        this.tickets = tickets;
    }

    public String getJunkName() {
        return junkName;
    }

    public void setJunkName(String junkName) {
        this.junkName = junkName;
    }

    public LocalDateTime getDepartureDttm() {
        return departureDttm;
    }

    public void setDepartureDttm(LocalDateTime departureDttm) {
        this.departureDttm = departureDttm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
