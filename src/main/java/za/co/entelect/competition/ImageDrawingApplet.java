package za.co.entelect.competition;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

import javax.imageio.*;
import javax.swing.*;

class ImageDrawingComponent extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage[][] map;
	private BufferedImage tank1A;
	private BufferedImage tank1B;
	private BufferedImage base1;
	private BufferedImage tank2A;
	private BufferedImage tank2B;
	private BufferedImage base2;
	private BufferedImage wall;
	private BufferedImage bullet;
	private BufferedImage empty;
	private ArrayList<BufferedImage> bullets;
	private int blockSize = 16;
	int timercount = 0;
	
	double tank1Ax = 0;
	
	public void setTank1Ax(double newX) {
		tank1Ax = newX;
	}
	
	public double getTank1Ax() {
		return tank1Ax;
	}

	public ImageDrawingComponent() {
		tank1A = initImage(".\\assets\\Tank1A.png");
		tank1B = initImage(".\\assets\\Tank1B.png");
		base1 = initImage(".\\assets\\Base1.png");
		tank2A = initImage(".\\assets\\Tank2A.png");
		tank2B = initImage(".\\assets\\Tank2B.png");
		base2 = initImage(".\\assets\\Base2.png");
		wall = initImage(".\\assets\\Wall.png");
		bullet = initImage(".\\assets\\Bullet.png");
		empty = initImage(".\\assets\\Empty.png");
	}

	private BufferedImage initImage(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
			if (image.getType() != BufferedImage.TYPE_INT_RGB) {
				BufferedImage bi2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics big = bi2.getGraphics();
				big.drawImage(image, 0, 0, null);
				image = bi2;
			}
		} catch (IOException e) {
			System.out.println("Image could not be read");
			//            System.exit(1);
		}
		return image;	
	}

	public Dimension getPreferredSize() {
		return new Dimension(16*13, 16*13);
	}
	//
	//    static String[] getDescriptions() {
	//        return descs;
	//    }
	//
	//    void setOpIndex(int i) {
	//        opIndex = i;
	//    }

	private void drawGrid(Graphics g, BufferedImage img, double x, double y, int rotation, double scale, int frame, int numFrames) {		
		Graphics2D g2 = (Graphics2D) g;	
		
		if (numFrames != 1) {	
			img = img.getSubimage((img.getWidth()*frame/numFrames), 0, img.getWidth()/numFrames, img.getHeight());
		}
		
		if (rotation != 0) {		
			double rotationRequired = rotation*Math.PI/2;
			double locationX = img.getWidth() / 2;
			double locationY = img.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);		
			img = op.filter(img, null);
		}
		
		if (scale != 1) {
			AffineTransform tx = AffineTransform.getScaleInstance(scale, scale);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			img = op.filter(img, null);
		}
		
		g2.drawImage(img, (int)Math.round(x*blockSize*scale), (int)Math.round(y*blockSize*scale), null);
	}

	/* In this example the image is recalculated on the fly every time
	 * This makes sense where repaints are infrequent or will use a
	 * different filter/op from the last.
	 * In other cases it may make sense to "cache" the results of the
	 * operation so that unless 'opIndex' changes, drawing is always a
	 * simple copy.
	 * In such a case create the cached image and directly apply the filter
	 * to it and retain the resulting image to be repainted.
	 * The resulting image if untouched and unchanged Java 2D may potentially
	 * use hardware features to accelerate the blit.
	 */
	public void paint(Graphics g) {

		byte[][] bMap = {
				{0,1,0,0,0,1,0},
				{0,0,0,0,0,1,0},
				{1,1,1,0,0,0,0},
				{1,1,1,0,0,1,0},
				{1,0,1,0,1,1,0},
				{0,0,0,0,0,1,0},
				{1,0,1,0,0,0,0},
		};

		double scale = 2;
		for (int y = 0; y < bMap.length; y++) {
			for (int x = 0; x < bMap[0].length; x++) {
				if (bMap[y][x] == 1) {
					drawGrid(g, wall, x, y, 0, scale, timercount%2, 1);
				} else {
					drawGrid(g, empty, x, y, 0, scale, timercount%2, 1);
				} 
			}
		}
		
		//Graphics2D g2 = (Graphics2D) g;
		drawGrid(g, tank1B, 1, 0, 1, scale, timercount%2, 1);
		drawGrid(g, base1, 2, 0, 0, scale, timercount%2, 1);
		drawGrid(g, tank2A, 0, 1, 2, scale, timercount%2, 1);
		drawGrid(g, tank2B, 1, 1, 3, scale, timercount%2, 1);
		drawGrid(g, base2, 2, 1, 0, scale, timercount%2, 1);
		drawGrid(g, bullet, 2, 2, 0, scale, timercount%2, 1);
		drawGrid(g, tank1A, tank1Ax, 0, 1, scale, timercount%2, 2);

		//        switch (opIndex) {
		//        case 0 : /* copy */
		//            g.drawImage(bi, 0, 0, null);
		//            break;
		//
		//        case 1 : /* scale up using coordinates */
		//            g.drawImage(bi,
		//                        0, 0, w, h,     /* dst rectangle */
		//                        0, 0, w/2, h/2, /* src area of image */
		//                        null);
		//            break;
		//
		//        case 2 : /* scale down using transform */
		//            g2.drawImage(bi, AffineTransform.getScaleInstance(0.7, 0.7), null);
		//            break;
		//
		//        case 3: /* scale up using transform Op and BICUBIC interpolation */
		//            AffineTransform at = AffineTransform.getScaleInstance(1.5, 1.5);
		//            AffineTransformOp aop =
		//                new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		//            g2.drawImage(bi, aop, 0, 0);
		//            break;
		//
		//        case 4:  /* low pass filter */
		//        case 5:  /* sharpen */
		//            float[] data = (opIndex == 4) ? BLUR3x3 : SHARPEN3x3;
		//            ConvolveOp cop = new ConvolveOp(new Kernel(3, 3, data),
		//                                            ConvolveOp.EDGE_NO_OP,
		//                                            null);
		//            g2.drawImage(bi, cop, 0, 0);
		//            break;
		//
		//        case 6 : /* rescale */
		//            RescaleOp rop = new RescaleOp(1.1f, 20.0f, null);
		//            g2.drawImage(bi, rop, 0, 0);
		//            break;
		//
		//        case 7 : /* lookup */
		//            byte lut[] = new byte[256];
		//            for (int j=0; j<256; j++) {
		//                lut[j] = (byte)(256-j); 
		//            }
		//            ByteLookupTable blut = new ByteLookupTable(0, lut); 
		//            LookupOp lop = new LookupOp(blut, null);
		//            g2.drawImage(bi, lop, 0, 0);
		//            break;
		//
		//        default :
		//}
	}
}

