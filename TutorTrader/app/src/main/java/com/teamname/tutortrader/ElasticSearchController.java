package com.teamname.tutortrader;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
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
                Index index = new Index.Builder(session).index("cmput301w16t07").type("session").id(params[0].getSessionID().toString()).build();

                try {
                    DocumentResult execute = client.execute(index);
                    return null;
                } catch (IOException e) {
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
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class UpdateSessionTask extends AsyncTask<Session, Void, Void> {

        @Override
        protected Void doInBackground(Session... params) {
            verifyClient();
            try {
                //String updater = "{\"from\":0,\"size\":10000,\"query\":{\"match\":{\"" + params[0] + "\":\"" + params[1] + "\"}}}";
                String newId = params[0].getSessionID().toString();
                JestResult result = client.execute(new Index.Builder(params[0])
                                .index("cmput301w16t07")
                                .type("session")
                                .id(newId)
                                .build()
                );

            } catch (IOException e) {
                throw new RuntimeException();
            }
            return null;
        }

    }

    public static class UpdateProfileTask extends AsyncTask<Profile, Void, Void> {

        @Override
        protected Void doInBackground(Profile... profiles) {
            verifyClient();
            try {
                String newId = profiles[0].getProfileID().toString();
                JestResult result = client.execute(new Index.Builder(profiles[0])
                                .index("cmput301w16t07")
                                .type("profile")
                                .id(newId)
                                .build()
                );

            } catch (IOException e) {
                throw new RuntimeException();
            }
            return null;
        }
    }
        public static void verifyClient() {
            if (client == null) {
                DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080/");
                DroidClientConfig config = builder.build();

                JestClientFactory factory = new JestClientFactory();
                factory.setDroidClientConfig(config);
                client = (JestDroidClient) factory.getObject();

                gson = new Gson();
                client.setGson(gson);
            }
        }

    public static class GetProfileTask extends AsyncTask<String, Void, ArrayList<Profile>> {

        @Override
        protected ArrayList<Profile> doInBackground(String... params) {
            verifyClient();

            // Base arraylist to hold sessions
            ArrayList<Profile> newProfiles = new ArrayList<Profile>();

            // The following gets the top "10000" profiles
            String search_string;
            if (params[0] == "") {
                search_string = "{\"from\" : 0, \"size\" : 10000}";//, \"query\":{\"match\":{\"status\":\"available\"}}}";
            } else {
                // The following gets the top 10000 sessions matching the string passed in
                search_string = "{\"from\":0,\"size\":10000,\"query\":{\"match\":{\"" + params[0] + "\":\"" + params[1] + "\"}}}";
            }
            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301w16t07")
                    .addType("profile")
                    .build();
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<Profile> searchedProfile = execute.getSourceAsObjectList(Profile.class);
                    newProfiles.addAll(searchedProfile);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
            return newProfiles;
        }
    }

    public static class AddProfileTask extends AsyncTask<Profile, Void, Void> {


        @Override
        protected Void doInBackground(Profile... params) {
            verifyClient();

            for (Profile profile : params) {
                Index index = new Index.Builder(profile).index("cmput301w16t07").type("profile").id(profile.getProfileID().toString()).build();

                try {
                    DocumentResult execute = client.execute(index);
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
     * must pass in a profile's UUID
     * from:https://github.com/CMPUT301W16T06/TechNoLogic/blob/master/src/app/src/main/java/ca/ualberta/cs/technologic/ElasticSearchComputer.java
     */
    public static class RemoveProfileTask extends AsyncTask<UUID, Void, Void> {

        @Override
        protected Void doInBackground(UUID... params) {
            verifyClient();
            ArrayList<Profile> profileToDelete = new ArrayList<>();
            List<SearchResult.Hit<Map,Void>> hits = null;
            String elasticSearchID;
            String query = "{\"query\":{ \"match\" :{\"ProfileID\":\"" + params[0] + "\"}}}";
            Search search = new Search.Builder(query)
                    .addIndex("cmput301w16t07")
                    .addType("profile")
                    .build();

            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<Profile> searchedProfiles = execute.getSourceAsObjectList(Profile.class);
                    hits = execute.getHits(Map.class);
                    profileToDelete.addAll(searchedProfiles);
                    Log.e("TEST", "Searching for profile to delete");
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }


            SearchResult.Hit hit = hits.get(0);
            Map source = (Map)hit.source;
            elasticSearchID = (String) source.get(JestResult.ES_METADATA_ID);
            Delete delete = new Delete.Builder(elasticSearchID).index("cmput301w16t07").type("profile").build();

            try {
                DocumentResult execute = client.execute(delete);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

