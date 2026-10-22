package com.mdl.ai_excel_agent.service;

import com.mdl.ai_excel_agent.entity.User;
import com.mdl.ai_excel_agent.repository.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    private final UserRepository userRepository;
    private final AiService aiService;

    public ExcelService(UserRepository userRepository, AiService aiService) {
        this.userRepository = userRepository;
        this.aiService = aiService;
    }

    public List<User> readExcel(MultipartFile file) throws IOException {
        List<User> validUsers = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            User user = new User();

            user.setName(row.getCell(0).getStringCellValue());
            user.setEmail(row.getCell(1).getStringCellValue());
            user.setAge((int) row.getCell(2).getNumericCellValue());
            user.setDepartment(row.getCell(3).getStringCellValue());

            String result = aiService.validateUser(user);

            System.out.println("AI Result: " + result);

            if (result.toUpperCase().contains("VALID")
                    && !result.toUpperCase().contains("INVALID")) {

                validUsers.add(user);
            }
        }
        workbook.close();
        userRepository.saveAll(validUsers);
        return validUsers;
    }


}
