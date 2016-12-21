package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TwitterId {

    private final String name;
    private final Long id;
}
