import  java.awt.*;
import  java.awt.event.*;
import  java.io.*;
import  javax.swing.*;
import  java.awt.image.*;
import  javax.imageio.*;
import  java.util.Stack;
import  java.net.URL;

public class ImageEdit
{
    int  xPad;
    int  xf;
    int  yf;
    int  yPad;
    boolean pressed=false;
    Color maincolor;
    MyFrame f;
    MyPanel japan;
    BufferedImage imag;
    Stack<BufferedImage> history = new Stack<>();

    public ImageEdit()
    {
        f=new MyFrame("Графический редактор");
        f.setSize(350,350);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maincolor=Color.black;
         
        JMenuBar menuBar = new  JMenuBar();
        f.setJMenuBar(menuBar);
        menuBar.setBounds(0,0,350,30);
        JMenu fileMenu = new  JMenu("Режимы");
        menuBar.add(fileMenu);
         
        /*Action loadAction = new  AbstractAction("")
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
        japan.setBounds(0,30,256,256);
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
          URL backIconUrl = getClass().getResource("/back.png");
          if (backIconUrl != null) {
            ImageIcon backIcon = new ImageIcon(backIconUrl);
            backbutton.setIcon(backIcon);
          }

          backbutton.addActionListener(new  ActionListener()
            {
              public void actionPerformed(ActionEvent event)
              { 
                if (!history.isEmpty()) {
                  BufferedImage previousImage = history.pop();
                  imag = previousImage;
                  japan.repaint();
              }
              else
              {
                clearImage();
              }
              }

            });
          toolbar.add(backbutton);

          JButton pushresult = new  JButton();
          URL pushresultIconUrl = getClass().getResource("/pushresult.png");
          if (pushresultIconUrl != null) {
            ImageIcon pushresultIcon = new ImageIcon(pushresultIconUrl);
            pushresult.setIcon(pushresultIcon);
          }

          pushresult.addActionListener(new  ActionListener()
            {
              public void actionPerformed(ActionEvent event)
              {
                String processedPixels = ImageProcessor.processImage(imag);
                outputTextArea.append("processedPixels");

                clearImage();
                history.clear();
              }
    
          });
          toolbar.add(pushresult);
           
          toolbar.setBounds(0, 0, 350, 30);
          f.add(toolbar);
           
          japan.addMouseMotionListener(new  MouseMotionAdapter()
                  {
                      public void mouseDragged(MouseEvent e) 
                      { 
                          if (pressed)
                          {
                          //Graphics g = imag.getGraphics();
                          Graphics2D g2 = imag.createGraphics();
                          g2.setColor(maincolor);
                          
                          g2.setStroke(new  BasicStroke(25.0f));
                          g2.drawLine(xPad, yPad, e.getX(), e.getY());
                      
                          xPad=e.getX();
                          yPad=e.getY();
                          g2.dispose();
                          japan.repaint();

                          }
                      }
                  });
          japan.addMouseListener(new  MouseAdapter()
                  {
                     public void mouseClicked(MouseEvent e) {
                           
                     //Graphics g = imag.getGraphics();
                     /*Graphics2D g2 = imag.createGraphics();
                     
                          g2.setColor(maincolor);
                          
                          g2.setStroke(new  BasicStroke(3.0f));
                          g2.drawLine(xPad, yPad, xPad+1, yPad+1);
                          
                          xPad=e.getX();
                          yPad=e.getY();*/
                           
                          pressed=true;
                          japan.repaint();

                   }
                      public void mousePressed(MouseEvent e) {
                         xPad=e.getX();
                          yPad=e.getY();
                          xf=e.getX();
                          yf=e.getY();
                          pressed=true;
                        }

                      public void mouseReleased(MouseEvent e) {
                          if (pressed) {
                              BufferedImage currentImageCopy = deepCopy(imag);
                              history.push(currentImageCopy);
                              pressed = false;
                          }
                      }
                    
                  });
                  
        f.addComponentListener(new  ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                  
                    japan.setSize(f.getWidth(), f.getHeight()-80);
                    BufferedImage tempImage = new  BufferedImage(japan.getWidth(), japan.getHeight(), BufferedImage.TYPE_INT_RGB);
                             Graphics2D d2 = (Graphics2D) tempImage.createGraphics();
                        d2.setColor(Color.white);
                        d2.fillRect(0, 0, japan.getWidth(), japan.getHeight());
                    tempImage.setData(imag.getRaster());
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
  
     class MyFrame extends JFrame
     {
      public void paint(Graphics g) {
        super.paint(g);
    }

    public MyFrame(String title) {
        super(title);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setLocation(0, 0);
    }
     }
 
     class MyPanel extends JPanel
     {
       public MyPanel(){}

       public void paintComponent (Graphics g)
          {
            super.paintComponent(g);
            if(imag==null)
             {
                 imag = new  BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                 Graphics2D d2 = (Graphics2D) imag.createGraphics();
                 d2.setColor(Color.white);
                 d2.fillRect(0, 0, imag.getWidth(), imag.getHeight());
             }

             g.drawImage(imag, 0, 0, getWidth(), getHeight(), null);      
          }
     }
     
}
