
public class StringUtils {

    public static final Locale PT_BR = new Locale("pt", "BR");
    public static final NumberFormat DECIMAL_FORMAT = DecimalFormat.getCurrencyInstance(PT_BR);
    private static final String ACCENTS = "[ÀÁáàÉÈéèÍíÓóÒòÚúçÇÃãÕõÊêÔôÄäËëÏïÖöÜü]";

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatPhoneNumber(String phone) {
        var size = length(phone);
        if (size <= 0) {
            return phone;
        }
        if (size < 11) {
            return phone.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return phone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }

    public static String formatCEP(String cep) {
        if (length(cep) < 8)
            return cep;
        return cep.replaceAll("(\\d{5})(\\d{3})", "$1-$2");
    }

    /**
     * Remove toda a acentuação da String substituindo por caracteres simples sem acento.
     * @param src Sequência de caracteres a ser convertido.
     */
    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Remove os caracteres acentuados ou substitui pela String informada.
     * @param src Sequência de caracteres a ser convertido.
     * @param replacement Sequência de caracteres que irá substituir.
     * @return Sequência de caracteres convertidos: joão -> joao ou joão -> jo_o
     */
    public static String unaccent(String src, String replacement) {
        if (isBlank(src))
            return src;
        if (isBlank(replacement))
            return AppStringUtils.unaccent(src);
        return src.replaceAll(ACCENTS, replacement);
    }

    /**
     * Prepara a String para ser utilizada como argumento na condição <b>like</b> da query SQL.
     * @param src Sequência de caracteres a ser convertido, se for nulo ou String vazia, retorna <b>null</b>.
     * @return Sequência de caracteres convertidos: João -> %JO_O%
     */
    public static String toSqlLike(String src, boolean isNative) {
        if (isNative)
            src = AppStringUtils.unaccent(src, "_");
        return isNotBlank(src) ? "%" + src.toUpperCase() + "%" : null;
    }

    public static String toSqlLike(final String src) {
        return AppStringUtils.toSqlLike(src, false);
    }

    /**
     * Converte a primeira letra de cada palavra em maiúscula.<br>
     * Ex: marks souZa -> Marks Souza || ex-jogador -> Ex-Jogador
     * @param src Sequência de caracteres a ser convertido.
     * @param joining 'String' utilizada para fazer a junção das palavras.
     * @return A String capitalizada.
     */
    public static String capitalize(final String src, String joining) {
        if (isBlank(src))
            return null;
        if (joining == null)
            joining = "";
        var words = split(src, " ");
        return Arrays.stream(words)
                .map(AppStringUtils::capitalizeWord)
                .map(StringUtils::trim)
                .collect(Collectors.joining(joining));
    }

    /**
     * Converte a primeira letra de cada palavra em maiúscula.<br>
     * Ex: marks souZa -> Marks Souza || ex-jogador -> Ex-Jogador
     * @param src Sequência de caracteres a ser convertido.
     * @return A String capitalizada.
     */
    public static String capitalize(final String src) {
        return AppStringUtils.capitalize(src, " ");
    }

    /**
     * Converte a primeira letra da palavra em maiúscula e o restante em minúsculas. <br>
     * Ex: marks souZa -> Marks Souza || ex-jogador -> Ex-Jogador
     * @param word Sequência de caracteres a ser convertido.
     * @return A String capitalizada.
     */
    public static String capitalizeWord(final String word) {
        if (isBlank(word)) return word;
        var lowerWord = lowerCase(trim(word));
        // Adiciona espaço antes e depois do hífen.
        // Manobra adicionada para capitalizar a primeira letra da palavra que venha após o hífen.
        var words = split(lowerWord, "-");
        var delimiter = words.length > 1 ? "-" : "";
        return Arrays.stream(words)
                .map(w -> length(w) > 2 ? StringUtils.capitalize(w) : w)
                .collect(Collectors.joining(delimiter));
    }
}
