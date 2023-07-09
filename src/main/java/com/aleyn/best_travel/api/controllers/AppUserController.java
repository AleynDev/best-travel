package com.aleyn.best_travel.api.controllers;

import com.aleyn.best_travel.infrastructure.abstract_services.ModifyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@Tag(name = "User")
@RestController
@AllArgsConstructor
@RequestMapping(path = "user")
public class AppUserController {

    private final ModifyUserService modifyUserService;

    @Operation(summary = "Enabled or disabled user")
    @PatchMapping(path = "enabled-or-disabled")
    public ResponseEntity<Map<String, Boolean>> enableOrDisable(@RequestParam String userName) {
        return ResponseEntity.ok(this.modifyUserService.enabled(userName));
    }

    @Operation(summary = "Add role user")
    @PatchMapping(path = "add-role")
    public ResponseEntity<Map<String, Set<String>>> addRole(@RequestParam String userName, @RequestParam String role) {
        return ResponseEntity.ok(this.modifyUserService.addRole(userName, role));
    }

    @Operation(summary = "Remove role user")
    @PatchMapping(path = "remove-role")
    public ResponseEntity<Map<String, Set<String>>> removeRole(@RequestParam String userName, @RequestParam String role) {
        return ResponseEntity.ok(this.modifyUserService.removeRole(userName, role));
    }

}
