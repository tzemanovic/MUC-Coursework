package com.tzemanovic.muccoursework;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tzemanovic.muccoursework.helper.FontLoader;
import com.tzemanovic.muccoursework.rss.RSSItem;
import com.tzemanovic.muccoursework.rss.RSSReader;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends ActionBarActivity {

    private LinearLayout newsFeed;

    private final static String NEWS_FEED_URL = "http://www.gosugamers.net/dota2/news/rss";
    private final static DateFormat RSS_DATE_FORMATTER_FROM_STRING = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    private final static DateFormat RSS_DATE_FORMATTER_TO_STRING = new SimpleDateFormat("EEEE d MMMM yyyy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsFeed = (LinearLayout) findViewById(R.id.newsFeed);

        ((TextView) findViewById(R.id.newsHeading)).setTypeface(FontLoader.constantia(this));

        AsyncRSSReader asyncRSSReader = new AsyncRSSReader();
        asyncRSSReader.execute(NEWS_FEED_URL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRssFeed(List<RSSItem> result) {
        for (final RSSItem item : result) {
            View rssItem = View.inflate(getApplicationContext(), R.layout.rss_item, null);

            TextView title = (TextView) rssItem.findViewById(R.id.rssItemTitle);
            title.setText(item.getTitle());
            title.setTypeface(FontLoader.constantia(MainActivity.this));

            TextView pubDate = (TextView) rssItem.findViewById(R.id.rssItemPubDate);
            pubDate.setText(formatPubDate(item.getPubDate()));
            pubDate.setTypeface(FontLoader.constantia(MainActivity.this));

            String descriptionText = android.text.Html.fromHtml(item.getDescription()).toString()
                    .replace("Click here to read the full article.", "").replace("\n", "");
            TextView description = (TextView) rssItem.findViewById(R.id.rssItemDescription);
            description.setText(descriptionText);
            //description.setTypeface(FontLoader.constantia(MainActivity.this));

            LinearLayout readMore = (LinearLayout) rssItem.findViewById(R.id.rssItemReadMore);
            readMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                    startActivity(browserIntent);
                }
            });

            newsFeed.addView(rssItem);
        }
        findViewById(R.id.newsLoading).setVisibility(View.GONE);
    }

    private static String formatPubDate(String pubDate) {
        Date date = null;
        try {
            date = RSS_DATE_FORMATTER_FROM_STRING.parse(pubDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return pubDate;
        } else {
            return RSS_DATE_FORMATTER_TO_STRING.format(date);
        }
    }

    private class AsyncRSSReader extends AsyncTask<String, Void, List<RSSItem>> {

        @Override
        protected List<RSSItem> doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Unable to obtain news data, malformed URL.", Toast.LENGTH_LONG);
            }

            List<RSSItem> feed = null;
            if (url != null) {
                try {
                    feed = RSSReader.read(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to obtain news data.", Toast.LENGTH_LONG);
                }
            }

            return feed;
        }

        @Override
        protected void onPostExecute(List<RSSItem> result) {
            showRssFeed(result);
        }
    }

}
