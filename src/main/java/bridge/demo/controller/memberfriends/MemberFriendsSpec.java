package bridge.demo.controller.memberfriends;

import bridge.demo.controller.memberfriends.request.MemberFriendsRequest;
import bridge.demo.controller.memberfriends.response.MemberFriendResponses;
import bridge.demo.controller.memberfriends.response.MemberFriendsResponse;
import bridge.demo.dto.MemberFormDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "MemberFriends", description = "MemberFriends Controller")
public interface MemberFriendsSpec {

	@Operation(summary = "멤버 친구 정보 조회", description = "멤버의 친구 리스트 정보 조회")
	ResponseEntity<MemberFriendResponses> findById(String memberId);

	@Operation(summary = "친구 리스트에 친구 추가", description = "친구 리스트에 친구 추가")
	ResponseEntity addFriends(String memberId, MemberFriendsRequest request);

	@Operation(summary = "친구 리스트에 친구 삭제", description = "친구 리스트에 친구 삭제")
	ResponseEntity subtractFriends(String memberId, MemberFriendsRequest request);

	@Operation(summary = "친구 리스트 생성", description = "친구 리스트 생성")
	ResponseEntity<MemberFriendsResponse> create(MemberFriendsRequest request);

	@Operation(summary = "친구 리스트 삭제", description = "친구 리스트 삭제")
	ResponseEntity deleteFriends(String memberId);

}
