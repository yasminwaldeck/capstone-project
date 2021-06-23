import TypeContext from "../context/TypeContext";
import useWatchlist from "../hooks/useWatchlist";
import {useContext, useState} from "react";
import useTrending from "../hooks/useTrending";
import MovieAndSeriesCard from "../components/MovieAndSeriesCard";


export default function TrendingMovieAndSeriesPage(){


    const {MOVIE, SERIES, DAY, WEEK} = useContext(TypeContext)
    const [searchType, setSearchType] = useState(MOVIE)
    const [timewindow, setTimewindow] = useState(DAY)
    const { watchlist } = useWatchlist();
    const { searchResults } = useTrending(timewindow, searchType);


    return(
        <div>
            <div>
                <input type="radio" name="search_type" onChange={() => setSearchType(MOVIE)} defaultChecked/> Movie
                <input type="radio" name="search_type" onChange={() => setSearchType(SERIES)}/> Series
            </div>
            <div>
                <input type="radio" name="timewindow" onChange={() => setTimewindow(DAY)} defaultChecked/> Day
                <input type="radio" name="timewindow" onChange={() => setTimewindow(WEEK)}/> Week
            </div>
            {searchResults && searchResults.map((item) => (
                <MovieAndSeriesCard key={item.imdbID} item={item} watched={watchlist.find(watchedItem => watchedItem.imdbID === item.imdbID)}/>
            ))}
            {console.log("time: " + timewindow + "type: " + searchType)}
        </div>
    )
}