package com.aleyn.best_travel.infrastructure.abstract_services;

import java.util.Map;
import java.util.Set;

public interface ModifyUserService {

    Map<String, Boolean> enabled(String userName);

    Map<String, Set<String>> addRole(String userName, String role);

    Map<String, Set<String>> removeRole(String userName, String role);

}
