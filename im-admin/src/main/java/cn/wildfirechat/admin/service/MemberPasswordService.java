package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.MemberPasswordMapper;
import cn.wildfirechat.common.model.po.MemberPasswordPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
public class MemberPasswordService extends BaseService {

    public static final String ALGORITHM_NAME = "SHA-1";

    @Resource
    private MemberPasswordMapper memberPasswordMapper;

    public void insertPassword(MemberPasswordPO mp, String password) {
        try{
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM_NAME);
            digest.reset();
            String salt = UUID.randomUUID().toString();
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String hashedPwd = Base64.getEncoder().encodeToString(hashed);
            mp.setPassword(hashedPwd);
            mp.setSalt(salt);
            mp.setTryCount(0);
            mp.setLastTryTime(0);
            mp.setResetCodeTime(0);
            memberPasswordMapper.insert(mp);
        }catch (Exception e){
            log.error("用户密码新增错误{}", e.getMessage(), e);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void changePassword(MemberPasswordPO mp, String password) {
        try{
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM_NAME);
            digest.reset();
            String salt = UUID.randomUUID().toString();
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String hashedPwd = Base64.getEncoder().encodeToString(hashed);
            mp.setPassword(hashedPwd);
            mp.setSalt(salt);
            mp.setLastTryTime(0);
            mp.setTryCount(0);
            memberPasswordMapper.updatePassword(mp);
        }catch (Exception e){
            log.error("用户密码修改错误{}", e.getMessage(), e);
        }
    }


    public void update(MemberPasswordPO mp) {
        memberPasswordMapper.update(mp);
    }

}
