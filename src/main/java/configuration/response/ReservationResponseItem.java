package configuration.response;

import java.time.LocalDateTime;

public class ReservationResponseItem {

    public String userId;
    public String tripId;
    public Integer ticketQuantity;
    public LocalDateTime expiryDttm;

    public ReservationResponseItem() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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
