package configuration.response;

import java.time.LocalDateTime;

public class ReservationResponseItem {

    // TODO add user information;
    public Integer ticketQuantity;
    public String id;
    public LocalDateTime expiryDttm;

    public ReservationResponseItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getExpiryDttm() {
        return expiryDttm;
    }

    public void setExpiryDttm(LocalDateTime expiryDttm) {
        this.expiryDttm = expiryDttm;
    }

    public Integer getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(Integer ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }
}
