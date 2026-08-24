package com.autodm.server.controller;

import com.autodm.server.dto.SystemSettingsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Value("${autodm.data-dir:${user.home}/.autodm}")
    private String dataDir;

    @Value("${autodm.db-name:autodm.db}")
    private String dbName;

    @GetMapping("/system")
    public ResponseEntity<SystemSettingsDto> getSystemSettings() {
        SystemSettingsDto settings = new SystemSettingsDto(dataDir, dbName);
        return ResponseEntity.ok(settings);
    }
}
