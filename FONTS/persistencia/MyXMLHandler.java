package persistencia;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Arrays;


public class MyXMLHandler extends DefaultHandler {

    private String t;
    private String a;
    private ArrayList<String> Content;

    private boolean isAutor = false;
    private boolean isTitol = false;
    private boolean isContingut = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)  {
        if (qName.equalsIgnoreCase("autor")) isAutor = true;
        else if (qName.equalsIgnoreCase("titol")) isTitol = true;
        else if (qName.equalsIgnoreCase("contingut")) isContingut = true;

    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        //if (qName.equalsIgnoreCase("document")) {}
    }

    @Override
    public void characters(char ch[], int start, int length) {
        if (isAutor) {
            a = new String(ch, start, length);
            isAutor = false;
        } else if (isTitol) {
            t = new String(ch, start, length);
            isTitol = false;
        } else if (isContingut) {
            Content = new ArrayList<>();
            String c = new String(ch, start, length);
            //c = c.replace("\n"," $$BREAK$$ ");
            Content.addAll(new ArrayList<String>(Arrays.asList(c.split(" "))));
            Content.removeAll(Arrays.asList("", null));
            isContingut = false;
        }
    }

    public String getA() {
        return a;
    }

    public ArrayList<String> getContent() {
        return Content;
    }

    public String getT() {
        return t;
    }
}
