package edu.java.scrapper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.java.scrapper.utils.UnixEpochTimeDeserializer;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowQuestionResponse(
    @JsonProperty("items")
    List<QuestionItem> itemList
) {
    public record QuestionItem(
        @JsonProperty("is_answered")
        boolean isAnswered,

        @JsonProperty("answer_count")
        Long answerCount,

        @JsonProperty("last_activity_date")
        @JsonDeserialize(using = UnixEpochTimeDeserializer.class)
        OffsetDateTime lastActivityDate,

        @JsonProperty("creation_date")
        @JsonDeserialize(using = UnixEpochTimeDeserializer.class)
        OffsetDateTime creationDate,

        @JsonProperty("last_edit_date")
        @JsonDeserialize(using = UnixEpochTimeDeserializer.class)
        OffsetDateTime lastEditDate,

        @JsonProperty("question_id")
        Long questionId,

        @JsonProperty("link")
        String link,

        @JsonProperty("title")
        String title

    ) {
    }
}
