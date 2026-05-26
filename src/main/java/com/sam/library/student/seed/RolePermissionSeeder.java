package com.sam.library.student.seed;

import com.sam.library.student.entity.Permission;
import com.sam.library.student.entity.Role;
import com.sam.library.student.entity.SysUser;
import com.sam.library.student.repository.PermissionRepository;
import com.sam.library.student.repository.RoleRepository;
import com.sam.library.student.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Order(2)
@Component
@RequiredArgsConstructor
public class RolePermissionSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final SysUserRepository sysUserRepository;

    // ─── Add a new module here to auto-generate its READ/CREATE/UPDATE/DELETE permissions ──
    private static final List<String> MODULES = List.of(
        "STUDENT", "PRODUCT", "BRAND", "CATEGORY",
        "CLIENT", "SUPPLIER", "USER", "ROLE", "PERMISSION",
        "ORDER", "PURCHASE_ORDER", "STOCK"
    );

    private static final String[] CRUD = {"READ", "CREATE", "UPDATE", "DELETE"};

    private static final Map<String, String> ACTION_LABEL = Map.of(
        "READ", "View", "CREATE", "Create", "UPDATE", "Update", "DELETE", "Delete"
    );

    @Override
    @Transactional
    public void run(String... args) {
        seedPermissions();
        seedRoles();
        seedUserRoles();
    }

    // ─── Permissions ─────────────────────────────────────────────────────────────

    private void seedPermissions() {
        // Load existing names so only missing permissions are inserted on each startup.
        // This means adding a module to the MODULES list above is enough to seed it.
        Set<String> existing = permissionRepository.findAll().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        List<Permission> toSave = new ArrayList<>();
        for (String module : MODULES) {
            String moduleLabel = module.charAt(0) + module.substring(1).toLowerCase();
            for (String action : CRUD) {
                String name = module + "_" + action;
                if (!existing.contains(name)) {
                    String desc = ACTION_LABEL.get(action) + " " + moduleLabel;
                    toSave.add(perm(name, action, desc, module));
                }
            }
        }
        if (!toSave.isEmpty()) {
            permissionRepository.saveAll(toSave);
        }
    }

    // ─── Roles ───────────────────────────────────────────────────────────────────

    private void seedRoles() {
        if (roleRepository.count() > 0) {
            return;
        }

        Map<String, Permission> all = permissionRepository.findAll().stream()
                .collect(Collectors.toMap(Permission::getName, p -> p));

        // ADMIN — full CRUD on every module
        Role admin = role("Administrator", "ADMIN", false, "Full system access",
                perms(all, MODULES.toArray(String[]::new)));

        // MANAGER — full CRUD on business modules (no user/role/permission management)
        Role manager = role("Manager", "MANAGER", false, "Manage business data",
                perms(all, "STUDENT", "PRODUCT", "BRAND", "CATEGORY",
                           "CLIENT", "SUPPLIER", "ORDER", "PURCHASE_ORDER", "STOCK"));

        // STAFF — default role, limited write access on core modules
        Role staff = role("Staff", "STAFF", true, "Day-to-day operations",
                permsOf(all,
                    module("STUDENT",        "READ", "CREATE", "UPDATE"),
                    module("PRODUCT",        "READ", "CREATE", "UPDATE"),
                    module("BRAND",          "READ"),
                    module("CATEGORY",       "READ"),
                    module("CLIENT",         "READ", "CREATE"),
                    module("SUPPLIER",       "READ"),
                    module("ORDER",          "READ", "CREATE"),
                    module("PURCHASE_ORDER", "READ", "CREATE"),
                    module("STOCK",          "READ", "CREATE")
                ));

        // GUEST — read-only across all business modules
        Role guest = role("Guest", "GUEST", false, "Read-only access",
                permsOf(all,
                    module("STUDENT",        "READ"),
                    module("PRODUCT",        "READ"),
                    module("BRAND",          "READ"),
                    module("CATEGORY",       "READ"),
                    module("CLIENT",         "READ"),
                    module("SUPPLIER",       "READ"),
                    module("ORDER",          "READ"),
                    module("PURCHASE_ORDER", "READ"),
                    module("STOCK",          "READ")
                ));

        roleRepository.saveAll(List.of(admin, manager, staff, guest));
    }

    // ─── User roles ──────────────────────────────────────────────────────────────

    private void seedUserRoles() {
        List<SysUser> users = sysUserRepository.findAll();
        if (users.stream().anyMatch(u -> !u.getRoles().isEmpty())) {
            return;
        }

        Map<String, SysUser> userMap = users.stream()
                .collect(Collectors.toMap(SysUser::getName, u -> u));
        Map<String, Role> roleMap = roleRepository.findAll().stream()
                .collect(Collectors.toMap(Role::getCode, r -> r));

        assign(userMap, roleMap, "admin",   "ADMIN");
        assign(userMap, roleMap, "manager", "MANAGER");
        assign(userMap, roleMap, "staff",   "STAFF");
        assign(userMap, roleMap, "guest",   "GUEST");

        sysUserRepository.saveAll(users);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    /** All CRUD permissions for each of the listed modules. */
    private Set<Permission> perms(Map<String, Permission> all, String... modules) {
        Set<Permission> result = new HashSet<>();
        for (String module : modules) {
            for (String action : CRUD) {
                Permission p = all.get(module + "_" + action);
                if (p != null) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    /** Bundles a module name with its allowed actions for use in {@link #permsOf}. */
    private String[] module(String module, String... actions) {
        String[] entry = new String[actions.length + 1];
        entry[0] = module;
        System.arraycopy(actions, 0, entry, 1, actions.length);
        return entry;
    }

    /** Specific action subsets per module. Each entry is built with {@link #module}. */
    private Set<Permission> permsOf(Map<String, Permission> all, String[]... moduleActions) {
        Set<Permission> result = new HashSet<>();
        for (String[] entry : moduleActions) {
            String module = entry[0];
            for (int i = 1; i < entry.length; i++) {
                Permission p = all.get(module + "_" + entry[i]);
                if (p != null) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    private Permission perm(String name, String action, String description, String module) {
        Permission p = new Permission();
        p.setName(name);
        p.setAction(action);
        p.setDescription(description);
        p.setModule(module);
        return p;
    }

    private Role role(String name, String code, boolean isDefault, String description,
                      Set<Permission> permissions) {
        Role r = new Role();
        r.setName(name);
        r.setCode(code);
        r.setDefault(isDefault);
        r.setDescription(description);
        r.setPermissions(permissions);
        return r;
    }

    private void assign(Map<String, SysUser> users, Map<String, Role> roles,
                        String username, String roleCode) {
        SysUser user = users.get(username);
        Role role = roles.get(roleCode);
        if (user != null && role != null) {
            user.getRoles().add(role);
        }
    }
}
