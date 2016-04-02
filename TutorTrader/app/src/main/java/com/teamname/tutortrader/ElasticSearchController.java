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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Result;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by abrosda on 3/22/16.
 * From lonely twitter elastic search lab
 */
public class ElasticSearchController {


    private static JestDroidClient client;
    private static Gson gson;


    public static class GetSessionsTask extends AsyncTask<String, Void, ArrayList<Session>> {

        @Override
        protected ArrayList<Session> doInBackground(String... params) {
            verifyClient();

            // Base arraylist to hold sessions
            ArrayList<Session> newSessions = new ArrayList<Session>();

            /**
             * Adapted from lab 11 lonelytwitter code to fit our needs
             */
            // The following gets the top "10000" sessions
            String search_string;
            if (params[0] == "") {
                //search_string = "{\"from\":0,\"size\":10000,\"query\":{\"match\":{\"status\":\"available\" }}}";
                search_string = "{\"from\" : 0, \"size\" : 10000}";//, \"query\":{\"match\":{\"status\":\"available\"}}}";
            } else {
                // The following gets the top 10000 sessions matching the string passed in
                search_string = "{\"from\":0,\"size\":10000,\"query\":{\"match\":{\"" + params[0] + "\":\"" + params[1] + "\"}}}";
            }
            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301w16t07")
                    .addType("session")
                    .build();
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<Session> searchedSessions = execute.getSourceAsObjectList(Session.class);
                    newSessions.addAll(searchedSessions);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
            return newSessions;
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

    /**
     * must pass in a session's UUID
     * from:https://github.com/CMPUT301W16T06/TechNoLogic/blob/master/src/app/src/main/java/ca/ualberta/cs/technologic/ElasticSearchComputer.java
     */
    public static class RemoveSessionTask extends AsyncTask<UUID, Void, Void> {

        @Override
        protected Void doInBackground(UUID... params) {
            verifyClient();
            ArrayList<Session> sessionToDelete = new ArrayList<>();
            List<SearchResult.Hit<Map,Void>> hits = null;
            String elasticSearchID;
            String query = "{\"query\":{ \"match\" :{\"sessionID\":\"" + params[0] + "\"}}}";
            Search search = new Search.Builder(query)
                    .addIndex("cmput301w16t07")
                    .addType("session")
                    .build();

            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<Session> searchedSessions = execute.getSourceAsObjectList(Session.class);
                    hits = execute.getHits(Map.class);
                    sessionToDelete.addAll(searchedSessions);
                    Log.e("TEST", "Searching for session to delete");
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }


            SearchResult.Hit hit = hits.get(0);
            Map source = (Map)hit.source;
            elasticSearchID = (String) source.get(JestResult.ES_METADATA_ID);
            Delete delete = new Delete.Builder(elasticSearchID).index("cmput301w16t07").type("session").build();

            try {
                DocumentResult execute = client.execute(delete);
                if (execute.isSucceeded()) {
                } else {
                    // TODO: Something more useful
                    Log.e("TODO", "Our delete of session failed, oh no!");
                }
                return null;
            } catch (IOException e) {
                // TODO: Something more useful
                e.printStackTrace();
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
