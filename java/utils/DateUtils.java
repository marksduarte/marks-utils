public final class DateUtils {
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_BR = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateTimeFormatter DATE_FORMAT_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getYearsMonthsAndDays(int tDias) {
        var dt2 = LocalDate.now();
        var dt1 = dt2.minusDays(tDias);

        var years = ChronoUnit.YEARS.between(dt1, dt2);
        dt1 = dt1.plusYears(years);

        var months = ChronoUnit.MONTHS.between(dt1, dt2);
        dt1 = dt1.plusMonths(months);

        var days = ChronoUnit.DAYS.between(dt1, dt2);

        return years + " anos, " + months + " meses e " + days + " dias";
    }

    /**
     * Converte a data do tipo Date para LocalDate.
     * @param date
     * @return {@link LocalDate}
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null)
            return null;
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * @param data a ser convertida em String no formato "dd/MM/yyyy".
     * @return  {@code String} null se o paramêtro {@code date} for nulo.
     */
    public static String toDateBR(Date date) {
        return (Objects.nonNull(date)) ? SIMPLE_DATE_FORMAT_BR.format(date) : null;
    }

    public static String toDateBR(LocalDate date) {
        return (Objects.nonNull(date)) ? date.format(DATE_FORMAT_BR) : null;
    }

    public static boolean isTodayOrFuture(LocalDate date) {
        var today = LocalDate.now();
        return Objects.nonNull(date) && date.isEqual(today) && date.isAfter(today);
    }

    public static boolean isPast(LocalDate date) {
        var today = LocalDate.now();
        return Objects.nonNull(date) && date.isBefore(today);
    }

    /**
     * Converte a {@link String} caso seja uma data válida, caso contrário, devolve a mesma.
     * @param date {@link String} a ser convertida caso seja uma data válida.
     * @return O {@link Instant} formatado como {@link String}: {@code 2007-12-03T10:15:30.00Z} ou o valor original.
     */
    public static String parseDateToString(final Date date) {
        if (date == null) return null;
        try {
            return parseInstantToString(parseDateToInstant(date));
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static String parseInstantToString(final Instant dateTime) {
        return dateTime != null ? dateTime.toString() : "";
    }

    public static Date parseStringToDate(final String value) {
        var sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Instant parseDateToInstant(final Date date) {
        if (date == null) return null;
        try {
            return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toInstant();
        } catch (DateTimeException e) {
            return null;
        }
    }

    /**
     * Compara a data com a data atual ignorando a hora.
     * @param date Data a ser comparada
     * @return true se a data for igual ou anterior a data atual.
     */
    public static boolean isTodayOrPast(Date date) {
        Date today = getTodayZeroTime();
        date = getNewInstanceWithTimeZero(date);
        return Objects.nonNull(date) && (!date.after(today));
    }

    public static boolean isPast(Date date) {
        Date today = getTodayZeroTime();
        return Objects.nonNull(date) && date.before(today);
    }

    public static boolean isTodayOrFuture(Date date) {
        Date today = getTodayZeroTime();
        return Objects.isNull(date) || (!date.before(today));
    }

    public static boolean isFuture(Date date) {
        Date today = getTodayZeroTime();
        return Objects.isNull(date) || date.after(today);
    }
}
