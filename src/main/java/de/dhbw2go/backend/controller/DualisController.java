package de.dhbw2go.backend.controller;

import de.dhbw2go.backend.dualis.DualisCookie;
import de.dhbw2go.backend.dualis.models.exam.DualisExamModel;
import de.dhbw2go.backend.dualis.models.overview.DualisOverviewModel;
import de.dhbw2go.backend.dualis.models.semester.DualisSemesterModel;
import de.dhbw2go.backend.payload.requests.dualis.DualisCredentialsRequest;
import de.dhbw2go.backend.services.DualisService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dualis")
@Tag(name = "Dualis")
@SecurityRequirements
public class DualisController {

    @Autowired
    private DualisService dualisService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DualisCookie.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = ""),
    })
    @PostMapping(path = "/cookie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DualisCookie> cookie(@Valid @RequestBody final DualisCredentialsRequest dualisCredentialsRequest) {
        return null;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DualisOverviewModel.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = ""),
    })
    @PostMapping(path = "/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DualisOverviewModel> overview(@Valid @RequestBody final DualisCookie dualisCookie) {
        return null;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = DualisSemesterModel.class)), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = ""),
    })
    @PostMapping(path = "/semesters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DualisSemesterModel>> semesters(@Valid @RequestBody final DualisCookie dualisCookie) {
        return null;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DualisSemesterModel.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = ""),
    })
    @PostMapping(path = "/semester/{semester-reference-arguments}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DualisSemesterModel> semester(@PathVariable("semester-reference-arguments") final String semesterReferenceArguments, @Valid @RequestBody final DualisCookie dualisCookie) {
        return null;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = DualisExamModel.class)), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = ""),
    })
    @PostMapping(path = "/exams/{exams-reference-arguments}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DualisExamModel>> exams(@PathVariable("exams-reference-arguments") final String examReferenceArguments, @Valid @RequestBody final DualisCookie dualisCookie) {
        return null;
    }
}
