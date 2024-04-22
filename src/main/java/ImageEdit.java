import  java.awt.*;
import  java.awt.geom.*;
import  java.awt.event.*;
import  javax.swing.*;
import  java.awt.image.*;
import  java.util.*;
import  java.net.URL;
import javax.swing.border.LineBorder;

public class ImageEdit {
    int  xPad;
    int  yPad;
    boolean mouseClicked = false;
    
    Color maincolor;
    MyFrame f;
    MyPanel japan;
    BufferedImage imag;
    Stack<BufferedImage> history = new Stack<>();

    public ImageEdit() {

        f = new MyFrame("MLJapanese");
        f.setSize(1000,700);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(Color.gray);
        maincolor = Color.black;
         
        japan = new  MyPanel();
        japan.setBounds(300,130,350,350);
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

        //menu
        JButton training = new JButton();
        URL trainingIconUrl = getClass().getResource("/resources/training.png");
        if (trainingIconUrl != null) {
          ImageIcon trainingIcon = new ImageIcon(trainingIconUrl);
          training.setIcon(trainingIcon);
        }
        training.setPreferredSize(new Dimension(300, 70));
        training.setMaximumSize(new Dimension(300, 70));
        training.setBorder(new LineBorder(Color.black));
        menu.add(training);

        JButton education = new JButton();
        URL educationIconUrl = getClass().getResource("/resources/education.png");
        if (educationIconUrl != null) {
          ImageIcon educationIcon = new ImageIcon(educationIconUrl);
          education.setIcon(educationIcon);
        }
        education.setPreferredSize(new Dimension(300, 70));
        education.setMaximumSize(new Dimension(300, 70));
        education.setBorder(new LineBorder(Color.black));
        menu.add(education);

        JButton test = new JButton();
        URL testIconUrl = getClass().getResource("/resources/test.png");
        if (testIconUrl != null) {
          ImageIcon testIcon = new ImageIcon(testIconUrl);
          test.setIcon(testIcon);
        }
        test.setPreferredSize(new Dimension(300, 70));
        test.setMaximumSize(new Dimension(300, 70));
        test.setBorder(new LineBorder(Color.black));
        menu.add(test);

        JButton hieroglyphs = new JButton();
        URL hieroglyphsIconUrl = getClass().getResource("/resources/hieroglyphs.png");
        if (hieroglyphsIconUrl != null) {
          ImageIcon hieroglyphsIcon = new ImageIcon(hieroglyphsIconUrl);
          hieroglyphs.setIcon(hieroglyphsIcon);
        }
        hieroglyphs.setPreferredSize(new Dimension(300, 70));
        hieroglyphs.setMaximumSize(new Dimension(300, 70));
        hieroglyphs.setBorder(new LineBorder(Color.black));
        menu.add(hieroglyphs);

        JButton themeColor = new JButton();
        URL themeColorIconUrl = getClass().getResource("/resources/themeColor.png");
        if (themeColorIconUrl != null) {
          ImageIcon themeColorIcon = new ImageIcon(themeColorIconUrl);
          themeColor.setIcon(themeColorIcon);
        }
        themeColor.setPreferredSize(new Dimension(300, 70));
        themeColor.setMaximumSize(new Dimension(300, 70));
        themeColor.setBorder(new LineBorder(Color.black));
        menu.add(themeColor);

        //taskWindow
        JTextArea outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(Color.white);
        outputTextArea.setBorder(new LineBorder(Color.black));
        outputTextArea.setPreferredSize(new Dimension(900, 60));
        outputTextArea.setMaximumSize(new Dimension(900, 60));
        taskWindow.add(outputTextArea);

        //toolbar
        JButton backbutton = new JButton();
        URL backIconUrl = getClass().getResource("/resources/back.png");
        if (backIconUrl != null) {
          ImageIcon backIcon = new ImageIcon(backIconUrl);
          backbutton.setIcon(backIcon);
        }
        backbutton.setPreferredSize(new Dimension(60, 60));
        backbutton.setMaximumSize(new Dimension(60, 60));
        backbutton.setBorder(new LineBorder(Color.black));
        toolbar.add(backbutton);

        JButton pushresult = new JButton();
        URL pushresultIconUrl = getClass().getResource("/resources/pushresult.png");
        if (pushresultIconUrl != null) {
          ImageIcon pushresultIcon = new ImageIcon(pushresultIconUrl);
          pushresult.setIcon(pushresultIcon);
        }
        pushresult.setPreferredSize(new Dimension(60, 60));
        pushresult.setMaximumSize(new Dimension(60, 60));
        pushresult.setBorder(new LineBorder(Color.black));
        toolbar.add(pushresult);

        JTextArea statisticTextArea = new JTextArea();
        statisticTextArea.setEditable(false);
        statisticTextArea.setBackground(Color.white);
        statisticTextArea.setBorder(new LineBorder(Color.black));
        statisticTextArea.setPreferredSize(new Dimension(90, 60));
        statisticTextArea.setMaximumSize(new Dimension(90, 60));
        //statisticTextArea.setForeground(Color.BLUE);
        toolbar.add(statisticTextArea);

        JButton resetStatistic = new JButton();
        URL resetStatisticIconUrl = getClass().getResource("/resources/resetStatistic.png");
        if (resetStatisticIconUrl != null) {
          ImageIcon resetStatisticIcon = new ImageIcon(resetStatisticIconUrl);
          resetStatistic.setIcon(resetStatisticIcon);
        }
        resetStatistic.setPreferredSize(new Dimension(60, 60));
        resetStatistic.setMaximumSize(new Dimension(60, 60));
        resetStatistic.setBorder(new LineBorder(Color.black));
        toolbar.add(resetStatistic);

        f.setLayout(null);
        f.setVisible(true);

        //menu actions
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
              String processedPixels = ImageProcessor.processImage(imag);
              outputTextArea.setText(null);
              outputTextArea.append(processedPixels);

              clearImage();
              history.clear();
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
                
                //g2.drawLine(xPad, yPad, e.getX(), e.getY());
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

    public class CalligraphicStroke extends BasicStroke { //Адекватный класс
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

  //Хтонический ужас
  /*public class CalligraphicStroke implements Stroke {
    private final float diameter;
    private final float angle;

    public CalligraphicStroke(float diameter, float angle) {
        this.diameter = diameter;
        this.angle = angle;
    }

    public Shape createStrokedShape(Shape shape) {
        GeneralPath result = new GeneralPath();
        PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), 1);
        float[] coords = new float[6];
        float prevTanAngle = 0;

        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            float x = coords[0];
            float y = coords[1];

            if (type != PathIterator.SEG_CLOSE) {
                float tanAngle = (float) Math.atan2(coords[3] - coords[1], coords[2] - coords[0]);
                float angle1 = tanAngle + prevTanAngle - angle;
                float angle2 = tanAngle + prevTanAngle + angle;
                float cos1 = (float) Math.cos(angle1);
                float sin1 = (float) Math.sin(angle1);
                float cos2 = (float) Math.cos(angle2);
                float sin2 = (float) Math.sin(angle2);
                float x1 = x + diameter * cos1;
                float y1 = y + diameter * sin1;
                float x2 = x + diameter * cos2;
                float y2 = y + diameter * sin2;
                float controlX = x + diameter * (float) Math.cos(tanAngle);
                float controlY = y + diameter * (float) Math.sin(tanAngle);

                if (type == PathIterator.SEG_MOVETO) {
                    result.moveTo(x1, y1);
                } else {
                    result.curveTo(x1, y1, controlX, controlY, x2, y2);
                }
            }

            prevTanAngle = (type == PathIterator.SEG_CLOSE) ? 0 : (float) Math.atan2(coords[coords.length - 1] - coords[1], coords[coords.length - 2] - coords[0]);
            it.next();
        }

        return result;
    }
}*/
     
}