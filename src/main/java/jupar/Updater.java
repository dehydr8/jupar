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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import jupar.objects.Instruction;
import jupar.objects.Modes;
import jupar.parsers.UpdateXMLParser;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author Periklis Ntanasis
 */
public class Updater {

    @SuppressWarnings("rawtypes")
	public void update(String instructionsxml, String tmp, Modes mode) throws SAXException,
            FileNotFoundException, IOException, InterruptedException {

        UpdateXMLParser parser = new UpdateXMLParser();
        Iterator iterator = parser.parse(tmp + File.separator + instructionsxml, mode).iterator();
        Instruction instruction;

        while (iterator.hasNext()) {
            instruction = (Instruction) iterator.next();
            switch (instruction.getAction()) {
                case MOVE:
                    copy(tmp + File.separator + instruction.getFilename(), instruction.getDestination());
                    break;
                case DELETE:
                    delete(instruction.getDestination());
                    break;
                case EXECUTE:
                	execute(instruction, tmp);
                    break;
                case CLEAR:
                	FileUtils.cleanDirectory(new File(instruction.getDestination()));
                    break;
            }
        }
    }
    
    private void execute(Instruction instruction, String tmp) throws IOException {
    	String extension = "";
    	String prefix = "java -jar " + tmp + File.separator;
    	boolean wait = true;
    	
    	int i = instruction.getFilename().lastIndexOf('.');
    	if (i > 0) {
    	    extension = instruction.getFilename().substring(i+1).toLowerCase();
    	}
    	
    	if (extension.equals("bat")) {
    		prefix = "cmd /c ";
    		wait = false;
    	} else if (extension.equals("sh")) {
    		prefix = "sh ";
    		wait = false;
    	}
    	
    	String command = prefix + instruction.getFilename();
    	System.out.println("[JUPAR] exec(" + command + ")");
		Process p = Runtime.getRuntime().exec(command);
		
		if (wait)
			new ProcessOutputRedirector(p, command).redirect();
    }
    
    @SuppressWarnings("rawtypes")
	public void update(String instructionsxml, String tmp, String dstdir, Modes mode) throws SAXException,
            FileNotFoundException, IOException, InterruptedException {

        UpdateXMLParser parser = new UpdateXMLParser();
        Iterator iterator = parser.parse(tmp + File.separator + instructionsxml, mode).iterator();
        Instruction instruction;

        while (iterator.hasNext()) {
            instruction = (Instruction) iterator.next();
            switch (instruction.getAction()) {
                case MOVE:
                    copy(tmp + File.separator + instruction.getFilename(), dstdir+File.separator+instruction.getDestination());
                    break;
                case DELETE:
                    delete(dstdir+File.separator+instruction.getDestination());
                    break;
                case EXECUTE:
                    Runtime.getRuntime().exec("java -jar " + tmp + File.separator + instruction.getFilename());
                    break;
                case CLEAR:
                	FileUtils.cleanDirectory(new File(instruction.getDestination()));
                    break;
            }
        }

    }

    private void copy(String source, String destination) throws FileNotFoundException, IOException {
        File srcfile = new File(source);
        File dstfile = new File(destination);
        File dst = dstfile.getParentFile();
        
        if (!dst.exists()) {
        	dst.mkdirs();
        }

        InputStream in = new FileInputStream(srcfile);
        OutputStream out = new FileOutputStream(dstfile);

        byte[] buffer = new byte[512];
        int length;

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.close();
    }

    private void delete(String filename) {
        File file = new File(filename);
        file.delete();
    }
}
