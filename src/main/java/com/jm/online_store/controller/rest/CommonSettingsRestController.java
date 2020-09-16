package com.jm.online_store.controller.rest;

import com.jm.online_store.exception.SettingsNotFound;
import com.jm.online_store.model.CommonSettings;
import com.jm.online_store.service.interf.CommonSettingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/commonSettings")
@AllArgsConstructor
public class CommonSettingsRestController {
    private final CommonSettingsService commonSettingsService;

    @PostMapping
    public ResponseEntity<String> addNewSetting(@RequestBody CommonSettings commonSetting) {
        commonSettingsService.addSetting(commonSetting);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<CommonSettings> updateSetting(@RequestBody CommonSettings commonSettings) {
        try {
            return ResponseEntity.ok(commonSettingsService.updateSetting(commonSettings));
        } catch (SettingsNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{settingName}")
    public ResponseEntity<CommonSettings> getCommonSettingByName(@PathVariable String settingName) {
        try {
            return ResponseEntity.ok(commonSettingsService.getSettingByName(settingName));
        } catch (SettingsNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }
}