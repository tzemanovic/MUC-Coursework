package com.tzemanovic.muccoursework.rss;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Tomas Zemanovic on 27/11/2014.
 */
public class RSSReader {

    public static List<RSSItem> read(URL url) throws IOException, ParserConfigurationException, SAXException {
        // request url
        InputStream inputStream = url.openStream();
        InputSource inputSource = new InputSource(inputStream);
        // parse the response
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        RSSHandler rssHandler = new RSSHandler();
        xmlReader.setContentHandler(rssHandler);
        xmlReader.parse(inputSource);
        return rssHandler.getFeed();
    }

    private static class RSSHandler extends DefaultHandler {

        private List<RSSItem> feed;
        private StringBuilder currentStringBuilder;
        private RSSItem currentRSSItem;

        public List<RSSItem> getFeed() {
            return feed;
        }

        @Override
        public  void startDocument() {
            // RSS feed typically contains last 10 posts, so reserve the list size to 10
            feed = new ArrayList<RSSItem>(10);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            currentStringBuilder = new StringBuilder();
            if(qName.equals("item")) {
                currentRSSItem = new RSSItem();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            currentStringBuilder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            // parse RSS feed
            if(qName.equals("item")) {
                feed.add(currentRSSItem);
            } else if(currentRSSItem != null) {
                if(qName.equals("title")) {
                    currentRSSItem.setTitle(currentStringBuilder.toString());
                } else if(qName.equals("description")) {
                    currentRSSItem.setDescription(currentStringBuilder.toString());
                } else if(qName.equals("pubDate")) {
                    currentRSSItem.setPubDate(currentStringBuilder.toString());
                } else if(qName.equals("link")) {
                    currentRSSItem.setLink(currentStringBuilder.toString());
                }
            }
        }
    }
}
