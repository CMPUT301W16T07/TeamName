package com.teamname.tutortrader;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by abrosda on 3/22/16.
 * From lonely twitter elastic search lab
 */
public class ElasticSessionController {


    private static JestDroidClient client;
    private static Gson gson;


    public static class GetSessionsTask extends AsyncTask<String, Void, ArrayList<Session>> {

        @Override
        protected ArrayList<Session> doInBackground(String... params) {
            verifyClient();

            // Base arraylist to hold sessions
            ArrayList<Session> sessions = new ArrayList<Session>();

            /**
             * Adapted from lab 11 lonelytwitter code to fit our needs
             */
            // The following gets the top "10000" sessions
            String search_string;
            if (params[0] == "") {
                search_string = "{\"from\":0,\"size\":10000, \"sort\": {\"date\": {\"order\": \"desc\"}}}";
            } else {
                // The following gets the top 10000 sessions matching the string passed in
                search_string = "{\"from\":0,\"size\":10000,\"query\":{\"match\":{\"description\":\"" + params[0] + "\"}}, \"sort\": {\"date\": {\"order\": \"desc\"}}}";
            }
            Search search = new Search.Builder("")
                    .addIndex("cmput301w16t07")
                    .addType("session")
                    .build();
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<Session> newsessions = execute.getSourceAsObjectList(Session.class);
                    sessions.addAll(newsessions);
                    Log.e("TEST", "Searching");
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
            return sessions;
        }
    }

    public static class AddSessionTask extends AsyncTask<Session, Void, Void> {


        @Override
        protected Void doInBackground(Session... params) {
            verifyClient();

            for (Session session : params) {
                Index index = new Index.Builder(session).index("cmput301w16t07").type("session").build();

                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()) {

                    } else {
                        // TODO: Something more useful
                        Log.e("TODO", "Our insert of session failed, oh no!");
                    }
                    return null;
                } catch (IOException e) {
                    // TODO: Something more useful
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

        public static void verifyClient() {
            if (client == null) {
                // TODO: Consider moving this URL in to some config class
                DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080/");
                //builder.discoveryEnabled(true);
                DroidClientConfig config = builder.build();

                JestClientFactory factory = new JestClientFactory();
                factory.setDroidClientConfig(config);
                client = (JestDroidClient) factory.getObject();

                gson = new Gson();
                client.setGson(gson);
            }
        }

    }

