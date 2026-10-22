package com.mdl.ai_excel_agent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class InterviewTools {

    @Tool(description = """
            Calculate simple interest and total amount.
            Use this tool when the user asks to calculate simple interest.
            The annual interest rate must be expressed as a percentage.
            The time period must be expressed in years.
            """)
    public String calculateSimpleInterest(
            @ToolParam(description = "Principal amount, greater than zero")
            double principal,

            @ToolParam(description = "Annual interest rate in percent, zero or greater")
            double annualRatePercent,

            @ToolParam(description = "Time period in years, greater than zero")
            double timeYears
    ){

        if (!Double.isFinite(principal)
                || !Double.isFinite(annualRatePercent)
                || !Double.isFinite(timeYears)) {
            return "Calculation failed: inputs must be finite numbers.";
        }

        if (principal <= 0) {
            return "Calculation failed: principal must be greater than zero.";
        }

        if (annualRatePercent < 0) {
            return "Calculation failed: interest rate cannot be negative.";
        }

        if (timeYears <= 0) {
            return "Calculation failed: time must be greater than zero.";
        }

        double interest =
                principal * annualRatePercent * timeYears / 100.0;

        double totalAmount = principal + interest;

        return String.format(
                Locale.ROOT,
                """
                Calculation successful.
                Principal: %.2f
                Annual interest rate: %.2f%%
                Time: %.2f years
                Simple interest: %.2f
                Total amount: %.2f
                """,
                principal,
                annualRatePercent,
                timeYears,
                interest,
                totalAmount
        );
    }

}
