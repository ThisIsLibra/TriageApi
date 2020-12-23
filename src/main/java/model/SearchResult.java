/*
 * Copyright (C) 2020 Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package model;

import java.util.List;

/**
 * This class contains the search results for a given query, as well as the
 * offset to obtain more results for the same query.
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class SearchResult {

    /**
     * The list of search results for the given query
     */
    private List<SearchResultEntry> searchResults;

    /**
     * The offset to get new results for the same query
     */
    private String nextOffset;

    /**
     * This boolean defines if this object is empty
     */
    private boolean isEmpty;

    /**
     * This object is used to store the result of a search query.
     *
     * @param searchResults a list of search results based on a given query
     * @param offset the offset to obtain new results with the same query
     */
    public SearchResult(List<SearchResultEntry> searchResults, String offset) {
        this.searchResults = searchResults;
        this.nextOffset = offset;
        isEmpty = false;
    }

    /**
     * Gets the list of search results
     *
     * @return the list of search results
     */
    public List<SearchResultEntry> getSearchResults() {
        return searchResults;
    }

    /**
     * Gets the offset for new results
     *
     * @return the next offset
     */
    public String getNextOffset() {
        return nextOffset;
    }

    /**
     * If the object is empty, this value is true. If not, it is false.
     *
     * @return true if the object is empty, false if not
     */
    public boolean isEmpty() {
        return isEmpty;
    }

}
