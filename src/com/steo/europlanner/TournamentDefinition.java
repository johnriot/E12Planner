package com.steo.europlanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

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

import com.steo.europlanner.FeedsAdapter.FeedDefn;

public class TournamentDefinition {

    private static final String mDefinitionFileName = "tournamentdefn.xml";

    private File mDefnFile;

    private final Context mContext;
    private final ArrayList<Group> mGroups = new ArrayList<Group>();
    private final ArrayList<FeedDefn> mFeeds = new ArrayList<FeedDefn>();
    private final ArrayList<Venue> mVenues = new ArrayList<Venue>();    

    public TournamentDefinition(Context context) {
        mContext = context;

        initialise();
        refreshDataStructre();
        createVenues();
    }

    //Checks if the packaged defn file needs to be copied to user data dir
    private void initialise() {

        ContextWrapper wrapper = new ContextWrapper(mContext);
        File homeDir = wrapper.getFilesDir();
        mDefnFile = new File(homeDir, mDefinitionFileName);

        //For dev always copy - DONT SHIP WITH THIS!!!!!
        if(true /*!userDefnFile.exists()*/) {

            InputStream defnInputStream = mContext.getResources().openRawResource(
                    R.raw.tournamentdefn);

            try {
                OutputStream fos = new FileOutputStream(mDefnFile);
                byte copyBuffer[] = new byte[1024];
                int lengthCopied = 0;
                while((lengthCopied=defnInputStream.read(copyBuffer))>0) {
                    fos.write(copyBuffer, 0, lengthCopied);
                }
            }
            catch(IOException ex) {
                Assert.fail("No tournament definition - we are fucked" + ex.getMessage());
            }
        }
    }

    public void refreshDataStructre() {

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(new ContentHandler() {

                private Group mCurrentGroup;

                private static final String GROUP = "group";
                private static final String GROUP_ID = "id";

                private static final String TEAM = "team";
                private static final String TEAM_ID = "id";

                private static final String FIXTURE = "fixture";
                private static final String HOME_TEAM_ID = "hometeam";
                private static final String AWAY_TEAM_ID = "awayteam";
                private static final String VENUE_ID = "venue";
                private static final String DATE_ID = "date";
                private static final String SCORE_ID = "score";

                private static final String FEED = "feed";
                private static final String FEED_DESC = "description";
                private static final String FEED_URL = "url";
                private static final String FEED_ICON = "iconid";

                @Override public void startElement(String uri, String localName,
                        String qName, Attributes atts) throws SAXException {

                    if(localName.equals(FEED)) {
                        String url = atts.getValue(FEED_URL);
                        String desc = atts.getValue(FEED_DESC);
                        int icon = Integer.parseInt(atts.getValue(FEED_ICON));

                        int iconid = R.drawable.rss_icon;
                        switch(icon) {
                        case 0:
                            iconid = R.drawable.uefa;
                            break;
                        case 1:
                            iconid = R.drawable.bbcicon;
                        }

                        mFeeds.add(new FeedDefn(url, desc, iconid));
                    }

                    if(localName.equals(GROUP)) {
                        if(mCurrentGroup != null) {
                            mGroups.add(mCurrentGroup);
                        }

                        mCurrentGroup = new Group(Integer.parseInt(
                                atts.getValue(GROUP_ID)));
                    }
                    else if(localName.equals(TEAM)) {
                        mCurrentGroup.addTeam(new Team(Integer.parseInt(
                                atts.getValue(TEAM_ID))));
                    }
                    else if(localName.equals(FIXTURE)) {

                        int homeTeamId = Integer.parseInt(atts.getValue(HOME_TEAM_ID));
                        int awayTeamId = Integer.parseInt(atts.getValue(AWAY_TEAM_ID));
                        int venueId = Integer.parseInt(atts.getValue(VENUE_ID));
                        @SuppressWarnings("unused")
                        String date = atts.getValue(DATE_ID);
                        String score = atts.getValue(SCORE_ID);

                        
                        Team homeTeam = mCurrentGroup.getTeamById(homeTeamId);
                        Team awayTeam = mCurrentGroup.getTeamById(awayTeamId);
                        Assert.assertNotNull(homeTeam);
                        Assert.assertNotNull(awayTeam);
                        
                        Fixture fixture = new Fixture(homeTeam, awayTeam,
                        								venueId, new Date(), score);
                        
                        mCurrentGroup.addFixture(fixture);
                    }
                }

                @Override
                public void endDocument() throws SAXException {
                    if(mCurrentGroup != null) {
                        mGroups.add(mCurrentGroup);
                    }
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
            for(Group group : mGroups)
            	group.orderTeams();

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
    
    private void createVenues() {
    	// Hardcoded 8 venues
    	for(int ii = 0; ii < 8; ++ii) {
    		mVenues.add(new Venue(ii));
    	}
    		
    }

    public ArrayList<Group> getGroups() {
        return mGroups;
    }
    
    public ArrayList<Venue> getVenues() {
        return mVenues;
    }

    public ArrayList<FeedDefn> getFeeds() {
        return mFeeds;
    }
}
