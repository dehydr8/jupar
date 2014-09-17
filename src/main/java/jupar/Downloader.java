/**
 * Written by Periklis Master_ex Ntanasis <pntanasis@gmail.com>
 * http://masterex.github.com/
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package jupar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jupar.objects.FileDescription;
import jupar.objects.Modes;

import org.xml.sax.SAXException;

import jupar.parsers.DownloaderXMLParser;
import jupar.utils.JuparUtils;

/**
 *
 * @author Periklis Ntanasis
 */
public class Downloader {

	public void download(String filesxml, String destinationdir, Modes mode) throws SAXException,
            FileNotFoundException, IOException, InterruptedException {

        DownloaderXMLParser parser = new DownloaderXMLParser();
        Iterator<FileDescription> iterator = parser.parse(filesxml, mode).iterator();
        java.net.URL url;

        List<String> urls = new ArrayList<String>();
        while (iterator.hasNext()) {
        	
        	FileDescription element = iterator.next();
        	
        	String fileHash = JuparUtils.getFileHash(element.getDestination());
        	
        	boolean matches = false;
        	
        	if (fileHash != null && element.getHash() != null)
        		if (fileHash.equalsIgnoreCase(element.getHash()))
        			matches = true;
        	
        	if (!matches)
        		urls.add(element.getFilename());
        }
        
        File dir = new File(destinationdir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (String u : urls) {
            url = new java.net.URL(u);
            wget(url, destinationdir + File.separator + new File(url.getFile()).getName());
        }
    }

    private void wget(java.net.URL url, String destination) throws MalformedURLException, IOException {
        java.net.URLConnection conn = url.openConnection();
        java.io.InputStream in = conn.getInputStream();

        File dstfile = new File(destination);
        OutputStream out = new FileOutputStream(dstfile);

        byte[] buffer = new byte[512];
        int length;

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.close();
    }
}
