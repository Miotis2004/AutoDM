package com.autodm.server.dto;

public class SystemSettingsDto {
    private String dataDir;
    private String dbName;

    public SystemSettingsDto() {
    }

    public SystemSettingsDto(String dataDir, String dbName) {
        this.dataDir = dataDir;
        this.dbName = dbName;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
