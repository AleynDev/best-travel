package com.aleyn.best_travel.infrastructure.helpers;

import com.aleyn.best_travel.util.exceptions.ForbiddenCustomerException;
import org.springframework.stereotype.Component;

@Component
public class BlackListHelper {

    public void isInBlackListCustomer(String customerId) {
        if (customerId.equals("GOTW771012HMRGR087")) {
            throw new ForbiddenCustomerException();
        }
    }

}