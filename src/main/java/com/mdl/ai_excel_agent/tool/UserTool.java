package com.mdl.ai_excel_agent.tool;

import com.mdl.ai_excel_agent.entity.User;
import com.mdl.ai_excel_agent.repository.UserRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class UserTool {

    private final UserRepository userRepository;

    public UserTool(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Tool(description = "Save a user into the MySQL database")
    public String saveUser(
            String name,
            String email,
            Integer age,
            String department) {

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setDepartment(department);

        userRepository.save(user);

        return "User saved successfully: " + name;
    }

}
