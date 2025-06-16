package com.lycmoons.mapper;
import com.lycmoons.entity.dto.AccountDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {
    // 使用 username 来查询用户
    @Select("select * from Account where username = #{username}")
    AccountDTO findAccountByUsername(@Param("username") String username);

    // 使用 email 来查询用户
    @Select("select * from Account where email = #{email}")
    AccountDTO findAccountByEmail(@Param("email") String email);

    // 使用 id 来查询用户
    @Select("select * from Account where id = #{id}")
    AccountDTO findAccountById(@Param("id") Integer id);

    // 插入新用户
    @Insert("insert into Account values (#{id}, #{username}, #{email}, #{password}, #{role})")
    int insertAccount(AccountDTO account);

    // 修改密码
    @Update("update Account set password = #{password} where email = #{email}")
    int updatePassword(@Param("email") String email, @Param("password") String password);
}
