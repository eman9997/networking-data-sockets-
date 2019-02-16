// The packet class to build packet for datagram.

import java.io.Serializable;

public class Packet implements Serializable {
    private int seq=0; 		// the seq number
    private byte[] payload; 	// the datagram
    private int len; 		// the length of data
    public State state; 	// the state of packet  Ready,Sent,Acked,Received,Loss,Corrupt, Resent;
    public int reTransmits = 0; // the times of retransmits

     // no default constructor
    private Packet() { }

    public Packet(Packet p) {
        this.len = p.len;
        this.payload = p.payload;
        this.state = p.state;
        this.reTransmits = p.reTransmits;
    }

    
    public Packet(int seq, byte[] payload, State state) {
        this.seq = seq;
        this.payload = payload;
        this.state = state;
        this.len = payload.length;
    }

    public Packet(int seq, String strPayload, State state) {
        this.seq = seq;
        this.payload = strPayload.getBytes();
        this.state = state;
        this.len = this.payload.length;
    }
    
    public Packet(int seq, String strPayload) {
        this.seq = seq;
        this.payload = strPayload.getBytes();
        this.state = State.Ready;
        this.len = this.payload.length;
    }

    public void setSeq(int seq) { this.seq = seq; }
    
    public void setSeq(){ 
    	
    	this.seq = seq++; }

    public void setAck(boolean ack) { this.state = State.Acked; }

    public void setPayLoad(byte[] payload) { this.payload = payload; }
	
	public int getRetransmits(){ return reTransmits;}
	
	public void incRetransmits() {reTransmits++;}

    public byte[] getPayload() { return payload; }

    public int getSeq() { return seq; }

    public boolean isAcked() { return (state == State.Acked); }

    public String toString() {
        return ("Seq: " + seq + " 's state is " + state.toString() + " , it is " + reTransmits + " times to retransmits");
    }

}
