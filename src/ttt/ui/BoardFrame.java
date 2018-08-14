/**
 * 
 */
package ttt.ui;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;;
/**
 * Diese klasse erweitert JFrame. Als ContentPane wird BoardPanel benutzt.
 * @author Omar Sharaki
 *
 */
public class BoardFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2697296855875521183L;
	/**
	 * Wird benutzt als ContentPane fuer dieses Fenster.
	 */
	private BoardPanel panel;
	
	
	public BoardFrame() {
		panel=new BoardPanel();
		setJMenuBar(panel.getBar());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(panel);
		setTitle("TicTacToe");
		setResizable(false);
		pack();
		setVisible(true);
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
            	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            	        if ("Nimbus".equals(info.getName())) {
            	            UIManager.setLookAndFeel(info.getClassName());
            	            break;
            	        }
            	    }
            	    
            	} catch (Exception e) {
            	    // If Nimbus is not available, you can set the GUI to another look and feel.
            	}
            	new BoardFrame();
            }
        });
	}
}
