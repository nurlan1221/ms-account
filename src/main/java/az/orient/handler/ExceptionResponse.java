package az.orient.handler;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExceptionResponse {
    private Integer code;
    private String message;
    private List<FieldException> fieldErrors;
}
