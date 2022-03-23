package cn.blogscn.fund.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

public class PersistentLogins {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String series;
    private String token;
    private LocalDateTime validTime;

    public Integer getId() {
        return id;
    }

 /*   这张表是用户校验用户自动登录的表。设计这张表的原因是我看过一些网上的文章介绍使用cookie自动登录，但是他们基本上都是将用户名、密码、salt等字符串拼接之后md5加密然后保存在cookie中。
    虽然使用了md5这类非对称加密方式，但是将密码这类关键信息保存在用户端，我觉得是不太靠谱的。
    因此设计了这张表，将用户名、密码等关键信息加密之后的数据保存到这张表中，在用户的cookie里只保存了没有特殊含义的UUID值以及用户名.
    这张表中的几个字段的含义分别是：
    id    主键
    username    用户名
    series    用户使用密码登录成功之后获取的一个UUID值，同时用户端保存的cookie记录就是：EncryptionUtil.base64Encode(用户名:此UUID值)
    token    在拦截器中校验是否能够登录的密文，其加密方式是：EncryptionUtil.sha256Hex(用户名 + “_” + 密码 + “_” + 自动登录失效的时间点的字符串 + “_” +  自定义的salt)
    validTime    自动登录失效的时间，即：这个时间点之后只能重新用用户名、密码登录，如果在重新登录时勾选了“30天内自动登录”则更新该用户在persistent_logins这个表中的自动登录记录
    */


    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getValidTime() {
        return validTime;
    }

    public void setValidTime(LocalDateTime validTime) {
        this.validTime = validTime;
    }
}
