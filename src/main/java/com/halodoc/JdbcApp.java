package com.halodoc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.httprpc.sql.Parameters;

public class JdbcApp {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");

        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bootcamp?useSSL=false","root","");

        String query= "select username from BaseUser where username= :name";
        Map<String, Object> namedParameter= new HashMap<String, Object>();
        namedParameter.put("name","naveen");

        PreparedStatement preparedStatement=getPreparedStatement(query,con,namedParameter);

        ResultSet resultSet=preparedStatement.executeQuery();
//        ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
//        System.out.println(resultSetMetaData.getColumnCount());

        if(resultSet!=null){
            while(resultSet.next()){
                System.out.println(resultSet.getString("username"));
            }
        }
    }

    
    public static PreparedStatement getPreparedStatement(String sqlQuery,Connection connection,Map<String, Object> namedParameterMap)
            throws SQLException {
        final Parameters parameters = Parameters.parse(sqlQuery);

        final PreparedStatement preparedStatement = connection.prepareStatement(parameters.getSQL());
        if (containsParameter(parameters.getSQL())) {
            parameters.apply(preparedStatement, namedParameterMap);
        }
        return preparedStatement;
    }


    private static boolean containsParameter(final String sql) {
        return StringUtils.isNotBlank(sql) && sql.contains("?");
    }
}
