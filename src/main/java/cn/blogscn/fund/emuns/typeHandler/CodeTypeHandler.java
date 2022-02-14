package cn.blogscn.fund.emuns.typeHandler;

import cn.blogscn.fund.emuns.CodeType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;


public class CodeTypeHandler implements TypeHandler<CodeType> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, CodeType codeType,
            JdbcType jdbcType) throws SQLException {
        if(codeType == null)
            codeType = CodeType.COMMON;
        preparedStatement.setInt(i,codeType.getCode());
    }

    @Override
    public CodeType getResult(ResultSet resultSet, String s) throws SQLException {
        int anInt = resultSet.getInt(s);
        CodeType codeTypeByCode = CodeType.getCodeTypeByCode(anInt);
        return codeTypeByCode;
    }

    @Override
    public CodeType getResult(ResultSet resultSet, int i) throws SQLException {
        int anInt = resultSet.getInt(i);
        CodeType codeTypeByCode = CodeType.getCodeTypeByCode(anInt);
        return codeTypeByCode;
    }

    @Override
    public CodeType getResult(CallableStatement callableStatement, int i) throws SQLException {
        int anInt = callableStatement.getInt(i);
        CodeType codeTypeByCode = CodeType.getCodeTypeByCode(anInt);
        return codeTypeByCode;
    }
}
