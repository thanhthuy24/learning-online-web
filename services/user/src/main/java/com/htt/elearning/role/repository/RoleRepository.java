package com.htt.elearning.role.repository;

import com.htt.elearning.role.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleById(Long roleId);
    Role findByName(String name);
}
