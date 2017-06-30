package org.ohdsi.rabbitInAHat;


import org.ohdsi.rabbitInAHat.dataModel.ETL;
import org.ohdsi.rabbitInAHat.dataModel.Field;
import org.ohdsi.rabbitInAHat.dataModel.ItemToItemMap;
import org.ohdsi.rabbitInAHat.dataModel.MappableItem;
import org.ohdsi.rabbitInAHat.dataModel.Mapping;
import org.ohdsi.rabbitInAHat.dataModel.Table;

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
     *
     * @param etl
     * @param filename
     */
    public static void generate(ETL etl,String filename) {
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
                                sourceSelectList.append(sourceTable.getName() + "." + fieldToFieldMap.getSourceItem().getName().trim()+" "
                                        + startSQLComment + sourceTable.getFieldByName(fieldToFieldMap.getSourceItem().getName().trim()).getType() + endSQLComment
                                        + " AS " + fieldToFieldMap.getTargetItem().getName().trim()+" "
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
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(output.toString());
            writer.flush();
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        //check if file exists? and over write it

    }
}
