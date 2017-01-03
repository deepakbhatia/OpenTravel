package com.projects050414.myapplication3.app;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by obelix on 03/05/2014.
 */
public class DestinationSearchProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.projects050414.myapplication3.app.DestinationSearchProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public DestinationSearchProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
