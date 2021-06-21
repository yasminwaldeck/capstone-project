package service;

import de.neuefische.backend.config.OMDbConfig;
import de.neuefische.backend.model.*;
import de.neuefische.backend.repo.WatchlistRepo;
import de.neuefische.backend.service.OmdbApiService;
import de.neuefische.backend.service.WatchlistService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;

public class WatchlistServiceTest {

    private final WatchlistRepo watchlistRepo = mock(WatchlistRepo.class);
    private final OMDbConfig omDbConfig = new OMDbConfig();
    private final RestTemplate mockedTemplate = mock(RestTemplate.class);
    private final OmdbApiService omdbApiService = new OmdbApiService(omDbConfig, mockedTemplate);
    private final WatchlistService watchlistService = new WatchlistService(watchlistRepo, omdbApiService);

    @Test
    public void addMovieToDatabaseTest(){
        //GIVEN
        MovieAndSeries itemToAdd = MovieAndSeries.builder().imdbID("imdbID").type("type").build();

        when(mockedTemplate.getForEntity(
                "https://www.omdbapi.com/?apikey=" + omDbConfig.getKey() + "&i=imdbID", OmdbOverviewDto.class))
                .thenReturn(ResponseEntity.ok(OmdbOverviewDto.builder().imdbID("imdbID").build()));

        //WHEN
        watchlistService.addToWatchlist(itemToAdd);

        //THEN
        verify(watchlistRepo).save(MovieAndSeries.builder().imdbID("imdbID").type("type").build());
    }

    @Test
    public void deleteMovieToDatabaseTest(){
        //GIVEN


        //WHEN
        watchlistService.removeFromWatchlist("imdbID");

        //THEN
        verify(watchlistRepo).deleteByImdbID("imdbID");
    }

    @Test
    public void getWatchlistByTypeShouldReturnWatchlistByType() {
        // GIVEN
        when(watchlistRepo.findByType("type")).thenReturn(List.of(
                MovieAndSeries.builder().imdbID("id1").type("type").build(),
                MovieAndSeries.builder().imdbID("id2").type("type").build()
        ));

        OmdbOverviewDto omdbOverviewDto1 = new OmdbOverviewDto(
                "title", "year", "id1", "poster", "type"
        );
        OmdbOverviewDto omdbOverviewDto2 = new OmdbOverviewDto(
                "title", "year", "id2", "poster", "type"
        );

        when(mockedTemplate.getForEntity(
                "https://www.omdbapi.com/?apikey=" + omDbConfig.getKey() + "&i=id1", OmdbOverviewDto.class))
                .thenReturn(ResponseEntity.ok(omdbOverviewDto1));

        when(mockedTemplate.getForEntity(
                "https://www.omdbapi.com/?apikey=" + omDbConfig.getKey() + "&i=id2", OmdbOverviewDto.class))
                .thenReturn(ResponseEntity.ok(omdbOverviewDto2));

        // WHEN
        List<OmdbOverview> items = watchlistService.getWatchlistByType(Optional.of("type"));

        // THEN
        assertThat(items, is(List.of(
                new OmdbOverview(
                        "title", "year", "id1", "poster", "type"
                ),
                new OmdbOverview(
                        "title", "year", "id2", "poster", "type"
                )
        )));
        verify(watchlistRepo).findByType("type");
    }


}