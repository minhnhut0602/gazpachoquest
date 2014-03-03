package net.sf.gazpachoquest.dto.answers;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//import org.codehaus.jackson.annotate.JsonSubTypes;
//import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = TextAnswer.class, name = "T"),
        @JsonSubTypes.Type(value = NumericAnswer.class, name = "N"),
        @JsonSubTypes.Type(value = BooleanAnswer.class, name = "B"),
        @JsonSubTypes.Type(value = MultipleAnswer.class, name = "M"),
        @JsonSubTypes.Type(value = LongTextAnswer.class, name = "L") })
public interface Answer extends Serializable {
    Object getValue();
}
