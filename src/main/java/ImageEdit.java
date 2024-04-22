import  java.awt.*;
import  java.awt.geom.*;
import  java.awt.event.*;
import  javax.swing.*;
import  java.awt.image.*;
import  java.util.*;
import  java.net.URL;

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
        f.setSize(350,350);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maincolor = Color.black;
         
        /*JMenuBar menuBar = new  JMenuBar();
        f.setJMenuBar(menuBar);
        menuBar.setBounds(0,0,350,30);
        JMenu fileMenu = new  JMenu("Режимы");
        menuBar.add(fileMenu);
         
        Action loadAction = new  AbstractAction("")
        {
           public void actionPerformed(ActionEvent event)
           {
              }
            };
        JMenuItem loadMenu = new  JMenuItem(loadAction);
        fileMenu.add(loadMenu);
         
        Action saveAction = new  AbstractAction("")
        {
           public void actionPerformed(ActionEvent event)
           {
               
           }
            };
        JMenuItem saveMenu = new  JMenuItem(saveAction);
        fileMenu.add(saveMenu);
         
        Action saveasAction = new  AbstractAction("")
        {
           public void actionPerformed(ActionEvent event)
           {
              
            };
        JMenuItem saveasMenu = new  JMenuItem(saveasAction);
        fileMenu.add(saveasMenu);*/
         
        japan = new  MyPanel();
        japan.setBounds(0,30,350,350);
        japan.setBackground(Color.white);
        japan.setOpaque(true);
        f.add(japan);
      
        JToolBar toolbar = new  JToolBar("Toolbar", JToolBar.HORIZONTAL);

        JTextArea outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        //outputTextArea.setLineWrap(true);
        //JScrollPane scrollPane = new JScrollPane(outputTextArea);
        toolbar.add(outputTextArea);
           
        JButton backbutton = new  JButton();
        URL backIconUrl = getClass().getResource("/resources/back.png");
          if (backIconUrl != null) {
            ImageIcon backIcon = new ImageIcon(backIconUrl);
            backbutton.setIcon(backIcon);
          }

        toolbar.add(backbutton);

        JButton pushresult = new  JButton();
        URL pushresultIconUrl = getClass().getResource("/resources/pushresult.png");
          if (pushresultIconUrl != null) {
            ImageIcon pushresultIcon = new ImageIcon(pushresultIconUrl);
            pushresult.setIcon(pushresultIcon);
          }
        toolbar.add(pushresult);

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
              outputTextArea.append(processedPixels);

              clearImage();
              history.clear();
            }
    
          });
                     
        toolbar.setBounds(0, 0, 350, 30);
        f.add(toolbar);
           
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

        f.setLayout(null);
        f.setVisible(true);
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
      g2.setColor(Color.white);
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
