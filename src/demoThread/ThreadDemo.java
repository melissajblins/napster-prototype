package demoThread;

public class ThreadDemo extends Thread {
	
	private String nome;
	
	public ThreadDemo(String nome) {
		this.nome = nome;
	}
	
	public void run() {
		for (int i = 4; i > 0; i--) {
			System.out.println("Thread: " + this.nome+ i);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
}
