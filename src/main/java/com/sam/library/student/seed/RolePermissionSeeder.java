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

import java.util.Arrays;
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

    @Override
    @Transactional
    public void run(String... args) {
        seedPermissions();
        seedRoles();
        seedUserRoles();
    }

    // ─── Permissions ────────────────────────────────────────────────────────────

    private void seedPermissions() {
        if (permissionRepository.count() > 0) return;

        List<Permission> permissions = Arrays.asList(
            // Student
            perm("STUDENT_READ",       "READ",   "View students",          "STUDENT"),
            perm("STUDENT_CREATE",     "CREATE", "Create student",         "STUDENT"),
            perm("STUDENT_UPDATE",     "UPDATE", "Update student",         "STUDENT"),
            perm("STUDENT_DELETE",     "DELETE", "Delete student",         "STUDENT"),
            // Product
            perm("PRODUCT_READ",       "READ",   "View products",          "PRODUCT"),
            perm("PRODUCT_CREATE",     "CREATE", "Create product",         "PRODUCT"),
            perm("PRODUCT_UPDATE",     "UPDATE", "Update product",         "PRODUCT"),
            perm("PRODUCT_DELETE",     "DELETE", "Delete product",         "PRODUCT"),
            // Brand
            perm("BRAND_READ",         "READ",   "View brands",            "BRAND"),
            perm("BRAND_CREATE",       "CREATE", "Create brand",           "BRAND"),
            perm("BRAND_UPDATE",       "UPDATE", "Update brand",           "BRAND"),
            perm("BRAND_DELETE",       "DELETE", "Delete brand",           "BRAND"),
            // Category
            perm("CATEGORY_READ",      "READ",   "View categories",        "CATEGORY"),
            perm("CATEGORY_CREATE",    "CREATE", "Create category",        "CATEGORY"),
            perm("CATEGORY_UPDATE",    "UPDATE", "Update category",        "CATEGORY"),
            perm("CATEGORY_DELETE",    "DELETE", "Delete category",        "CATEGORY"),
            // Client
            perm("CLIENT_READ",        "READ",   "View clients",           "CLIENT"),
            perm("CLIENT_CREATE",      "CREATE", "Create client",          "CLIENT"),
            perm("CLIENT_UPDATE",      "UPDATE", "Update client",          "CLIENT"),
            perm("CLIENT_DELETE",      "DELETE", "Delete client",          "CLIENT"),
            // Supplier
            perm("SUPPLIER_READ",      "READ",   "View suppliers",         "SUPPLIER"),
            perm("SUPPLIER_CREATE",    "CREATE", "Create supplier",        "SUPPLIER"),
            perm("SUPPLIER_UPDATE",    "UPDATE", "Update supplier",        "SUPPLIER"),
            perm("SUPPLIER_DELETE",    "DELETE", "Delete supplier",        "SUPPLIER"),
            // User management
            perm("USER_READ",          "READ",   "View users",             "USER"),
            perm("USER_CREATE",        "CREATE", "Create user",            "USER"),
            perm("USER_UPDATE",        "UPDATE", "Update user",            "USER"),
            perm("USER_DELETE",        "DELETE", "Delete user",            "USER"),
            // Role management
            perm("ROLE_READ",          "READ",   "View roles",             "ROLE"),
            perm("ROLE_CREATE",        "CREATE", "Create role",            "ROLE"),
            perm("ROLE_UPDATE",        "UPDATE", "Update role",            "ROLE"),
            perm("ROLE_DELETE",        "DELETE", "Delete role",            "ROLE"),
            // Permission management
            perm("PERMISSION_READ",    "READ",   "View permissions",       "PERMISSION"),
            perm("PERMISSION_CREATE",  "CREATE", "Create permission",      "PERMISSION"),
            perm("PERMISSION_UPDATE",  "UPDATE", "Update permission",      "PERMISSION"),
            perm("PERMISSION_DELETE",  "DELETE", "Delete permission",      "PERMISSION")
        );

        permissionRepository.saveAll(permissions);
    }

    // ─── Roles + role_permission ─────────────────────────────────────────────────

    private void seedRoles() {
        if (roleRepository.count() > 0) return;

        Map<String, Permission> all = permissionRepository.findAll().stream()
                .collect(Collectors.toMap(Permission::getName, p -> p));

        // ADMIN — full access
        Role admin = role("Administrator", "ADMIN", false,
                "Full system access", all.values());

        // MANAGER — everything except user/role/permission management
        Role manager = role("Manager", "MANAGER", false,
                "Manage business data",
                pick(all,
                        "STUDENT_READ", "STUDENT_CREATE", "STUDENT_UPDATE", "STUDENT_DELETE",
                        "PRODUCT_READ", "PRODUCT_CREATE", "PRODUCT_UPDATE", "PRODUCT_DELETE",
                        "BRAND_READ",   "BRAND_CREATE",   "BRAND_UPDATE",   "BRAND_DELETE",
                        "CATEGORY_READ","CATEGORY_CREATE","CATEGORY_UPDATE","CATEGORY_DELETE",
                        "CLIENT_READ",  "CLIENT_CREATE",  "CLIENT_UPDATE",  "CLIENT_DELETE",
                        "SUPPLIER_READ","SUPPLIER_CREATE","SUPPLIER_UPDATE","SUPPLIER_DELETE"
                ));

        // STAFF — default role, read + limited create/update on core modules
        Role staff = role("Staff", "STAFF", true,
                "Day-to-day operations",
                pick(all,
                        "STUDENT_READ", "STUDENT_CREATE", "STUDENT_UPDATE",
                        "PRODUCT_READ", "PRODUCT_CREATE", "PRODUCT_UPDATE",
                        "BRAND_READ",   "CATEGORY_READ",
                        "CLIENT_READ",  "CLIENT_CREATE",
                        "SUPPLIER_READ"
                ));

        // GUEST — read-only across all business modules
        Role guest = role("Guest", "GUEST", false,
                "Read-only access",
                pick(all,
                        "STUDENT_READ",  "PRODUCT_READ", "BRAND_READ",
                        "CATEGORY_READ", "CLIENT_READ",  "SUPPLIER_READ"
                ));

        roleRepository.saveAll(List.of(admin, manager, staff, guest));
    }

    // ─── user_role ───────────────────────────────────────────────────────────────

    private void seedUserRoles() {
        List<SysUser> users = sysUserRepository.findAll();
        if (users.stream().anyMatch(u -> !u.getRoles().isEmpty())) return;

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

    private Permission perm(String name, String action, String description, String module) {
        Permission p = new Permission();
        p.setName(name);
        p.setAction(action);
        p.setDescription(description);
        p.setModule(module);
        return p;
    }

    private Role role(String name, String code, boolean isDefault, String description,
                      Iterable<Permission> permissions) {
        Role r = new Role();
        r.setName(name);
        r.setCode(code);
        r.setDefault(isDefault);
        r.setDescription(description);
        Set<Permission> set = new HashSet<>();
        permissions.forEach(set::add);
        r.setPermissions(set);
        return r;
    }

    private Set<Permission> pick(Map<String, Permission> all, String... names) {
        return Arrays.stream(names)
                .map(all::get)
                .collect(Collectors.toSet());
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
