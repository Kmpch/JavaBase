package ting.youri.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;

public class PPTToPicture {
	public static void main(String[] args) throws IOException {
		Thread t = new Thread(new Change());
		t.start();
	}
}
	
class Change implements Runnable{

	@Override
	public void run() {

		try {
			pptToPic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void pptToPic() throws Exception{
		String input_file_path ="D:\\《登高》PPT教学课件.ppt";
		String output_file_path = "D:\\ppt_to_pic\\";
		File ppt_file = new File(input_file_path);
		FileInputStream is =  null;
		 is = new FileInputStream(ppt_file);
		 HSLFSlideShow ppt_hslf = new HSLFSlideShow(is);
		 Dimension pageSize = ppt_hslf.getPageSize();
		 int index = 1;
		 int n = 6;
		 int width = pageSize.width * n;
		 int height = pageSize.height * n;
		 for(HSLFSlide slide : ppt_hslf.getSlides()) {
			 
			 BufferedImage img = new BufferedImage(pageSize.width, 
					 		pageSize.height, BufferedImage.TYPE_INT_RGB);
			 Graphics2D graphics = img.createGraphics();
			 //clear the drawing area
			 graphics.setPaint(Color.white);
			 graphics.fill(new Rectangle2D.Float(0,0,pageSize.width,pageSize.height));
			 
			 //render
			 slide.draw(graphics);
			 
			 // save the output
			 FileOutputStream out  = new FileOutputStream(output_file_path + index + ".png");
			 ImageIO.write(img, "png", out);
			 out.close();
			 index++;
		 }

	}
}

