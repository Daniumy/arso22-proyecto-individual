package utils;

import org.xml.sax.helpers.DefaultHandler;
import java.util.LinkedList;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Validador extends DefaultHandler {
	private LinkedList<SAXParseException> errores = new LinkedList<>();

	public LinkedList<SAXParseException> getErrores() {
		return errores;
	}

	@Override
	public void error(SAXParseException e) throws SAXException {

		this.errores.add(e);
	}
}