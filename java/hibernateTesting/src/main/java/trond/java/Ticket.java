package trond.java;

public class Ticket {

    int ticketId;

    String holderName;

    public Ticket() {}

    public Ticket(int ticketId, String holderName) {
        this.ticketId = ticketId;
        this.holderName = holderName;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        return "Ticket(ticketId: " + ticketId + ", holderName: " + holderName + ")";
    }
}
