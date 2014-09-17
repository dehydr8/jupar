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
package jupar.parsers;

import java.util.ArrayList;

import jupar.objects.FileDescription;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Periklis Ntanasis
 */
public class DownloaderXMLParserHandler extends DefaultHandler {

    private String currentelement = "";
    private ArrayList<FileDescription> files = new ArrayList<FileDescription>();
    private FileDescription description = new FileDescription();
    private StringBuilder builder = new StringBuilder();
    
    public DownloaderXMLParserHandler() {
        super();
    }

    @Override
    public void startElement(String uri, String name, String qName, Attributes atts) {
        currentelement = qName;
        builder.setLength(0);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String value = builder.toString().trim();
        
        if (currentelement.equals("destination")) {
        	description.setDestination(value);
        } else if (currentelement.equals("filename")) {
        	description.setFilename(value);
        } else if (currentelement.equals("hash")) {
        	description.setHash(value);
        }
        
        if (qName.equals("file")) {
        	files.add(description);
        	description = new FileDescription();
        }
        
        currentelement = "";
    }

    @Override
    public void characters(char ch[], int start, int length) {
    	builder.append(String.copyValueOf(ch, start, length));
    }

    public ArrayList<FileDescription> getFiles() {
        return files;
    }
}
