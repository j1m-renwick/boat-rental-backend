package configuration.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class TripResponseItem extends ResponseItem {

    // TODO replace object mapper bean with a custom one with the required config
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;
    private Integer tickets;
    private String junkName;
    private Harbour harbour;
    private TripType type;
    private LocalDateTime departureDttm;
    private String description;

    public TripResponseItem() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public TripType getType() {
        return type;
    }

    public void setType(TripType type) {
        this.type = type;
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
