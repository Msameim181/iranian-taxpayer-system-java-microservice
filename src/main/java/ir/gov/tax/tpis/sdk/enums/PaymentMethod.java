package ir.gov.tax.tpis.sdk.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PaymentMethod {
        /**
         * چک
         */
        CHEQUE,
        /**
         * تهاتر
         */
        BARTER,
        /**
         * وجه نقد
         */
        CASH,
        /**
         * pos
         */
        POS,
        /**
         * درگاه پرداخت اینترنتی
         */
        INTERNET,
        /**
         * کارت به کارت
         */
        CARD,
        /**
         * انتقال به حساب
         */
        TRANSFER,
        /**
         * سایر
         */
        OTHER;


    }