package wvustat.contentviewer;

import java.lang.reflect.Method;
import javax.swing.JOptionPane;
import java.io.*;

public class BrowserLaunch {

   private static final String errMsg = "Error attempting to launch pdf viewer";

   public void openResource(String content, boolean crypt) {	   
	   try {		   
		   File tmpfile = copyResourceToTmpFile(content, crypt);
		   openURL(tmpfile.toURL().toString());
	   
	   } catch (Exception ex) {
		   JOptionPane.showMessageDialog(null, errMsg + ":\n" + ex.getLocalizedMessage());
	   }
   }
   
   public File copyResourceToTmpFile(String content, boolean crypt) throws Exception {
	   
	   String filename = content.substring(content.lastIndexOf("/") + 1, content.length());
	   String tmpdir = System.getProperty("java.io.tmpdir");
	   if (tmpdir == null)
		   throw new Exception("Cannot locate system temporary directory");
	   
	   File tmpfile = new File(tmpdir + File.separator + filename);
	   
	   if (crypt)
		   decryptResourceTo(content, tmpfile.getAbsolutePath());
	   else
		   copyResourceTo(content, tmpfile.getAbsolutePath());
	   
	   return tmpfile;
   }
   
   public void copyResourceTo(String content, String filepath){
	   BufferedInputStream in = null;
	   BufferedOutputStream out = null;
	   
	   try {
		   in = new BufferedInputStream(BrowserLaunch.class.getResourceAsStream(content));
		   
		   File tmpfile = new File(filepath);
		   //if(tmpfile.exists()) return;
		   out = new BufferedOutputStream(new FileOutputStream(tmpfile));
	   
		   int c;
		   while ((c = in.read()) != -1)
			   out.write(c);
		   out.flush();
	   
	   } catch (Exception ex) {
		   //JOptionPane.showMessageDialog(null, errMsg + ":\n" + ex.getLocalizedMessage());
		   
	   } finally {
		   try {
			   if (in != null) in.close();
			   if (out != null) out.close();
		   } catch (Exception ex) {}
	   }
   }
   
   public void decryptResourceTo(String content, String filepath){
	   BufferedInputStream in = null;
	   BufferedOutputStream out = null;
	   
	   try {
		   in = new BufferedInputStream(BrowserLaunch.class.getResourceAsStream(content));
		   
		   File tmpfile = new File(filepath);
		   //if(tmpfile.exists()) return;
		   out = new BufferedOutputStream(new FileOutputStream(tmpfile));
	   
		   int c;
		   while ((c = in.read()) != -1)
			   out.write(c ^ 0xff);
		   
		   out.flush();
	   
	   } catch (Exception ex) {
		   //JOptionPane.showMessageDialog(null, errMsg + ":\n" + ex.getLocalizedMessage());
		   
	   } finally {
		   try {
			   if (in != null) in.close();
			   if (out != null) out.close();
		   } catch (Exception ex) {}
	   }
   }
   
   public void encryptFileTo(String from, String to){
	   BufferedInputStream in = null;
	   BufferedOutputStream out = null;
	   
	   try {
		   in = new BufferedInputStream(new FileInputStream(from));
		   out = new BufferedOutputStream(new FileOutputStream(to));
	   
		   int c;
		   while ((c = in.read()) != -1)
			   out.write(c ^ 0xff);
		   
		   out.flush();
	   
	   } catch (Exception ex) {
		   JOptionPane.showMessageDialog(null, errMsg + ":\n" + ex.getLocalizedMessage());
		   
	   } finally {
		   try {
			   if (in != null) in.close();
			   if (out != null) out.close();
		   } catch (Exception ex) {}
	   }
   }
   
   
   public void openURL(String url) {
      String osName = System.getProperty("os.name");
      try {
         if (osName.startsWith("Mac OS")) {
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",
               new Class[] {String.class});
            openURL.invoke(null, new Object[] {url});
         }
         else if (osName.startsWith("Windows"))
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
         else { //assume Unix or Linux
            String[] browsers = {
               "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
               if (Runtime.getRuntime().exec(
                     new String[] {"which", browsers[count]}).waitFor() == 0)
                  browser = browsers[count];
            if (browser == null)
               throw new Exception("Could not find web browser");
            else
               Runtime.getRuntime().exec(new String[] {browser, url});
         }
      }
      catch (Exception e) {
         JOptionPane.showMessageDialog(null, errMsg + ":\n" + e.getLocalizedMessage());
      }
   }   
   
   public static void main(String[] args){
	   String from = "/Users/admin/Dajie_Folder/work/JavaStat/LifeStats/";
	   String to = "/Users/admin/Desktop/eclipse/workspace/myJavaStat/src/contents/";
	   String[] files = {"chap03.pdf", "chap04.pdf", "chap05.pdf",
			   			"chap06.pdf", "chap07.pdf", "chap08.pdf",
			   			"chap09.pdf", "chap10.pdf", "chap11.pdf",
			   			"chap12.pdf", "chap13.pdf"
	   					};
	   
	   BrowserLaunch encrypter = new BrowserLaunch();
	   for(int i = 0; i < files.length; i++){
		   encrypter.encryptFileTo(from + files[i], to + files[i]);
	   }
   }
}
