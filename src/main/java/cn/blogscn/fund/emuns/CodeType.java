package cn.blogscn.fund.emuns;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum  CodeType {
    COMMON(1,"普通基金"),INDEX(2,"指数基金"),ETF(3,"开放式指数基金");
    CodeType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    @EnumValue
    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static CodeType getCodeTypeByCode(Integer code){
        switch (code){
            case 1:
                return COMMON;
            case 2:
                return INDEX;
            case 3:
                return ETF;
            default:
                return COMMON;
        }
    }

    @Override
    public String toString() {
        return "CodeType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
