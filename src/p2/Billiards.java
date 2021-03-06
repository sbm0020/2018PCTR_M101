package p2;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Billiards extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -489315240540216418L;
	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	private Board board;
	private ExecutorService ejecutor;
	private final int N_BALL = 5;
	private Ball[] balls;

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Práctica programación concurrente objetos móviles independientes");
		setResizable(false);
		setVisible(true);
	}

	private void initBalls() {
		balls=new Ball[N_BALL];
		for (int i=0;i<N_BALL;i++) {
			balls[i]=new Ball();
		}
		board.setBalls(balls);
	}

	private class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ejecutor = Executors.newCachedThreadPool();
			for (int i=0;i<N_BALL;i++) {
				ejecutor.submit(new Hilo(balls[i]));
			}
			// TODO Code is executed when start button is pushed
			

		}
		private class Hilo implements Runnable{
			Ball b;
			
			public Hilo(Ball bola) {
				b = bola;
			}
			
			@Override
			public void run() {
				while (true){
					b.move();
					board.repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						ejecutor.shutdownNow();
						break;
					}
					
				}
				
			}
			
		}
	}

	private class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ejecutor.shutdownNow();
		}
	}

	public static void main(String[] args) {
		new Billiards();
	}
}