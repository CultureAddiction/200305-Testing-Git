package Omok;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * <code>���� ����</code> - �Ϲݷ�(�����)�� �̿��ϴ� ������ �� ����.
 * https://namu.wiki/w/%EC%98%A4%EB%AA%A9(%EA%B2%8C%EC%9E%84)
 *
 * ����: Ŀ�ǵ���ο��� java Omok [<�� ũ��>]
 * @author ����
 * @version 1.0
 */
public class Omok
{
    /**
     * <code>����</code> - �������� �ʱ�ȭ
     * �� ũ��� �⺻������ 15���� Ŀ�ǵ���ο��� ������ �� ������ �� �ִ�.
     *
     * @param args a <code>String[]</code> value - command line
     * arguments
     */
    public static void main(String[] args) {

	int size = 15;
	if (args.length > 0)
	    size = Integer.parseInt(args[0]);

	JFrame frame = new JFrame();
	
	final int FRAME_WIDTH = 600;
	final int FRAME_HEIGHT = 650;
	frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
	frame.setTitle("Omok");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	OmokPanel panel = new OmokPanel(size);
	frame.add(panel);
	
	frame.setVisible(true);
    
    }
}

class OmokState {
	public static final int NONE = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 2;
	private int size;
	private int winner;
	private int currentPlayer;
	private int board[][];
	private boolean isSwitchOK = true;
	public OmokState(int size) {
		this.size = size;
		board = new int[size][size];
		currentPlayer = BLACK;
	}
	
	public void playPiece(int row, int col) {

		System.out.println("Try Place at row,column " + row +","+ col+" as Player"+currentPlayer);
		if (validMove(row, col))
			board[row][col] = currentPlayer;
		else{// ���⿡ �Ѽ� ���ٰ� ����� �߸� -> ���� �ϳ��� �߰� �ؼ� currentPlayer - Switch�� false�� �ǵ���
			JOptionPane.showMessageDialog(null, "���⿡ �� �� �����ϴ�.");
			isSwitchOK = false;
		}
		switch (currentPlayer) {	
		
		case BLACK:
			if (isSwitchOK)
			currentPlayer = WHITE;		// ���� �÷��̸� �����ϴ� ���.
			isSwitchOK= true;
			break;
		case WHITE:
			if (isSwitchOK)
			currentPlayer = BLACK;
			isSwitchOK=true;
			break;
		}
	}
	
	public int getPiece(int row, int col) {
		return board[row][col];
	}
	
	public int getWinner() {
		return winner;
	}
	
	public boolean validMove(int row, int col) {
		// 	validMove�� 	false�� ���⿡ �� �� ���ٴ� message�� ����Ѵ�.
		// 				true�� ����		
		int r = row, c = col;
		/*
		 * step
		 * ����: 0(��), 1(��)
		 * ����: 2(��), 3(��)
		 * �缱: 4(����), 5(����), 6(����), 7(����)
		 */
		int step = 0;
		int[] stepCount = new int[8];	// ������ �����ϴ� ��� ������(8����) �˻��ϴ� �迭
		boolean doneCheck = false;
		while (!doneCheck) {

			switch (step) {
			// if�������� step�� ���캼 ������ �����ϸ�, r�� c�� �����ϸ鼭 ���������� ���캸�� ������ ���� ������ stepCount�� ����� ����.
			// else�������� step�� ���� �ܰ�� �����ϸ�, r�� c�� �ʱ� row������ �ǵ��� ���´�.
			// ����. �浹�� ���� �ڸ� ���� �浹�� ��, �Ʒ��� �浹�� �ϳ� ������ case0�� Ž�� ���� = �ʱ�ȭ �ѹ�, case1�� Ž�� 1�� �ʱ�ȭ 
			//													�Ǿ�� �ϴµ�.... if�� �Ʒ��� ���� ���� �ʴ´�.
			case 0:
				if (!outOfBounds(r-1) && sameColor(--r, c))	//���� �� r--? ������ �� ���ű� ������ --- case 0�� ������ ȣ��ȴ�.
					stepCount[step]++;						// if�� ������ ���� �ʱ� ������ stepCount�� ���� ���� �ʴ´� - ���ǹ��� ���� Ȯ��.
				else { step++; r = row; c = col; }			// ���� �ذ�: �� ���ǹ� �Լ����� �̻�X. �μ��� ���캽 --- �̻� �߰�
				break;
			case 1:
				if (!outOfBounds(r+1) && sameColor(++r, c))
					stepCount[step]++;
				else { step++; r = row; c = col; }
				break;
			case 2:
				if (!outOfBounds(c+1) && sameColor(r, ++c))
					stepCount[step]++;
				else { step++; r = row; c = col; }
				break;
			case 3:
				if (!outOfBounds(c-1) && sameColor(r, --c))
					stepCount[step]++;
				else { step++; r = row; c = col; }
				break;
			case 4:
				if (!outOfBounds(r-1) && !outOfBounds(c+1) && sameColor(--r, ++c))
					stepCount[step]++;
				else { step++; r = row; c = col; }
				break;
			case 5:
				if (!outOfBounds(r+1) && !outOfBounds(c-1) && sameColor(++r, --c))
					stepCount[step]++;
				else { step++; r = row; c = col; }
				break;
			case 6:
				if (!outOfBounds(r-1) && !outOfBounds(c-1) && sameColor(--r, --c))
					stepCount[step]++;
				else { step++; r = row; c = col; }
				break;
			case 7:
				if (!outOfBounds(r+1) && !outOfBounds(c+1) && sameColor(++r, ++c))
					stepCount[step]++;
				else { step++; r = row; c = col; }
				break;
			default:
				doneCheck = true;
				break;
			}
		}
		// moveResult�� ���ڸ� �����ϸ� 0�� return, �������� �ʾҴٸ� 1�� 2�� return
		// 1�� 2��? 2�� ������ ���. 1�� ???
		int result = moveResult(stepCount);
		
		if (result == 0) winner = currentPlayer;
		
		if (result == 1 || result == 2)
			return false;
		
		return true;
	}
	
