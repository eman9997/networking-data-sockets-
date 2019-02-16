// the state class used to represent current state of a packet

public enum State {
    Ready(4),Sent(5), Acked(10), Received(13) ,Loss(15), Corrupt(20), Resent(25);
    private int value; 		// the value of enum state

    private State(int value) { this.value = value; }
    private State() { this.value = 4; }

    public String toString() {
        switch (this.value){
            case 4  : return "Ready";
            case 5  : return "Sent";
            case 10 : return "Acked";
            case 13 : return "Received";
            case 15 : return "Loss";
            case 20 : return "Corrupt";
            case 25 : return "Resent";
        }
        return "";
    }
}
