package com.neoware.europlanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Assert;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.ContextWrapper;

public class SquadDefinition {

    private static final String mDefinitionFileName = "squaddefn.xml";

    private File mDefnFile;

    private final Context mContext;
    private Squad mSquad;
    private static int mTargetSquadId;
    private static int mCurrentSquadId;

    public SquadDefinition(Context context, int teamIndx) {
        mTargetSquadId = teamIndx;
        mContext = context;
        initialise();
        refreshDataStructre();
    }

    //Checks if the packaged defn file needs to be copied to user data dir
    private void initialise() {

        ContextWrapper wrapper = new ContextWrapper(mContext);
        File homeDir = wrapper.getFilesDir();
        mDefnFile = new File(homeDir, mDefinitionFileName);

        //For dev always copy - DONT SHIP WITH THIS!!!!!
        if(true /*!userDefnFile.exists()*/) {

            InputStream defnInputStream = mContext.getResources().openRawResource(
                    R.raw.squaddefn);

            try {
                OutputStream fos = new FileOutputStream(mDefnFile);
                byte copyBuffer[] = new byte[1024];
                int lengthCopied = 0;
                while((lengthCopied=defnInputStream.read(copyBuffer))>0) {
                    fos.write(copyBuffer, 0, lengthCopied);
                }
            }
            catch(IOException ex) {
                Assert.fail("No squad definition - we are fucked" + ex.getMessage());
            }
        }
    }

    public void refreshDataStructre() {

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(new ContentHandler() {

                private static final String SQUAD = "squad";
                private static final String SQUAD_ID = "id";

                private static final String POSITION = "position";
                private static final String PLAYER = "player";
                private static final String NUMBER = "number";
                private static final String NAME = "name";
                private static final String DOB = "dob";

                @Override
                public void startElement(String uri, String localName,
                        String qName, Attributes atts) throws SAXException {

                    if(localName.equals(SQUAD)) {

                        mCurrentSquadId = Integer.parseInt(atts.getValue(SQUAD_ID));
                        // Only parse the squad we want, skip otherwise
                        if(mCurrentSquadId != mTargetSquadId) {
                            return;
                        }
                        else {
                            mSquad = new Squad(
                                    Integer.parseInt(atts.getValue(SQUAD_ID)));
                        }
                    }

                    else if(localName.equals(PLAYER)) {
                        // Only parse the squad we want, skip otherwise
                        if(mCurrentSquadId != mTargetSquadId) {
                            return;
                        }

                        mSquad.addPlayer(new Player(
                                atts.getValue(POSITION),
                                atts.getValue(NUMBER),
                                atts.getValue(NAME),
                                atts.getValue(DOB)
                                ));
                    }
                }

                @Override
                public void endDocument() throws SAXException { // TODO: Remove
                }

                @Override public void startPrefixMapping(String prefix, String uri) throws SAXException {}
                @Override public void startDocument() throws SAXException {}
                @Override public void skippedEntity(String name) throws SAXException {}
                @Override public void setDocumentLocator(Locator locator) {}
                @Override public void processingInstruction(String target, String data) throws SAXException {}
                @Override public void ignorableWhitespace(char[] ch, int start, int length)throws SAXException {}
                @Override public void endPrefixMapping(String prefix) throws SAXException {}
                @Override public void endElement(String uri, String localName, String qName) throws SAXException {}
                @Override public void characters(char[] ch, int start, int length) throws SAXException {}
            });

            reader.parse(new InputSource(new FileInputStream(mDefnFile)));

        } catch (ParserConfigurationException ex) {
            Assert.fail("SAX Failure: " + ex.getMessage());
        } catch (SAXException ex) {
            Assert.fail("SAX Failure: " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            Assert.fail("Parser Failure: " + ex.getMessage());
        } catch (IOException ex) {
            Assert.fail("Parser Failure: " + ex.getMessage());
        }
    }

    public Squad getSquad() {
        return mSquad;
    }
}
