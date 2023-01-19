package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query Object 会员查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPasswordQuery {
    private String uid;// UID
}
