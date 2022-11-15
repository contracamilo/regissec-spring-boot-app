package com.example.regisecure.Controllers;

import com.example.regisecure.Models.PermissionModel;
import com.example.regisecure.Models.PermissionRoleModel;
import com.example.regisecure.Models.RoleModel;
import com.example.regisecure.Repositories.PermissionRepository;
import com.example.regisecure.Repositories.PermissionRoleRepository;
import com.example.regisecure.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permission-rol")
public class PermissionRoleController {
    @Autowired
    private PermissionRoleRepository permissionRoleRepo;

    @Autowired
    private PermissionRepository permissionRepo;

    @Autowired
    private RoleRepository roleRepo;

    @GetMapping
    public List<PermissionRoleModel> index(){
        return this.permissionRoleRepo.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("rol/{id_rol}/permission/{id_permission}")
    public PermissionRoleModel create(@PathVariable String id_role,@PathVariable String id_permission){
        PermissionRoleModel newRolePermission = new PermissionRoleModel();

        RoleModel role = this.roleRepo.findById(id_role).get();
        PermissionModel permission = this.permissionRepo.findById(id_permission).get();

        if (role != null && permission != null){
            newRolePermission.setPermission(permission);
            newRolePermission.setRole(role);
            return this.permissionRoleRepo.save(newRolePermission);
        }else{
            return null;
        }
    }

    @GetMapping("{id}")
    public PermissionRoleModel show(@PathVariable String id){
        PermissionRoleModel currentRolePermission = this.permissionRoleRepo.findById(id).orElse(null);
        return currentRolePermission;
    }

    /**
     * Modificación Rol y Permiso
     * @param id
     * @param id_role
     * @param id_permission
     * @return
     */
    @PutMapping("{id}/rol/{id_rol}/permission/{id_permission}")
    public PermissionRoleModel update(@PathVariable String id, @PathVariable String id_role,@PathVariable String id_permission){
        PermissionRoleModel currentRolePermission = this.permissionRoleRepo.findById(id).orElse(null);
        RoleModel role = this.roleRepo.findById(id_role).get();
        PermissionModel permission = this.permissionRepo.findById(id_permission).get();

        if(currentRolePermission != null && permission != null && role != null){
            currentRolePermission.setPermission(permission);
            currentRolePermission.setRole(role);
            return this.permissionRoleRepo.save(currentRolePermission);
        }else{
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        PermissionRoleModel currentPermissionRole = this.permissionRoleRepo.findById(id).orElse(null);

        if (currentPermissionRole != null){
            this.permissionRoleRepo.delete(currentPermissionRole);
        }
    }

    @GetMapping("validate-permission/rol/{id_role}")
    public PermissionRoleModel getPermiso(@PathVariable String id_role, @RequestBody PermissionModel permissionData){
        PermissionModel permission = this.permissionRepo.getPermission(permissionData.getUrl(), permissionData.getMethod());
        RoleModel role = this.roleRepo.findById(id_role).get();

        if (permission != null && role != null){
            return this.permissionRoleRepo.getPermissionRole(role.get_id(),
                    permission.get_id());
        }else{
            return null;
        }
    }
}
