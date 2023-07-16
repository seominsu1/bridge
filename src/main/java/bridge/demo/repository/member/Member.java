package bridge.demo.repository.member;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "member")
@Getter
public class Member {

    @Id
    private String memberId;
    private String email;
    private String name;


    public Member(String memberId, String email, String name) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
    }
}
