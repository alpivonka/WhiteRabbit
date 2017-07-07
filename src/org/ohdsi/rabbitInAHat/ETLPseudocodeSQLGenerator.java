package org.ohdsi.rabbitInAHat;


import org.ohdsi.rabbitInAHat.dataModel.ETL;
import org.ohdsi.rabbitInAHat.dataModel.Field;
import org.ohdsi.rabbitInAHat.dataModel.ItemToItemMap;
import org.ohdsi.rabbitInAHat.dataModel.MappableItem;
import org.ohdsi.rabbitInAHat.dataModel.Mapping;
import org.ohdsi.rabbitInAHat.dataModel.Table;
import org.ohdsi.utilities.GenericSQLStatements;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by Al Pivonka on 6/29/2017.
 * This class will generate Pseudocode SQL from the ETL model
 * The Pseudocode SQL follows the https://www.w3schools.com/sql/sql_insert_into_select.asp Pattern
 * All ETL comments and Logic are added to the output Pseudocode SQL as SQL comments.
 * Each Attribute in the select statement selects an attribute and uses the AS clause.
 * Behind each attribute is a comment of it's type from source to destination.
 *
 */
public class ETLPseudocodeSQLGenerator {

    private static String startSQLComment="/*";
    private static String endSQLComment="*/";


