package com.lele.community.mapper;


import com.lele.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

    /**
     * #{}会自动调用User的Getter().
     * 插入一条记录.
     * @param user
     */
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified) values (#{name},#{account_id},#{token},#{gmt_create},#{gmt_modified})")
    void insert(User user);


    @Select("select * from user where token=#{token}")
    User findToken(String token);

}
