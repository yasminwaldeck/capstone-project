import { useContext, useState } from "react";
import MovieAndSeriesCard from "../components/MovieAndSeriesCard";
import TypeAndAuthContext from "../context/TypeAndAuthContext";
import useWatchlist from "../hooks/useWatchlist";
import useSearch from "../hooks/useSearch";
import styled from "styled-components/macro";
import useWatchHistory from "../hooks/useWatchHistory";

export default function SearchPage() {
  const { MOVIE, SERIES } = useContext(TypeAndAuthContext);
  const [searchString, setSearchString] = useState("");
  const [searchType, setSearchType] = useState(MOVIE);
  const { watchlist } = useWatchlist();
  const { searchResults } = useSearch(searchString, searchType);
  const { watchHistory } = useWatchHistory();

  return (
    <Search>
      <input
        type="text"
        value={searchString}
        onChange={(event) => setSearchString(event.target.value)}
      />
      <div>
        <input
          type="radio"
          name="search_type"
          onChange={() => setSearchType(MOVIE)}
          defaultChecked
        />{" "}
        Movie
        <input
          type="radio"
          name="search_type"
          onChange={() => setSearchType(SERIES)}
        />{" "}
        Series
      </div>
      {searchResults &&
        searchResults.map((item) => (
          <MovieAndSeriesCard
            key={item.imdbID}
            item={item}
            onWatchlist={watchlist.find(
              (watchedItem) => watchedItem.imdbID === item.imdbID
            )}
            onWatchHistory={watchHistory.find(
              (watchedItem) => watchedItem.imdbID === item.imdbID
            )}
          />
        ))}
      {
        <p>
          If you have not found what you have been looking for, try to be more
          specific.
        </p>
      }
    </Search>
  );
}

const Search = styled.div`
  input {
    margin-bottom: 2vh;
  }

  p {
    width: 80vw;
    margin: auto auto 3vh;
  }
`;
