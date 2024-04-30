import  java.awt.*;
import  java.awt.geom.*;
import  java.awt.event.*;
import  javax.swing.*;
import  java.awt.image.*;
import  java.util.*;
import  java.net.URL;
import javax.swing.border.LineBorder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ImageEdit {
    int xPad;
    int yPad;
    int rightAnswers = 0;
    int allAnswers = 0;
    boolean mouseClicked = false;
    String[] names = new String[92];
    int[] priority = new int[92];
    JButton[] buttons = new JButton[92];
    
    Color maincolor;
    MyFrame f;
    MyPanel japan;
    BufferedImage imag;
    Stack<BufferedImage> history = new Stack<>();

    public ImageEdit() {

        String fileName = "src/main/resources/alphabet.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
          String line;
          int index = 0;
          while ((line = reader.readLine()) != null && index < 92) {
              String[] parts = line.split("\\s+");
              if (parts.length == 2) {
                  names[index] = parts[0];
                  priority[index] = Integer.parseInt(parts[1]);
                  index++;
              }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      
        f = new MyFrame("MLJapanese");
        f.setSize(1000,700);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(Color.gray);
        maincolor = Color.black;
         
        japan = new  MyPanel();
        japan.setBounds(300, 130, 350, 350);
        japan.setBackground(Color.white);
        japan.setOpaque(true);
        f.add(japan);

        JToolBar menu = new  JToolBar("Menu", JToolBar.VERTICAL);
        menu.setBounds(0, 0, 300, 1000);
        menu.setBorderPainted(false);
        menu.setFloatable(false);
        menu.setBackground(Color.gray);
        f.add(menu);

        JToolBar taskWindow = new  JToolBar("TaskWindow", JToolBar.HORIZONTAL);
        taskWindow.setBounds(300, 5, 1500, 60);
        taskWindow.setBorderPainted(false);
        taskWindow.setFloatable(false);
        taskWindow.setBackground(Color.gray);
        f.add(taskWindow);

        JToolBar toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
        toolbar.setBounds(300, 65, 1500, 60);
        toolbar.setBorderPainted(false);
        toolbar.setFloatable(false);
        toolbar.setBackground(Color.gray);
        f.add(toolbar);

        JToolBar hiraganaSymbols = new JToolBar("hiraganaSymbols", JToolBar.HORIZONTAL);
        hiraganaSymbols.setLayout(new GridLayout(1, 2));
        hiraganaSymbols.setBounds(300, 5, 1600, 60);
        hiraganaSymbols.setBorderPainted(false);
        hiraganaSymbols.setFloatable(false);
        hiraganaSymbols.setBackground(Color.gray);
        hiraganaSymbols.setVisible(false);
        f.add(hiraganaSymbols);

        JPanel hiraganaChoice = new JPanel();
        hiraganaChoice.setLayout(new GridLayout(0, 8));
        hiraganaChoice.setBounds(300, 65, 1600, 400);
        hiraganaChoice.setBackground(Color.gray);
        JScrollPane hiraganaPane = new JScrollPane(hiraganaChoice);
        hiraganaPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        hiraganaPane.setBounds(300, 65, 1600, 400);
        hiraganaPane.setVisible(false);
        f.add(hiraganaPane);

        JToolBar katakanaSymbols = new JToolBar("katakanaSymbols", JToolBar.HORIZONTAL);
        katakanaSymbols.setLayout(new GridLayout(1, 2));
        katakanaSymbols.setBounds(300, 480, 1600, 60);
        katakanaSymbols.setBorderPainted(false);
        katakanaSymbols.setFloatable(false);
        katakanaSymbols.setBackground(Color.gray);
        katakanaSymbols.setVisible(false);
        f.add(katakanaSymbols);

        JPanel katakanaChoice = new JPanel();
        katakanaChoice.setLayout(new GridLayout(0, 8));
        katakanaChoice.setBounds(300, 540, 1600, 400);
        katakanaChoice.setBackground(Color.gray);
        JScrollPane katakanaPane = new JScrollPane(katakanaChoice);
        katakanaPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        katakanaPane.setBounds(300, 540, 1600, 400);
        katakanaPane.setVisible(false);
        f.add(katakanaPane);

        //menu
        JButton training = createButton("training", 300, 70, Color.black, menu);
        JButton education = createButton("education", 300, 70, Color.black, menu);
        JButton test = createButton("test", 300, 70, Color.black, menu);
        JButton hieroglyphs = createButton("hieroglyphs", 300, 70, Color.black, menu);
        JButton themeColor = createButton("themeColor", 300, 70, Color.black, menu);
        //taskWindow
        JTextArea outputTextArea = createTextArea(900, 60, taskWindow);  
        //toolbar
        JButton backbutton = createButton("back", 60, 60, Color.black, toolbar);
        JButton pushresult = createButton("pushresult", 60, 60, Color.black, toolbar);
        JTextArea statisticTextArea = createTextArea(90, 60, toolbar);
        //statisticTextArea.setForeground(Color.BLUE);
        JButton resetStatistic = createButton("resetStatistic", 60, 60, Color.black, toolbar);
        //hieroglyphsPanes
        JButton addHiragana = createButton("addHiragana", 800, 60, Color.black, hiraganaSymbols);
        JButton deleteHiragana = createButton("deleteHiragana", 800, 60, Color.black, hiraganaSymbols);
        JButton addKatakana = createButton("addKatakana", 800, 60, Color.black, katakanaSymbols);
        JButton deleteKatakana = createButton("deleteKatakana", 800, 60, Color.black, katakanaSymbols);
        for (int i = 0; i < 46; i++) {
          buttons[i] = createMassButtons(names[i], i, hiraganaChoice);
          buttons[i + 46] = createMassButtons(names[i + 46] + "K", i + 46, katakanaChoice);
        }


        f.setLayout(null);
        f.setVisible(true);

        //menu actions
        training.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) {
              taskWindow.setVisible(true);
              toolbar.setVisible(true);
              japan.setVisible(true);
              hiraganaPane.setVisible(false);
              hiraganaSymbols.setVisible(false);
              katakanaPane.setVisible(false);
              katakanaSymbols.setVisible(false);
            }
          });

        education.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) {
              taskWindow.setVisible(true);
              toolbar.setVisible(true);
              japan.setVisible(true);
              hiraganaPane.setVisible(false);
              hiraganaSymbols.setVisible(false);
              katakanaPane.setVisible(false);
              katakanaSymbols.setVisible(false);
            }
          });

        test.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) {
              taskWindow.setVisible(true);
              toolbar.setVisible(true);
              japan.setVisible(true);
              hiraganaPane.setVisible(false);
              hiraganaSymbols.setVisible(false);
              katakanaPane.setVisible(false);
              katakanaSymbols.setVisible(false);
            }
          });

        hieroglyphs.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) {
              taskWindow.setVisible(false);
              toolbar.setVisible(false);
              japan.setVisible(false);
              hiraganaPane.setVisible(true);
              hiraganaSymbols.setVisible(true);
              katakanaPane.setVisible(true);
              katakanaSymbols.setVisible(true);
            }
          });

        themeColor.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) { 
              if (maincolor == Color.black) {
                maincolor = Color.white;
                clearImage();
                japan.setBackground(Color.white);
                history.clear();
              }
              else {
                maincolor = Color.black;
                clearImage();
                japan.setBackground(Color.white);
                history.clear();
              }
            }

          });

        //toolbar actions
        backbutton.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) { 
              if (!history.isEmpty()) {
                BufferedImage previousImage = history.pop();
                imag = previousImage;
                japan.repaint();
              }
              else {
                clearImage();
              }
            }

          });
          
        
        pushresult.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) {
              String processedPixels = NeuroBridge.recognizeSymbol(imag,false);
              outputTextArea.setText(null);
              outputTextArea.append(processedPixels);

              clearImage();
              history.clear();
            }
    
          });
        
        //hieroglyphsPanes actions
        addHiragana.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) { 
              for(int i = 0; i < 46; i++){
                if(priority[i] == -1){
                  buttons[i].doClick();
                }
              }
            }
          });

        deleteHiragana.addActionListener(new  ActionListener()
        {
          public void actionPerformed(ActionEvent event) { 
            for(int i = 0; i < 46; i++){
              if(priority[i] != -1){
                buttons[i].doClick();
              }
            }
          }
        });

        addKatakana.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) { 
              for(int i = 46; i < 92; i++){
                if(priority[i] == -1){
                  buttons[i].doClick();
                }
              }
            }
          });

        deleteKatakana.addActionListener(new  ActionListener()
        {
          public void actionPerformed(ActionEvent event) { 
            for(int i = 46; i < 92; i++){
              if(priority[i] != -1){
                buttons[i].doClick();
              }
            }
          }
        });
        
        //canvas actions
        japan.addMouseMotionListener(new  MouseMotionAdapter()
          {
            public void mouseDragged(MouseEvent e) { 
              if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                Graphics2D g2 = imag.createGraphics();
                g2.setColor(maincolor);
                g2.setStroke(new  CalligraphicStroke(10.0f));

                if(mouseClicked){
                  BufferedImage currentImageCopy = deepCopy(imag);
                  history.push(currentImageCopy);
                  mouseClicked = false;
                }
                
                g2.drawLine(xPad + 2, yPad + 2, e.getX() + 2, e.getY() + 2);
                g2.drawLine(xPad + 7, yPad + 7, e.getX() + 7, e.getY() + 7);
                g2.drawLine(xPad - 2, yPad - 2, e.getX() - 2, e.getY() - 2);
                g2.drawLine(xPad - 7, yPad - 7, e.getX() - 7, e.getY() - 7);
                      
                xPad=e.getX();
                yPad=e.getY();
                
                g2.dispose();
                japan.repaint();
              }
            }
          });

        japan.addMouseListener(new  MouseAdapter()
          {
            public void mousePressed(MouseEvent e) {
              if(e.getID() == MouseEvent.MOUSE_PRESSED) {          
                xPad = e.getX();
                yPad = e.getY();
                mouseClicked = true;
              }
            }
          });
        
        //размер окна
        f.addComponentListener(new  ComponentAdapter()
          {
            public void componentResized(java.awt.event.ComponentEvent evt) {
              japan.setSize(f.getWidth(), f.getHeight()-80);
              BufferedImage tempImage = new  BufferedImage(japan.getWidth(), japan.getHeight(), BufferedImage.TYPE_INT_RGB);
              Graphics2D d2 = (Graphics2D) tempImage.createGraphics();
              d2.setColor(Color.white);
              d2.fillRect(0, 0, japan.getWidth(), japan.getHeight());
              d2.drawImage(imag, 0, 0, japan.getWidth(), japan.getHeight(), null);
              imag=tempImage;
              japan.repaint();
            }
          });
    }


    public static void main(String[] args) {
      SwingUtilities.invokeLater(new  Runnable() {
        public void run() {
        new  ImageEdit();
        }
      });        
    }

    private JButton createButton(String name, int width, int height, Color color, JToolBar bar) {
      JButton button = new JButton(name);
      URL IconUrl = getClass().getResource(name + ".png");
      if (IconUrl != null) {
        ImageIcon Icon = new ImageIcon(IconUrl);
        button.setIcon(Icon);
      }
      button.setPreferredSize(new Dimension(width, height));
      button.setMaximumSize(new Dimension(width, height));
      button.setBorder(new LineBorder(color));
      bar.add(button); 
      return button;
    }

    private JTextArea createTextArea(int width, int height, JToolBar bar) {
      JTextArea textArea = new JTextArea();
      textArea.setEditable(false);
      textArea.setBackground(Color.white);
      textArea.setBorder(new LineBorder(Color.black));
      textArea.setPreferredSize(new Dimension(width, height));
      textArea.setMaximumSize(new Dimension(width, height));
      bar.add(textArea);
      return textArea;
    }

    private JButton createMassButtons(String name, int index, JPanel bar) {
      JButton button = new JButton(name);
      URL IconUrl = getClass().getResource("/alphabet/" + name + ".png");
      if (IconUrl != null) {
        ImageIcon Icon = new ImageIcon(IconUrl);
        button.setIcon(Icon);
      }
      button.setPreferredSize(new Dimension(200, 200));
      button.setMaximumSize(new Dimension(200, 200));
      button.setBorder(new LineBorder(Color.black));
      bar.add(button);
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          URL IconUrl = getClass().getResource("/alphabet/" + name + ".png");
          URL IconBUrl = getClass().getResource("/alphabet/" + name + "B.png");
            if(priority[index] != -1){
              if (IconBUrl != null) {
                ImageIcon Icon = new ImageIcon(IconBUrl);
                button.setIcon(Icon);
              }
              priority[index] = -1;
            } else{
              if (IconUrl != null) {
                ImageIcon Icon = new ImageIcon(IconUrl);
                button.setIcon(Icon);
              }
              priority[index] = 0;
            }
        }
      });
      return button;
    }

    private BufferedImage deepCopy(BufferedImage image) {
      ColorModel colorModel = image.getColorModel();
      boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
      WritableRaster raster = image.copyData(null);
      return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    private void clearImage() {
      Graphics2D g2 = imag.createGraphics();
      if(maincolor == Color.black){
        g2.setColor(Color.white);
      }
      else{
        g2.setColor(Color.black);
      }
      g2.fillRect(0, 0, imag.getWidth(), imag.getHeight());
      g2.dispose();
      japan.repaint();
    }
  
    class MyFrame extends JFrame {
      public void paint(Graphics g) {
        super.paint(g);
      }

      public MyFrame(String title) {
        super(title);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(0, 0);
      }
    }
 
    class MyPanel extends JPanel {
      public MyPanel(){}

      public void paintComponent (Graphics g) {
        super.paintComponent(g);
        if(imag==null) {
          imag = new  BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
          Graphics2D d2 = (Graphics2D) imag.createGraphics();
          d2.setColor(Color.white);
          d2.fillRect(0, 0, imag.getWidth(), imag.getHeight());
        }

        g.drawImage(imag, 0, 0, getWidth(), getHeight(), null);      
      }
    }

    public class CalligraphicStroke extends BasicStroke { //Кисть
      private float size;
  
      public CalligraphicStroke(float size) {
          super(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
          this.size = size;
      }
  
      @Override
      public Shape createStrokedShape(Shape shape) {
          GeneralPath path = new GeneralPath();
          BasicStroke stroke1 = new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
          path.append(stroke1.createStrokedShape(shape), false);
        
          return path;
      }
  }
     
}