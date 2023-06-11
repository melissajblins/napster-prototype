package demoThread;

public class Principal {

	public static void main(String[] args) {
		ThreadDemo td1 = new ThreadDemo("Demo 1");
		ThreadDemo td2 = new ThreadDemo("Demo 2");

		td1.start();
		td2.start();
	}
}
