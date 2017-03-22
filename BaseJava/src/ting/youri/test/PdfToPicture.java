package ting.youri.test;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PdfToPicture {
	public static void main(String[] args) throws IOException {
   	 // load a pdf from a byte buffer  
       File file = new File("D:\\spring-framework-reference.pdf");  
       RandomAccessFile raf = new RandomAccessFile(file, "r");  
       FileChannel channel = raf.getChannel();  
       ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel  
               .size());  
       PDFFile pdffile = new PDFFile(buf);  
       System.out.println("页数： " + pdffile.getNumPages());  
       String getPdfFilePath = "D:\\pdfPicFile";
       System.out.println("getPdfFilePath is  :"+getPdfFilePath);
       for (int i = 1; i <= pdffile.getNumPages(); i++) {  
           // draw the first page to an image  
           PDFPage page = pdffile.getPage(i);  

           // get the width and height for the doc at the default zoom  
           Rectangle rect = new Rectangle(0, 0, (int) page.getBBox()  
                   .getWidth(), (int) page.getBBox().getHeight());  

           int width = rect.width * 6;
           int height = rect.height * 6;
           // generate the image  
           Image img = page.getImage(width, height, // width &  
                                                               // height  
                   rect, // clip rect  
                   null, // null for the ImageObserver  
                   true, // fill background with white  
                   true // block until drawing is done  
                   );  

           BufferedImage tag = new BufferedImage(width, height,  
                   BufferedImage.TYPE_INT_RGB);  
           tag.getGraphics().drawImage(img, 0, 0, width, height,  
                   null); 
          
         
            //jdk 1.7版本被移除 JPEGImageEncoder
           /*FileOutputStream out = new FileOutputStream( getPdfFilePath+"\\" + i + ".jpg"); // 输出到文件流
           System.out.println("成功保存图片到 ：  " +getPdfFilePath+"\\" + i + ".jpg");
          
           JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
           encoder.encode(tag); // JPEG编码
           out.close();*/           
           ImageIO.write(tag,  "png" , new File( getPdfFilePath+"\\" + i + ".png")); tag.flush();
       }  
	}
}
