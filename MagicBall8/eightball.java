import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class eightball {
	static JFrame mainPanel = new JFrame("Political Magic Ball 8");
	static JTextField input = new JTextField();
	static JButton updateInput = new JButton("Ask");
	static JButton getClinton, getSanders, getTrump;
	static JMenuBar menubar = new JMenuBar();
	static JMenu menu = new JMenu();
	static ArrayList<JMenuItem> canidates = new ArrayList<JMenuItem>();
	static ArrayList<String> quoteList = new ArrayList<>();
	static HashMap<String, HashMap<String, Integer>> quotePoints = new HashMap<>();
	static HashMap<String, String> candidateQuote = new HashMap<String, String>();
	static String question;
	static String currentPerson;
	static JLabel sanders, clinton, trump;

	public static void main(String[] args) throws FileNotFoundException {
		
		getSanders = new JButton("Sanders");
		getClinton = new JButton("Clinton");
		getTrump = new JButton("Trump");
		getSanders.setSize(new Dimension(90, 40));
		getClinton.setSize(new Dimension(90, 40));
		getTrump.setSize(new Dimension(90, 40));
		getSanders.setLocation(100, 100);
		getClinton.setLocation(200, 100);
		getTrump.setLocation(300, 100);
		ActionListener ac = new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				String s = action.getActionCommand().toLowerCase();
				try {
					changePerson(s);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "File not found");
					System.exit(1);
				}
			}
		};
		getSanders.addActionListener(ac);
		getTrump.addActionListener(ac);
		getClinton.addActionListener(ac);
		mainPanel.setDefaultCloseOperation(2);
		mainPanel.setPreferredSize(new Dimension(900, 720));
		mainPanel.setSize(900, 660);
		mainPanel.setLocationRelativeTo(null);
		mainPanel.setResizable(false);
		mainPanel.setVisible(true);
		makeInputBox();
		menubar.add(menu);
		sanders = new JLabel(new ImageIcon("SandersWithFlag.png"));
		sanders.setSize(900, 720);
		clinton = new JLabel(new ImageIcon("HillaryWithFlag.png"));
		clinton.setSize(900, 720);
		trump = new JLabel(new ImageIcon("TrumpWithFlag.png"));
		trump.setSize(900, 720);
		updateInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				if (action.getActionCommand().equals("Ask")) {
					JOptionPane.showMessageDialog(null, getResponse(input.getText()));
					input.setText(null);
				}
			}
		});
		updateInput.setLocation(500, 550);
		updateInput.setSize(new Dimension(60, 40));
		mainPanel.add(getSanders);
		mainPanel.add(getClinton);
		mainPanel.add(getTrump);
		mainPanel.add(menubar);
		mainPanel.add(input);
		mainPanel.add(updateInput);
		mainPanel.add(clinton);
		mainPanel.add(sanders);
		mainPanel.add(trump);
		mainPanel.add(clinton);
		mainPanel.add(sanders);
		changePerson("clinton");
		mainPanel.pack();
	}

	public static void read(String name) throws FileNotFoundException {
		quotePoints = new HashMap<String, HashMap<String, Integer>>();
		quoteList = new ArrayList<String>();
		Scanner sc = new Scanner(new File(name + "Sayings"));
		while (sc.hasNext()) {
			String quote = sc.nextLine();
			HashMap<String, Integer> quoteVal = new HashMap<String, Integer>();
			String str = "";
			do {
				str = sc.nextLine();
				if (!(str.equals("---"))) {
					quoteVal.put(str.substring(1).split(":")[0], Integer.parseInt(str.substring(1).split(":")[1]));
				}
			} while (!str.equals("---"));
			candidateQuote.put(quote, name);
			quotePoints.put(quote, quoteVal);
			quoteList.add(quote);
		}
		sc.close();
	}

	public static void changePerson(String name) throws FileNotFoundException {
		currentPerson = name;
		sanders.setVisible(false);
		clinton.setVisible(false);
		trump.setVisible(false);
		if (name.equals("sanders")) {
			read("bernie");
			sanders.setVisible(true);
		} else if (name.equals("clinton")) {
			read("hillary");
			clinton.setVisible(true);
		} else if (name.equals("trump")) {
			read("trump");
			trump.setVisible(true);
		}
	}

	public static String getResponse(String input) {
		String quote = "";
		int maxPt = 0;
		String input2 = input.toLowerCase();
		String[] words = input2.split("^\\W+");
		for (String str : quoteList) {
			HashMap<String, Integer> wordToPoint = quotePoints.get(str);
			int points = 0;
			for (String word : words) {
				Iterator<String> it = wordToPoint.keySet().iterator();
				while (it.hasNext()) {
					String check = (String) it.next();
					if (check.toLowerCase().contains(word.toLowerCase()) || word.toLowerCase().contains(check.toLowerCase()))
						points += wordToPoint.get(check);
				}
			}
			if (points >= maxPt) {
				maxPt = points;
				quote = str;
			}
		}
		if (input.contains("sure") && currentPerson.equals("clinton")) {
			return "I am not a liar!";
		}
		if (maxPt < 1) {
			String msg = "I have nothing to say to you";
			try {
				Scanner msgGtr = new Scanner(new File("ballMessages"));
				for (int i = (int) (Math.random() * 18); i >= 0 && msgGtr.hasNext(); i--) {
					msg = msgGtr.nextLine();
				}
				msgGtr.close();
				return msg;
			} catch (Exception e) {
				return "I have nothing to say to you";
			}
		}
		return quote;
	}

	public static void makeInputBox() {
		input.setSize(400, 40);
		input.setBorder(BorderFactory.createLineBorder(Color.black));
		input.setLocation(100, 550);
	}
}