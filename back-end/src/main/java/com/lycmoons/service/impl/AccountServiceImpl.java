package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.AccountDTO;
import com.lycmoons.entity.vo.request.RegisterVO;
import com.lycmoons.entity.vo.request.ResetPasswordVO;
import com.lycmoons.entity.vo.response.PersonalInfoVO;
import com.lycmoons.mapper.AccountMapper;
import com.lycmoons.service.AccountService;
import com.lycmoons.service.EmailService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    AccountMapper accountMapper;

    @Resource
    EmailService emailService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用 username 来查找数据库中的用户，返回一个 User 类型数据到 Security 上下文供后续密码的校验
        // 如果没有查找到用户名为 username 的用户，就抛出一个 UsernameNotFoundException 异常

        // 由于前端允许使用用户名或邮箱登录，故前端给的这个username参数可能是用户名或邮箱
        AccountDTO account = accountMapper.findAccountByUsername(username);
        if(account == null) account = accountMapper.findAccountByEmail(username);

        if(account == null) throw new UsernameNotFoundException("用户名或密码错误");

        // account 不为空，就返回这个用户的 User 类型到上下文，供 Security 进行密码校验
        return User
                .withUsername(username)  // 这里返回的 username 还是原始用户输入的（用户名或邮箱）
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    @Override
    public String register(RegisterVO vo) {
        String email = vo.getEmail();
        String code = vo.getCode();
        String username = vo.getUsername();
        String password = passwordEncoder.encode(vo.getPassword());  // 密码加密

        // 校验验证码
        String msg = emailService.checkCode(email, code);
        if(msg != null) return msg;

        // 检查用户名/邮箱是否已被使用
        AccountDTO account = accountMapper.findAccountByEmail(email);
        if(account != null) return "邮箱已被使用";
        account = accountMapper.findAccountByUsername(username);
        if(account != null) return "用户名已被使用";

        // 完成注册
        int cnt = accountMapper.insertAccount(new AccountDTO(
                null, username, email, password, "user"
        ));
        if(cnt > 0) return null;
        return "注册失败";
    }

    @Override
    public String resetPassword(ResetPasswordVO vo) {
        String email = vo.getEmail();
        String code = vo.getCode();
        String password = passwordEncoder.encode(vo.getPassword());

        // 验证码校验
        String msg = emailService.checkCode(email, code);
        if(msg != null) return msg;

        // 用户存在性校验
        AccountDTO account = accountMapper.findAccountByEmail(email);
        if(account == null) return "此邮箱用户不存在";

        // 修改密码
        int cnt = accountMapper.updatePassword(email, password);
        if(cnt > 0) return null;
        return "密码修改失败";
    }

    @Override
    public PersonalInfoVO getPersonalInfo(Integer userId) {
        AccountDTO account = accountMapper.findAccountById(userId);
        if(account == null) return null;
        return new PersonalInfoVO(account.getUsername(), account.getEmail());
    }
}