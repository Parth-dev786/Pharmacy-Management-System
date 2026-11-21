package com.college.pharmacy;

import java.time.LocalDate;

public interface Expirable {
    boolean isExpired(LocalDate today);
    long daysUntilExpiry(LocalDate today);
}
