package de.neuefische.backend.model.OMDb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OmdbOverview {

    //Should be deleted soon
    private String title;
    private String year;
    private String imdbID;
    private String poster;
    private String type;
}
