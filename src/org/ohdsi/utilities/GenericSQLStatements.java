package org.ohdsi.utilities;

/**
 * Created by q798470 on 7/6/2017.
 */
public class GenericSQLStatements {
    public static String INSERT_INTO_STATEMENT = "INSERT INTO FILL_RATE(TABLE_NAME,TABLE_COLUMN,FILL_RATE)";

    public static String FILLRATE_SQL ="\nSELECT '#TABLENAME#' AS TABLE_NAME,'#COLUMNNAME#' AS TABLE_COLUMN,"
            +"\n(100.0 * COUNT(#COLUMNNAME#)) / COUNT(1) AS FILL_RATE"
            +"\nFROM #TABLENAME#;\n ";

    public static String TARGET_FILLRATE_SQL = INSERT_INTO_STATEMENT+FILLRATE_SQL;

    public static String SOURCE_FILLRATE_INSERT_SQL =INSERT_INTO_STATEMENT+"\n VALUES(";

    public static String FILLRATE_ONLY_SQL ="SELECT (100.0 * COUNT(#COLUMNNAME#)) / COUNT(1) AS FILL_RATE FROM #TABLENAME#;";


}
