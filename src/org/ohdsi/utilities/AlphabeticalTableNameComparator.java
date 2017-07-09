package org.ohdsi.utilities;

import org.ohdsi.rabbitInAHat.dataModel.Table;

import java.util.Comparator;

/**
 * Created by q798470 on 7/9/2017.
 */
public class AlphabeticalTableNameComparator implements Comparator< Table> {

    @Override
    public int compare(Table o1, Table o2) {
        if(o1 != null && o2 != null) {
            if(!StringUtilities.isNullOrEmptyString(o1.getName()) && !StringUtilities.isNullOrEmptyString(o2.getName())) {
                return o1.getName().compareTo(o2.getName());
            }else{
                return 0;
            }
        }
        return 0;
    }
}