	// switch case������ stepCount[]�� �����ֱ� ���� ���� �Լ� 2��
	public boolean outOfBounds(int n) {
		// �Լ� ���� �̻� ��
		return !(n >= 0 && n < size);
	}
	
	public boolean sameColor(int r, int c) {
		// �Լ� ���� �̻� ��
		return board[r][c] == currentPlayer;
	}
	
	
	/*
	 * �̱�� ��(5): 0
	 * �ݼ�(33 Ȥ�� 44): 1
	 * ���(6�̻�): 2
	 * ��: 3
	 */
	public int moveResult(int[] stepCount) {	// return���� 1,2�� false, 0�̸� ���� ����.
		final int checkBugOn33 = 0;
		final int checkBugOn44 = 1;
		int countTwo = 0, countThree = 0;
		boolean win = false;
		for (int i=0; i<8; i++) {
			// 1. moveResult 2 Ȥ�� 0�� �����ϴ� if-else
			if (i % 2 == 1 && (stepCount[i-1] + stepCount[i] > 5-1)) 
								// sc[0] + sc[1] > 5, sc[3] + sc[4] > 6 .... 
								// -> ��+���� 6,7,8,... ��+���� 6,7,8... �̷���� = 6�� �϶� 
				return 2;		// 6��, 7�� 
			else 
				if (i % 2 == 1 && (stepCount[i-1] + stepCount[i] == 5-1))
								// �� + �� = 5, �� + �� = 5, 
					win = true;
			
			// moveresult 1�� �����ϴ� if-else
			if (stepCount[i] == 2-1) 
				countTwo++;
			else
				if (stepCount[i] == 3-1) 
					countThree++;
		}
		
		// �Ʒ��� return���� �����ϴ� if����
		if (countTwo >= 2-checkBugOn33 || countThree >= 2-checkBugOn44)
			return 1;
		if (win)
			return 0;
		return 3;
	}
	
	
	
	
}

class OmokPanel extends JPanel
{
    private final int MARGIN = 5;
    private final double PIECE_FRAC = 0.9;

    private int size = 19;
    private OmokState state;
    
    public OmokPanel() 
    {
	this(15);
    }

    public OmokPanel(int size) 
    {
	super();
	this.size = size;
	state = new OmokState(size);
	addMouseListener(new GomokuListener());
    }

    class GomokuListener extends MouseAdapter 
    {
	public void mouseReleased(MouseEvent e) 
	{
	    double panelWidth = getWidth();
	    double panelHeight = getHeight();
	    double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
	    double squareWidth = boardWidth / size;
	    double pieceDiameter = PIECE_FRAC * squareWidth;
	    double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
	    double yTop = (panelHeight - boardWidth) / 2 + MARGIN;
	    int col = (int) Math.round((e.getX() - xLeft) / squareWidth - 0.5);
	    int row = (int) Math.round((e.getY() - yTop) / squareWidth - 0.5);
	    if (row >= 0 && row < size && col >= 0 && col < size
		&& state.getPiece(row, col) == OmokState.NONE
		&& state.getWinner() == OmokState.NONE) {
		state.playPiece(row, col);
		repaint();
		int winner = state.getWinner();
		if (winner != OmokState.NONE)
		    JOptionPane.showMessageDialog(null,
                      (winner == OmokState.BLACK) ? "Black wins!" 
						    : "White wins!");
	    }
	}    
    }
    
    
    public void paintComponent(Graphics g) 
    {
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
	
	double panelWidth = getWidth();
	double panelHeight = getHeight();

	g2.setColor(new Color(0.925f, 0.670f, 0.34f)); // ������
	g2.fill(new Rectangle2D.Double(0, 0, panelWidth, panelHeight));

	
	double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
	double squareWidth = boardWidth / size;
	double gridWidth = (size - 1) * squareWidth;
	double pieceDiameter = PIECE_FRAC * squareWidth;
	boardWidth -= pieceDiameter;
	double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
	double yTop = (panelHeight - boardWidth) / 2 + MARGIN;

	g2.setColor(Color.BLACK);
	for (int i = 0; i < size; i++) {
	    double offset = i * squareWidth;
	    g2.draw(new Line2D.Double(xLeft, yTop + offset, 
				      xLeft + gridWidth, yTop + offset));
	    g2.draw(new Line2D.Double(xLeft + offset, yTop,
				      xLeft + offset, yTop + gridWidth));
	}
	
	for (int row = 0; row < size; row++) 
	    for (int col = 0; col < size; col++) {
		int piece = state.getPiece(row, col);
		if (piece != OmokState.NONE) {
		    Color c = (piece == OmokState.BLACK) ? Color.BLACK : Color.WHITE;
		    g2.setColor(c);
		    double xCenter = xLeft + col * squareWidth;
		    double yCenter = yTop + row * squareWidth;
		    Ellipse2D.Double circle
			= new Ellipse2D.Double(xCenter - pieceDiameter / 2,
					       yCenter - pieceDiameter / 2,
					       pieceDiameter,
					       pieceDiameter);
		    g2.fill(circle);
		    g2.setColor(Color.black);
		    g2.draw(circle);
		}
	    }
    }
    
    
}




