import  java.awt.*;
import  java.awt.geom.*;
import  java.awt.event.*;
import  javax.swing.*;
import  java.awt.image.*;
import  java.util.*;
import  java.net.URL;
import  javax.swing.border.LineBorder;
import  java.io.BufferedReader;
import java.io.BufferedWriter;
import  java.io.FileReader;
import java.io.FileWriter;
import  java.io.IOException;


public class ImageEdit {
    private int xPad;
    private int yPad;
    private Insets insets;
    private Dimension screenSize;
    private int contentWidth;
    private int contentHeight;
    private int rightAnswers = 0;
    private int allAnswers = 0;
    private int mode = 0;
    private int chosenNumber;
    private boolean mouseClicked = false;
    private String[] names = new String[92];
    private int[] priority = new int[92];
    private JButton[] buttons = new JButton[92];
    private Color maincolor;
    private MyFrame f;
    private MyPanel japan;
    private BufferedImage imag;
    private Stack<BufferedImage> history = new Stack<>();

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
        f.setExtendedState(MyFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(Color.LIGHT_GRAY);
        japan = new  MyPanel();
        japan.setBounds(300, 130, 350, 350);
        japan.setBackground(Color.white);
        japan.setOpaque(true);
        f.add(japan);
        maincolor = Color.black;

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        insets = f.getInsets();
        contentWidth = (int)screenSize.getWidth() - insets.left - insets.right;
        contentHeight = (int)screenSize.getHeight() - insets.top - insets.bottom - 100;

        JToolBar menu = createToolbar("Menu", JToolBar.VERTICAL, 0, 0, 300, contentHeight, Color.LIGHT_GRAY);
        JToolBar taskWindow = createToolbar("TaskWindow", JToolBar.HORIZONTAL, 305, 5, contentWidth - 305, 60, Color.LIGHT_GRAY);
        JToolBar toolbar = createToolbar("ToolBar", JToolBar.HORIZONTAL, 305, 65, contentWidth - 305, 60, Color.LIGHT_GRAY);
        JToolBar hiraganaSymbols = createToolbar("HiraganaSymbols", JToolBar.HORIZONTAL, 305, 5, 600, 60, Color.LIGHT_GRAY);
        hiraganaSymbols.setLayout(new GridLayout(1, 2));
        hiraganaSymbols.setVisible(false);
        JToolBar katakanaSymbols = createToolbar("KatakanaSymbols", JToolBar.HORIZONTAL, 305, (contentHeight - 130) / 2 + 70, 600, 60, Color.LIGHT_GRAY);
        katakanaSymbols.setLayout(new GridLayout(1, 2));
        katakanaSymbols.setVisible(false);
        JScrollPane hiraganaPane = createPane(305, 65, contentWidth - 305, (contentHeight - 130) / 2);
        JScrollPane katakanaPane = createPane(305, (contentHeight - 130) / 2 + 130, contentWidth - 305, (contentHeight - 130) / 2);

        //menu
        JButton training = createButton("training", 300, 70, Color.black, menu);
        JButton test = createButton("test", 300, 70, Color.black, menu);
        JButton education = createButton("education", 300, 70, Color.black, menu);
        JLabel perfectSymbol = createLabel(300, contentHeight - 350, Color.gray, menu);
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
        JButton addHiragana = createButton("addHiragana", 300, 60, Color.black, hiraganaSymbols);
        JButton deleteHiragana = createButton("deleteHiragana", 300, 60, Color.black, hiraganaSymbols);
        JButton addKatakana = createButton("addKatakana", 300, 60, Color.black, katakanaSymbols);
        JButton deleteKatakana = createButton("deleteKatakana", 300, 60, Color.black, katakanaSymbols);
        for (int i = 0; i < 46; i++) {
          buttons[i] = createMassButtons(names[i], i, (JPanel)hiraganaPane.getViewport().getView());
          buttons[i + 46] = createMassButtons(names[i + 46] + "K", i + 46, (JPanel)katakanaPane.getViewport().getView());
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
              perfectSymbol.setIcon(null);
              hiraganaPane.setVisible(false);
              hiraganaSymbols.setVisible(false);
              katakanaPane.setVisible(false);
              katakanaSymbols.setVisible(false);

              mode = 2;
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

              /*chosenNumber = 1; 
              if(maincolor == Color.black){
                setLabelImage(names[chosenNumber], perfectSymbol);
              } else{
                setLabelImage(names[chosenNumber] + "B", perfectSymbol);
              }*/
              mode = 1;
            }
          });

        test.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) {
              taskWindow.setVisible(true);
              toolbar.setVisible(true);
              japan.setVisible(true);
              perfectSymbol.setIcon(null);
              hiraganaPane.setVisible(false);
              hiraganaSymbols.setVisible(false);
              katakanaPane.setVisible(false);
              katakanaSymbols.setVisible(false);

              mode = 0;
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
                f.getContentPane().setBackground(Color.DARK_GRAY);
                menu.setBackground(Color.DARK_GRAY);
                taskWindow.setBackground(Color.DARK_GRAY);
                toolbar.setBackground(Color.DARK_GRAY);
                hiraganaSymbols.setBackground(Color.DARK_GRAY);
                katakanaSymbols.setBackground(Color.DARK_GRAY);
                if(mode == 1){
                  setLabelImage(names[chosenNumber] + "B", perfectSymbol);
                }
                for(int i = 0; i < 46; i++){
                  setThisIcon(names[i] + "B", buttons[i]);
                  if(priority[i] == -1) {
                    buttons[i].setIcon(createTransparentIcon(buttons[i].getIcon()));
                  }
                  setThisIcon(names[i + 46] + "KB", buttons[i + 46]);
                  if(priority[i + 46] == -1) {
                    buttons[i + 46].setIcon(createTransparentIcon(buttons[i + 46].getIcon()));
                  }
                }
                maincolor = Color.white;
                clearImage();
                japan.setBackground(Color.white);
                history.clear();
              }
              else {
                f.getContentPane().setBackground(Color.LIGHT_GRAY);
                menu.setBackground(Color.LIGHT_GRAY);
                taskWindow.setBackground(Color.LIGHT_GRAY);
                toolbar.setBackground(Color.LIGHT_GRAY);
                hiraganaSymbols.setBackground(Color.LIGHT_GRAY);
                katakanaSymbols.setBackground(Color.LIGHT_GRAY);
                if(mode == 1){
                  setLabelImage(names[chosenNumber], perfectSymbol);
                }
                for(int i = 0; i < 46; i++){
                  setThisIcon(names[i], buttons[i]);
                  if(priority[i] == -1) {
                    buttons[i].setIcon(createTransparentIcon(buttons[i].getIcon()));
                  }
                  setThisIcon(names[i + 46] + "K", buttons[i + 46]);
                  if(priority[i + 46] == -1) {
                    buttons[i + 46].setIcon(createTransparentIcon(buttons[i + 46].getIcon()));
                  }
                }
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
                  if(maincolor == Color.black){
                    setThisIcon(names[i], buttons[i]);
                  } else{
                    setThisIcon(names[i] + "B", buttons[i]);
                  }
                  priority[i] = 0;
                }
              }
            }
          });

        deleteHiragana.addActionListener(new  ActionListener()
        {
          public void actionPerformed(ActionEvent event) { 
            for(int i = 0; i < 46; i++){
              if(priority[i] != -1){
                buttons[i].setIcon(createTransparentIcon(buttons[i].getIcon()));
                priority[i] = -1;
              }
            }
          }
        });

        addKatakana.addActionListener(new  ActionListener()
          {
            public void actionPerformed(ActionEvent event) { 
              for(int i = 46; i < 92; i++){
                if(priority[i] == -1){
                  if(maincolor == Color.black){
                    setThisIcon(names[i] + "K", buttons[i]);
                  } else{
                    setThisIcon(names[i] + "KB", buttons[i]);
                  }
                  priority[i] = 0;
                }
              }
            }
          });

        deleteKatakana.addActionListener(new  ActionListener()
        {
          public void actionPerformed(ActionEvent event) { 
            for(int i = 46; i < 92; i++){
              if(priority[i] != -1){
                buttons[i].setIcon(createTransparentIcon(buttons[i].getIcon()));
                priority[i] = -1;
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
        
        //frame actions
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

        f.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            if(e.getID() == WindowEvent.WINDOW_CLOSING){
              try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(""); 
                for (int i = 0; i < 92; i++) {
                  writer.write(names[i] + ' ' + String.valueOf(priority[i]));
                  writer.newLine();
                }
              } catch (IOException j) {
                j.printStackTrace();
              }
            }
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

    private JToolBar createToolbar(String name, int orientation, int x, int y, int width, int height, Color color) {
      JToolBar toolbar = new  JToolBar(name, orientation);
      toolbar.setBounds(x, y, width, height);
      toolbar.setBorderPainted(false);
      toolbar.setFloatable(false);
      toolbar.setBackground(color);
      f.add(toolbar);
      return toolbar;
    }

    private JScrollPane createPane(int x, int y, int width, int height) {
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0, (contentWidth - 305) / 200));
      panel.setBounds(x, y ,width, height);
      panel.setBackground(Color.DARK_GRAY);
      JScrollPane pane = new JScrollPane(panel);
      pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      pane.setBounds(x, y ,width, height);
      pane.setVisible(false);
      f.add(pane);
      return pane;
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
      button.setHorizontalAlignment(SwingConstants.LEFT);
      button.setVerticalAlignment(SwingConstants.BOTTOM);
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

    private JLabel createLabel(int width, int height, Color color, JToolBar bar) {
      JLabel label = new JLabel("", SwingConstants.CENTER);
      label.setPreferredSize(new Dimension(width, height));
      label.setMaximumSize(new Dimension(width, height));
      label.setBackground(color);
      bar.add(label);
      return label;
    }

    private JButton createMassButtons(String name, int index, JPanel bar) {
      JButton button = new JButton(name);
      setThisIcon(name, button);
      if(priority[index] == -1){
        button.setIcon(createTransparentIcon(button.getIcon()));
      }
      button.setPreferredSize(new Dimension(200, 200));
      button.setMaximumSize(new Dimension(200, 200));
      button.setBorder(new LineBorder(Color.black));
      button.setHorizontalAlignment(SwingConstants.LEFT);
      button.setVerticalAlignment(SwingConstants.BOTTOM);
      bar.add(button);

      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(priority[index] != -1){
            button.setIcon(createTransparentIcon(button.getIcon()));
            priority[index] = -1;
          } else{
            if(maincolor == Color.white) {
              setThisIcon(name + "B", button);
            } else{
              setThisIcon(name, button);
            }
            priority[index] = 0;
          }
        }
      });
      return button;
    }

    public static Icon createTransparentIcon(Icon icon) { //get transparent icon
      
      BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();
      icon.paintIcon(null, g2d, 0, 0);
      g2d.dispose();

      int width = image.getWidth();
      int height = image.getHeight();
      BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2dTransparent = transparentImage.createGraphics();
      g2dTransparent.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
      g2dTransparent.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2dTransparent.drawImage(image, 0, 0, null);
      g2dTransparent.dispose();

      return new ImageIcon(transparentImage);
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

    private void setThisIcon(String name, JButton button) {
      URL IconUrl = getClass().getResource("/alphabet/" + name + ".png");
      if (IconUrl != null) {
        ImageIcon Icon = new ImageIcon(IconUrl);
        button.setIcon(Icon);
        }
    }

    private void setLabelImage(String name, JLabel label) {
      URL IconUrl = getClass().getResource("/alphabet/" + name + ".png");
      if (IconUrl != null) {
        ImageIcon Icon = new ImageIcon(IconUrl);
        label.setIcon(Icon);
        }
    }
  
    class MyFrame extends JFrame {
      public void paint(Graphics g) {
        super.paint(g);
      }

      public MyFrame(String title) {
        super(title);
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

    public class CalligraphicStroke extends BasicStroke { //Brush
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