package configuration.response;

import java.time.LocalDateTime;

public class TripResponseItem extends ResponseItem {

    private String name;
    private Integer tickets;
    private String junkName;
    private Harbour harbour;
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

    public Harbour getHarbour() {
        return harbour;
    }

    public void setHarbour(Harbour harbour) {
        this.harbour = harbour;
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