public class ImageDrawingApplet extends JApplet {
	static String imageFileName = "examples/bld.jpg";
	private URL imageSrc;
	JButton b1;

	public ImageDrawingApplet () {
	}

	public ImageDrawingApplet (URL imageSrc) {
		this.imageSrc = imageSrc;
	}

	public void init() {
		try {
			imageSrc = new URL(getCodeBase(), imageFileName);
		} catch (MalformedURLException e) {
		}
		buildUI();
	}

	public void buildUI() {
		final ImageDrawingComponent id = new ImageDrawingComponent();
		add("Center", id);
		//        JComboBox choices = new JComboBox(id.getDescriptions());
		//        choices.addActionListener(new ActionListener() {
		//                public void actionPerformed(ActionEvent e) {
		//                    JComboBox cb = (JComboBox)e.getSource();
		//                    id.setOpIndex(cb.getSelectedIndex());
		//                    id.repaint();
		//                };
		//            });
		//3        add("South", choices);

		b1 = new JButton("Disable middle button");
		b1.setVerticalTextPosition(AbstractButton.CENTER);
		b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
		//b1.setMnemonic(KeyEvent.VK_D);
		b1.setActionCommand("doTask");
		add("South", b1);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				int frames = 10;
//				System.out.println("id.getTank1Ax(): "+id.getTank1Ax());
//				id.setTank1Ax(id.getTank1Ax() + 1/(double)frames);
//				id.repaint();
				
				
				
//				int fps = 10;
//				for (int i = 0; i < frames; i++) {
//					id.setTank1Ax(id.getTank1Ax() + 1/(double)frames);
//					id.repaint();
//					try {
//						Thread.sleep(1000/fps);
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					}
//				}
				
				final int frames = 16;
				int fps = 16;
		        Timer timer = new Timer(1000/fps,new ActionListener(){
		            @Override
		            public void actionPerformed(ActionEvent e){
		            	id.timercount++;
			            id.setTank1Ax(id.getTank1Ax() + 1/(double)frames);
			            id.repaint();
			            if (id.timercount == frames) {
			            	((Timer)e.getSource()).stop();
			            	id.timercount = 0;
			            }
		            }
		        });
	        	timer.start();
				
				
				System.out.println("doTask");            
			};
		});

	}

	public static void main(String s[]) {
		JFrame f = new JFrame("ImageDrawing");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		ImageDrawingApplet id = new ImageDrawingApplet();
		id.buildUI();
		f.add("Center", id);
		f.pack();
		f.setVisible(true);
	}
}