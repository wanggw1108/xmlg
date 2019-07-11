package com.temporary.center.ls_service.service.comm;

import com.temporary.center.ls_service.domain.Logtable;

import java.sql.SQLException;

public interface LogtableService {

	/**
     * ������־
     * @param log
     * @return
     * @throws SQLException
     */
    public boolean addLog(Logtable log) throws SQLException;
    
}
