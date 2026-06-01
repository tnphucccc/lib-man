package com.example.backend.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that the record-based DTOs serialize/deserialize through the
 * Spring-configured Jackson mapper with the same JSON contract as before the
 * record migration (field names = component names, request binding via the
 * canonical constructor).
 */
@JsonTest
class DtoJsonContractTest {

    @Autowired
    private JacksonTester<BookDTO> bookJson;

    @Autowired
    private JacksonTester<BorrowingDTO> borrowingJson;

    @Autowired
    private JacksonTester<BorrowerPatchDTO> patchJson;

    @Test
    void bookDtoSerializesWithExpectedKeys() throws Exception {
        BookDTO dto = new BookDTO(
                1L, "Dune", "9780441013593", 1965,
                Set.of(new AuthorDTO.AuthorSummaryDTO(7L, "Frank Herbert", "American", null)),
                "AVAILABLE", "http://cover");

        assertThat(bookJson.write(dto))
                .hasJsonPath("$.bookId")
                .hasJsonPath("$.title")
                .hasJsonPath("$.isbn")
                .hasJsonPath("$.publicationYear")
                .hasJsonPath("$.authors")
                .hasJsonPath("$.status")
                .hasJsonPath("$.coverImageUrl")
                // nested summary keys
                .hasJsonPath("$.authors[0].id")
                .hasJsonPath("$.authors[0].name")
                .hasJsonPath("$.authors[0].nationality")
                .hasJsonPath("$.authors[0].portraitUrl");
    }

    @Test
    void bookDtoDeserializesFromRequestJson() throws Exception {
        String json = """
                {
                  "title": "Dune",
                  "isbn": "9780441013593",
                  "publicationYear": 1965,
                  "status": "AVAILABLE",
                  "authors": [{"id": 7, "name": "Frank Herbert", "nationality": "American"}]
                }
                """;
        BookDTO dto = bookJson.parseObject(json);
        assertThat(dto.title()).isEqualTo("Dune");
        assertThat(dto.isbn()).isEqualTo("9780441013593");
        assertThat(dto.publicationYear()).isEqualTo(1965);
        assertThat(dto.bookId()).isNull(); // omitted -> null, as before
        assertThat(dto.authors()).hasSize(1);
        assertThat(dto.authors().iterator().next().name()).isEqualTo("Frank Herbert");
    }

    @Test
    void borrowingDtoRoundTripsTimestamps() throws Exception {
        BorrowingDTO dto = new BorrowingDTO(
                5L, 1L, 2L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1), null,
                LocalDateTime.of(2026, 1, 1, 10, 30), LocalDateTime.of(2026, 1, 1, 10, 30),
                "BORROWED");
        BorrowingDTO back = borrowingJson.parseObject(borrowingJson.write(dto).getJson());
        assertThat(back).isEqualTo(dto);
        assertThat(back.createdAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 10, 30));
    }

    @Test
    void patchDtoOmittedFieldsAreNull() throws Exception {
        BorrowerPatchDTO dto = patchJson.parseObject("{\"name\": \"Alice\"}");
        assertThat(dto.name()).isEqualTo("Alice");
        assertThat(dto.email()).isNull();
        assertThat(dto.status()).isNull();
    }
}
