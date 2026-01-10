package attendance.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public final class Money implements Comparable<Money> {

    private static final int SCALE = 2; // 소수 2자리 고정
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final BigDecimal amount; // 항상 scale=2로 정규화된 값

    private Money(BigDecimal amount) {
        this.amount = normalize(amount);
    }

    // ====== factories ======
    public static Money of(String amount) {
        return new Money(new BigDecimal(amount));
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    // ====== getter ======
    public BigDecimal amount() {
        return amount;
    }

    // ====== operations ======
    public Money plus(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money minus(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public Money times(long multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)));
    }

    public Money times(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier));
    }

    /** 0.1 = 10% */
    public Money rate(BigDecimal rate) {
        return new Money(this.amount.multiply(rate));
    }

    /** 10 = 10% (금액의 10%) */
    public Money percentOf(int percent) {
        validatePercent(percent);
        BigDecimal rate = BigDecimal.valueOf(percent).movePointLeft(2); // 10 -> 0.10
        return rate(rate);
    }

    /** 할인 "금액" */
    public Money discountAmountPercent(int percent) {
        return percentOf(percent);
    }

    /** 할인 "적용 후 금액" */
    public Money applyDiscountPercent(int percent) {
        return this.minus(discountAmountPercent(percent));
    }

    // ====== compare / equals ======
    @Override
    public int compareTo(Money other) {
        return this.amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        // scale=2로 통일되어 equals 안정적
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount.toPlainString(); // "10.50"처럼 2자리 유지
    }

    // ====== helpers ======
    private static BigDecimal normalize(BigDecimal raw) {
        Objects.requireNonNull(raw, "amount must not be null");
        return raw.setScale(SCALE, ROUNDING);
    }

    private static void validatePercent(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("[ERROR] percent must be 0..100: " + percent);
        }
    }

    // ====== demo ======
    public static void main(String[] args) {
        System.out.printf("|%10s|%n","ABC");// 오른쪽 정렬 (앞에 공백)
        System.out.printf("|%-10s|%n","ABC");// 왼쪽 정렬 (뒤에 공백)
//        System.out.printf("|%010d|%n",123);// 10칸, 빈칸을 0으로 채움 -> 0000000123
        System.out.printf("|%3d|%n",123);// 10칸, 빈칸을 0으로 채움 -> 0000000123

    }
}

