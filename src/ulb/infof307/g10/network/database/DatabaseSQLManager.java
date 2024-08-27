package ulb.infof307.g10.network.database;

import java.sql.*;

/**
 * The class DatabaseSQLManager is used to manage SQL requests.
 */
public class DatabaseSQLManager {
    private static DatabaseSQLManager instance = null;
    private Connection connection = null;

    private DatabaseSQLManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error connecting to database.");
            e.printStackTrace();
        }
    }

    /**
     * This function returns the instance of the DatabaseSQLManager used or created
     * @return instance of a new DatabaseSQLManager
     */
    public static synchronized DatabaseSQLManager getInstance() {
        if (instance == null) {
            instance = new DatabaseSQLManager();
        }
        return instance;
    }

    /**
     * This function implements the insert method of the SQL's language
     * The insert method add values in columns to a table
     * @param table is the name of the database's table
     * @param columns is a column list in database's table
     * @param values is a list of information
     */
    public void insert(String table, String[] columns, String[] values) {
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            columnBuilder.append(columns[i]);
            valueBuilder.append("?");
            if (i < columns.length - 1) {
                columnBuilder.append(",");
                valueBuilder.append(",");
            }
        }
        String sql = "INSERT INTO " + table + "(" + columnBuilder.toString() + ") VALUES(" + valueBuilder.toString() + ")";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Insert error");
        }
    }

    /**
     * This function implements the update method of the SQL's language
     * This update method redefined new values in specific columns of the table
     * @param table name of database's table
     * @param where position in the database's table
     * @param columns is a column list in database's table
     * @param values is a list of information
     */
    public void update(String table, String[] columns, String[] values, String where) {
        StringBuilder setBuilder = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            setBuilder.append(columns[i]).append("=?");
            if (i < columns.length - 1) {
                setBuilder.append(",");
            }
        }
        String sql = "UPDATE " + table + " SET " + setBuilder.toString() + " WHERE " + where;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function implements the update method of the SQL's language
     * This update method redefined specific values in specific columns of the table
     * @param table is the name of the database's table
     * @param columns is a column list in database's table
     * @param values is a list of information
     * @param whereColumns position of the columns in the database's table
     * @param whereValues position of values in the database's table
     */
    public void update(String table, String[] columns, String[] values, String[] whereColumns, String[] whereValues) {
        StringBuilder setBuilder = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            setBuilder.append(columns[i]).append(" = ?");
            if (i < columns.length - 1) {
                setBuilder.append(", ");
            }
        }

        StringBuilder whereBuilder = new StringBuilder();
        for (int i = 0; i < whereColumns.length; i++) {
            whereBuilder.append(whereColumns[i]).append(" = ?");
            if (i < whereColumns.length - 1) {
                whereBuilder.append(" AND ");
            }
        }
        String sql = "UPDATE " + table + " SET " + setBuilder.toString() + " WHERE " + whereBuilder.toString();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            int parameterIndex = 1;
            // Set values for SET clause
            for (String value : values) {
                statement.setString(parameterIndex++, value);
            }
            // Set values for WHERE clause
            for (String whereValue : whereValues) {
                statement.setString(parameterIndex++, whereValue);
            }
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function implements the delete method of the SQL's language
     * The delete method deletes values in the table
     * @param table is the name of the database's table
     * @param conditions SQL's queries conditions
     * @param values is a list of information
     */
    public void delete(String table, String[] conditions, String[] values) throws SQLException {
        StringBuilder conditionBuilder = new StringBuilder();
        for (int i = 0; i < conditions.length; i++) {
            conditionBuilder.append(conditions[i]).append("=?");
            if (i < conditions.length - 1) {
                conditionBuilder.append(" AND ");
            }
        }
        String sql = "DELETE FROM " + table + " WHERE " + conditionBuilder.toString();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }
            statement.execute();
        } catch (SQLException e) {
           throw e;
        }
    }

    /**
     * This function implements the select method of the SQL's language
     * The select method returns data from the selected table
     * @param table is the name of the database's table
     * @param columns is a column list in database's table
     * @param conditions SQL's queries conditions
     * @param values is a list of information
     * @return Returns the selected information
     */
    public ResultSet select(String table, String[] columns, String[] conditions, String[] values) {
        StringBuilder columnBuilder = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            columnBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                columnBuilder.append(",");
            }
        }
        StringBuilder conditionBuilder = new StringBuilder();
        for (int i = 0; i < conditions.length; i++) {
            conditionBuilder.append(conditions[i]).append("=?");
            if (i < conditions.length - 1) {
                conditionBuilder.append(" AND ");
            }
        }
        String sql = "SELECT " + columnBuilder.toString() + " FROM " + table + " WHERE " + conditionBuilder.toString();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * This function implements the Create Table method of the SQL's language
     * The Create Table method create a new table with columns and restriction types in the database
     * @param table is the name of the database's table
     * @param columns is a column list in the database's table
     * @param types list of type in the database's table
     */
    public void createTable(String table, String[] columns, String[] types) throws SQLException {
        if (columns.length != types.length) {
            throw new IllegalArgumentException("Columns and types arrays must have the same length");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i]).append(" ").append(types[i]);
            if (i < columns.length - 1) {
                builder.append(",");
            }
        }
        String sql = "CREATE TABLE IF NOT EXISTS " + table + "(" + builder.toString() + ")";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw e;
        }
    }
}
