  import java.io.IOException;
	import java.net.DatagramPacket;
	import java.net.DatagramSocket;
	import java.net.SocketException;
	
	
public class Scheduler implements Runnable{
	
	DatagramPacket Receive = null;
	byte[] buffer = new byte[1024];
	DatagramSocket socket;
	DatagramPacket receivedPacket, sentPacket;
	Message received;
	
	public Scheduler() {
		try {
			socket = new DatagramSocket(8080);
			
        } catch (SocketException exception) {
            exception.printStackTrace();
        }
	}
	
	public void start() {
		while (true)
        {
            Receive = new DatagramPacket(buffer, buffer.length);
  
            try {
				socket.receive(Receive);
			} catch (IOException e) {
				e.printStackTrace();
			}
  
            try {
                received = (Message) Data.fromByteArray(Receive.getData());
            } catch (ClassNotFoundException e ) {
                e.printStackTrace();
            } catch (IOException e) {
				e.printStackTrace();
			}
            
            
            System.out.println("Packet received from " + received.getSender());
            System.out.println("From " + Receive.getAddress());
            System.out.println("Port: " + Receive.getPort());
            System.out.println(received);
            System.out.println("Will be sent to the host....");
            	
            try{
            	byte[] sentBuffer = Data.toByteArray(received);
            	sentPacket = new DatagramPacket(sentBuffer, sentBuffer.length, Receive.getAddress(), Receive.getPort());
            	System.out.println("Sent successed");
            	} catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Sent failed");
                }
            } 
            	
        	
        
        }
    
	public static void main(String args[]) throws IOException {
        Scheduler scheduler = new Scheduler();
        scheduler.start();
    }

    @Override
    public void run() {
        start();
    }
	
	
	
	
}
