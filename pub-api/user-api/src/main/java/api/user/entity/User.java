package api.user.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;

    private String source;

    private String userId;

    private String userName;
}
