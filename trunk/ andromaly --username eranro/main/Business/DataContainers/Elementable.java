package andromaly.main.Business.DataContainers;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Elementable {
	public Element asElement(Document dom) throws ParserConfigurationException;
}
