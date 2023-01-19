package cn.wildfirechat.common.model.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectDataForm {
    private String username;
    private String clientId;
    private String password;
}
