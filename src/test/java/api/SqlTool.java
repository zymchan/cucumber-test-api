package api;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import util.StringUtil;

import java.sql.*;
import java.util.Properties;

public class SqlTool {

    final static String CONFIG_PATH="src/test/resources/config.properties";
    final static Properties configs = StringUtil.readPropertiesFile(CONFIG_PATH);

    public String requestResult;

    Connection conn;

    public void initDriver(){
        try {
            Class.forName(configs.getProperty("driverName"));
            conn = DriverManager.getConnection(configs.getProperty("db_url"), configs.getProperty("db_user"), configs.getProperty("db_password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryDb(String sql) throws Exception{
        String result="";
        JSONArray jsonArray= new JSONArray();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            jsonArray.add(getType(rs, rsmd));
        }
        if(!jsonArray.isEmpty()){
            result=jsonArray.toString();
        }
        requestResult = result;
    }

    public void insertDb(String sql) throws Exception{
        Statement st = conn.createStatement();
        st.execute(sql);
    }

    public void updateDb(String sql) throws Exception{
        insertDb(sql);
    }

    private JSONObject getType(ResultSet rs, ResultSetMetaData rsmd) throws Exception{
        JSONObject obj = new JSONObject();
        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            String columnName = rsmd.getColumnLabel(i + 1);
            switch (rsmd.getColumnType(i + 1)) {
                case java.sql.Types.ARRAY:
                    obj.put(columnName, rs.getArray(columnName));
                    break;
                case java.sql.Types.BIGINT:
                    obj.put(columnName, rs.getInt(columnName));
                    break;
                case java.sql.Types.BOOLEAN:
                    obj.put(columnName, rs.getBoolean(columnName));
                    break;
                case java.sql.Types.BLOB:
                    obj.put(columnName, rs.getBlob(columnName));
                    break;
                case java.sql.Types.DOUBLE:
                    obj.put(columnName, rs.getDouble(columnName));
                    break;
                case java.sql.Types.FLOAT:
                    obj.put(columnName, rs.getFloat(columnName));
                    break;
                case java.sql.Types.INTEGER:
                    obj.put(columnName, rs.getInt(columnName));
                    break;
                case java.sql.Types.NVARCHAR:
                    obj.put(columnName, rs.getNString(columnName));
                    break;
                case java.sql.Types.VARCHAR:
                    obj.put(columnName, rs.getString(columnName));
                    break;
                case java.sql.Types.TINYINT:
                    obj.put(columnName, rs.getInt(columnName));
                    break;
                case java.sql.Types.SMALLINT:
                    obj.put(columnName, rs.getInt(columnName));
                    break;
                case java.sql.Types.DATE:
                    obj.put(columnName, rs.getDate(columnName));
                    break;
                case java.sql.Types.TIMESTAMP:
                    obj.put(columnName, rs.getTimestamp(columnName));
                    break;
                default:
                    obj.put(columnName, rs.getObject(columnName));
                    break;
            }
        }
        return obj;
    }
}
