import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class SimpleAnimation extends JPanel {
   public static final int TIMER_DELAY = 1000;
   private JTextField textField = new JTextField(10);
   private JLabel displayLabel = new JLabel("", SwingConstants.CENTER);

   public SimpleAnimation() {
      Action btnAction = new DoItBtnAction("Do It!", KeyEvent.VK_D);
      JPanel topPanel = new JPanel();
      topPanel.add(textField);
      topPanel.add(new JButton(btnAction));
      textField.addActionListener(btnAction);

      setLayout(new GridLayout(2, 1));
      add(topPanel);
      add(displayLabel);
   }

   private class DoItBtnAction extends AbstractAction {
      private String textFieldText = "";
      public DoItBtnAction(String name, int mnemonic) {
         super(name);
         putValue(MNEMONIC_KEY, mnemonic);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         displayLabel.setText("");
         setEnabled(false);
         textFieldText = textField.getText();
         new Timer(TIMER_DELAY, new ActionListener() {
            private int i = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
               if (i >= textFieldText.length()) {
                  ((Timer) e.getSource()).stop();
                  DoItBtnAction.this.setEnabled(true);
               } else {
                  displayLabel.setText(displayLabel.getText() + textFieldText.charAt(i));
                  i++;
               }
            }
         }).start();
      }
   }

   private static void createAndShowGui() {
      JFrame frame = new JFrame("SimpleAnimation");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new SimpleAnimation());
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
}