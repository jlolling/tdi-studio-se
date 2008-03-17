// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model.migration;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Arrays;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.RemovePropertyComponentConversion;
import org.talend.core.model.components.conversions.RenameComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.PropertyComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;

/**
 * Use to rename tDB(Input|Output|SQLRow) into tMysql(Input|Output|Row). Related bug 540.
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class RenametDBInputToMySQLMigrationTask extends AbstractJobMigrationTask {

    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            IComponentConversion removePropertyComponentConversion = new RemovePropertyComponentConversion("TYPE"); //$NON-NLS-1$

            RenameComponentConversion renameComponentConversion = new RenameComponentConversion("tMysqlInput"); //$NON-NLS-1$
            IComponentFilter filter1 = new PropertyComponentFilter("tDBInput", "TYPE", "mysql"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ModifyComponentsAction.searchAndModify(item, filter1, Arrays.<IComponentConversion> asList(renameComponentConversion,
                    removePropertyComponentConversion));

            renameComponentConversion.setNewName("tMysqlOutput"); //$NON-NLS-1$
            IComponentFilter filter2 = new PropertyComponentFilter("tDBOutput", "TYPE", "mysql"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ModifyComponentsAction.searchAndModify(item, filter2, Arrays.<IComponentConversion> asList(renameComponentConversion,
                    removePropertyComponentConversion));

            renameComponentConversion.setNewName("tMysqlRow"); //$NON-NLS-1$
            IComponentFilter filter3 = new PropertyComponentFilter("tDBSQLRow", "TYPE", "mysql"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ModifyComponentsAction.searchAndModify(item, filter3, Arrays.<IComponentConversion> asList(renameComponentConversion,
                    removePropertyComponentConversion));

            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 2, 17, 12, 0, 0);
        return gc.getTime();
    }
}
