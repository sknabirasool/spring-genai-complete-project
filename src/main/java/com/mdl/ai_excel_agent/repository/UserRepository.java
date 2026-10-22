package com.mdl.ai_excel_agent.repository;

import com.mdl.ai_excel_agent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