    private static boolean isNullOrEmptyString(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Still a work in progress, needing more elaborate examples
     * This will only generate Pseudocode SQL for mapped fields ....
     * @param etl
     * @param filename
     */
    public static void generate(ETL etl,String filename,DialogStatus ds) {

        if(ds.isGeneratePseudocodeSql()) {
            StringBuilder output = new StringBuilder();
            for (Table targetTable : etl.getTargetDatabase().getTables()) {
                for (ItemToItemMap tableToTableMap : etl.getTableToTableMapping().getSourceToTargetMaps()) {
                    if (tableToTableMap.getTargetItem() == targetTable) {
                        StringBuilder insertIntoList = new StringBuilder();
                        StringBuilder sourceSelectList = new StringBuilder();
                        StringBuilder logic = new StringBuilder();
                        StringBuilder comment = new StringBuilder();

                        Table sourceTable = (Table) tableToTableMap.getSourceItem();
                        Mapping<Field> fieldtoFieldMapping = etl.getFieldToFieldMapping(sourceTable, targetTable);

                        for (MappableItem targetField : fieldtoFieldMapping.getTargetItems()) {

                            for (ItemToItemMap fieldToFieldMap : fieldtoFieldMapping.getSourceToTargetMaps()) {
                                if (fieldToFieldMap.getTargetItem() == targetField) {
                                    if (sourceSelectList.length() != 0) {
                                        sourceSelectList.append("\n,");
                                    }
                                    sourceSelectList.append(sourceTable.getName() + "." + fieldToFieldMap.getSourceItem().getName().trim() + " "
                                            + startSQLComment + sourceTable.getFieldByName(fieldToFieldMap.getSourceItem().getName().trim()).getType() + endSQLComment
                                            + " AS " + fieldToFieldMap.getTargetItem().getName().trim() + " "
                                            + startSQLComment + targetTable.getFieldByName(fieldToFieldMap.getTargetItem().getName().trim()).getType() + endSQLComment);
                                    if (!isNullOrEmptyString(fieldToFieldMap.getLogic().trim()))
                                        sourceSelectList.append("\n /* FieldToField LOGIC : " + fieldToFieldMap.getLogic().trim() + "*/");
                                    if (!isNullOrEmptyString(fieldToFieldMap.getComment().trim()))
                                        sourceSelectList.append("\n /* FieldToField COMMENT : " + fieldToFieldMap.getComment().trim() + "*/");
                                    if (insertIntoList.length() != 0)
                                        insertIntoList.append(",");
                                    insertIntoList.append(fieldToFieldMap.getTargetItem().getName().trim());
                                }
                            }

                            for (Field field : targetTable.getFields()) {
                                if (field.getName().equals(targetField.getName())) {
                                    if (comment.length() != 0)
                                        comment.append("\n");
                                    if (!isNullOrEmptyString(field.getComment().trim()))
                                        comment.append(" /* Target Field Comment : " + field.getComment().trim() + "*/");
                                }
                            }
                        }
                        if (!isNullOrEmptyString(insertIntoList.toString()))
                            output.append("INSERT INTO " + targetTable.getName().trim().toUpperCase() + " (" + insertIntoList.toString().toUpperCase() + ")");
                        if (!isNullOrEmptyString(sourceSelectList.toString())) {
                            output.append("\nSELECT \n" + sourceSelectList.toString().toUpperCase());
                            output.append("\nFROM " + sourceTable.getName().trim().toUpperCase() + ";\n\n");
                        }
                        if (!isNullOrEmptyString(logic.toString()))
                            output.append(logic.toString().toUpperCase());
                        if (!isNullOrEmptyString(comment.toString()))
                            output.append(comment.toString().toUpperCase());
                    }
                }
            }//end loops
            //write out put to file
            String[] fileNameSplit = filename.split("\\.(?=[^\\.]+$)");
            writeToFile(output.toString().toUpperCase(), fileNameSplit[0] + "_ETLPseudocodeSQL" + ".sql" );
        }
        if(ds.isTargetFillRates())
        generateTargetFillRatesSQL(etl, filename);
        if(ds.isSourceFillRates())
        generateSourceFillRatesSQL(etl, filename);
    }


    private static void writeToFile(String output, String filename){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(output.toString());
            writer.flush();
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /*
    Source and target Fill Rates SQL
    SELECT 'StringTableName' AS TABLE_NAME,'StringColumnName' AS TABLE_COLUMN,
    100.0 * COUNT(columnName) / COUNT(1) AS FILL_RATE
    FROM tableName

    CREATE table FILL_RATE(
    TABLE_NAME STRING,
    TABLE_COLUMN STRING,
    FILL_RATE LONG)
 */



    /**
     * Generates the Insert statements for the Source DB's Fill Rates
     * Table to attributes.
     * @param etl
     * @param filename
     */
    public static void generateSourceFillRatesSQL(ETL etl,String filename) {
        StringBuilder fill_rate_sql_SB = new StringBuilder();
        for(Table sourceTable:etl.getSourceDatabase().getTables()){
            fill_rate_sql_SB.append("--"+sourceTable.getName()+"\n");
            for(Field field:sourceTable.getFields()) {
                fill_rate_sql_SB.append(GenericSQLStatements.SOURCE_FILLRATE_INSERT_SQL +"'"+sourceTable.getName()+"','"+field.getName()+"','"+field.getFillRate()+"');\n");
            }
        }
        //System.out.println(fill_rate_sql_SB.toString());
        String[] fileNameSplit = filename.split("\\.(?=[^\\.]+$)");
        writeToFile(fill_rate_sql_SB.toString().toUpperCase(),fileNameSplit[0]+"_SourceDB_FillRates"+".sql");
    }

    /**
     * Generates the Insert statements for the Target DB's Fill Rates
     * Table to attributes.
     * @param etl
     * @param filename
     */
    public static void generateTargetFillRatesSQL(ETL etl,String filename) {
        StringBuilder fill_rate_sql_SB = new StringBuilder();
        for(Table targetTable:etl.getTargetDatabase().getTables()){
            fill_rate_sql_SB.append("--"+targetTable.getName()+"\n");
            for(Field field:targetTable.getFields()) {
                fill_rate_sql_SB.append(
                        GenericSQLStatements.TARGET_FILLRATE_SQL.toString().replaceAll("#TABLENAME#", targetTable.getName())
                                .replaceAll("#COLUMNNAME#", field.getName()));
            }
        }

        String[] fileNameSplit = filename.split("\\.(?=[^\\.]+$)");
        writeToFile(fill_rate_sql_SB.toString().toUpperCase(),fileNameSplit[0]+"_TargetDB_FillRates"+".sql");
    }
}
