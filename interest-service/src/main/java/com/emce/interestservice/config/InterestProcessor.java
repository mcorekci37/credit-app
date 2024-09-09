package com.emce.interestservice.config;

import com.emce.commons.entity.Installment;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.Period;

public class InterestProcessor implements ItemProcessor<Installment, Installment> {
    @Value("${application.param.interest}")
    public Double interest;


    @Override
    public Installment process(Installment installment) throws Exception {
        long latency = findLatency(installment.getDeadline(), LocalDate.now());

        double dept = installment.getDept();

        final double interestAmount =  (latency * (interest / 100 ) * dept ) / 360;
        System.out.println(interestAmount);
        installment.setDept(dept + interestAmount);
        installment.setInterest(installment.getInterest() + interestAmount);

        return installment;
    }

    public static long findLatency(LocalDate from, LocalDate to) {
        Period period = Period.between(from, to);
        final int DAYS_IN_YEAR = 365;
        final int DAYS_IN_MONTH = 30;

        long totalDays = period.getYears() * DAYS_IN_YEAR + period.getMonths() * DAYS_IN_MONTH + period.getDays();
        return totalDays;
    }
}
