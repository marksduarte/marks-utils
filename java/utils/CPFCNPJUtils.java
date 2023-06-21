
/**
 * classe para formatação e validação de CPF e CNPJ
 * @author Marks Duarte
 */
public class CPFCNPJUtil {

    private static final String VALUE_CANNOT_BE_NULL = "O valor não pode ser nulo";

    private static final String VALUE_CANNOT_BE_NULL_OR_EMPTY = "O valor não pode ser nulo ou vazio";

    private static final String SIZE_OF_VALUE_CANNOT_BE_BIGGER_THEN_14 = "O tamanho não pode ser maior que 14 caracteres";

    private static final String VALUE_IS_NOT_A_VALID_CPF_OR_CPNJ = "Não é um CPF ou CPNJ válido";

    private static final DecimalFormat CNPJ_NFORMAT = new DecimalFormat("00000000000000");

    private static final DecimalFormat CPF_NFORMAT = new DecimalFormat("00000000000");

    private static final String REGEX_ONLY_NUMBERS = "[^\\d]+";

    private static final String HIDE_CHAR = "*";

    private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

    private static final int[] pesoCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

    private CPFCNPJUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Formata o valor para CPF 000.000.000-00 ou CNPJ 00.000.000/0000-00 caso o
     * mesmo seja um CPF ou CNPJ válido
     *
     * @param value
     *            [string] representa um CPF ou CNPJ
     *
     * @return CPF ou CNPJ formatado
     */
    public static String formatCPForCPNJ(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_OR_EMPTY);
        }
        return formatCPForCPNJ(Long.parseLong(value.replaceAll(REGEX_ONLY_NUMBERS, "")),true);
    }

    /**
     * Formata valor para CPF 000.000.000-00 ou CNPJ 00.000.000/0000-00
     *
     * @param value
     *            [string] representa um CPF ou CNPJ
     *
     * @param check
     *            [boolean] se true verifica se é um CPF ou CNPJ valido, se
     *            false apenas realiza a formatação
     *
     * @return CPF ou CNPJ formatado
     */
    public static String formatCPForCPNJ(String value, boolean check) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_OR_EMPTY);
        }
        return formatCPForCPNJ(Long.parseLong(value.replaceAll(REGEX_ONLY_NUMBERS, "")), check);
    }

    /**
     * Formata valor para CPF 000.000.000-00 ou CNPJ 00.000.000/0000-00 caso o
     * mesmo seja um CPF ou CNPJ válido
     *
     * @param value
     *            [long] representa um CPF ou CNPJ
     *
     * @return CPF ou CNPJ formatado
     */
    public static String formatCPForCPNJ(Long value) {
        return formatCPForCPNJ(value, true);
    }

    /**
     * Formata valor para CPF 000.000.000-00 ou CNPJ 00.000.000/0000-00
     *
     * @param value
     *            [long] representa um CPF ou CNPJ
     *
     * @param check
     *            [boolean] se true verifica se é um CPF ou CNPJ valido, se
     *            false apenas realiza a formatação
     *
     * @return CPF ou CNPJ formatado
     */
    public static String formatCPForCPNJ(Long value, boolean check) {
        if (value == null) {
            throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL);
        }

        final int valueSize = value.toString().length();
        if (valueSize > 14) {
            throw new IllegalArgumentException(
                    SIZE_OF_VALUE_CANNOT_BE_BIGGER_THEN_14);
        }

        if (check && !isCPForCPNJ(value)) {
            throw new IllegalArgumentException(VALUE_IS_NOT_A_VALID_CPF_OR_CPNJ);
        }

        boolean isCPF = valueSize < 12;
        DecimalFormat formatDecimal = isCPF ? CPF_NFORMAT : CNPJ_NFORMAT;

        final String stringNumber = formatDecimal.format(value);

        return isCPF ? stringNumber.replaceAll(
                "(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4")
                : stringNumber.replaceAll(
                "(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                "$1.$2.$3/$4-$5");
    }

    /**
     * Verifica se um valor corresponde à um CPF ou CNPJ válido.
     *
     * @param value
     *            [string] valor à ser testado
     *
     * @return [boolean] true caso seja um valor válido, false caso contrário
     */
    public static boolean isCPForCPNJ(String value, boolean checkNullValue) {

        if((value == null || value.isEmpty()) && !checkNullValue) return true;

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_OR_EMPTY);
        }
        return isCPForCPNJ(Long.parseLong(value.replaceAll(REGEX_ONLY_NUMBERS, "")));
    }

    /**
     * Verifica se um valor corresponde à um CPF ou CNPJ válido.
     *
     * @param value
     *            [long] valor à ser testado
     *
     * @return [boolean] true caso seja um valor válido, false caso contrário
     */
    public static boolean isCPForCPNJ(Long value) {

        final int valueSize = value.toString().length();
        if (valueSize > 14) {
            return false;
        }

        boolean isCPF = valueSize < 12;

        return isCPF ? isCPF(value) : isCNPJ(value);
    }

    /**
     * Remove os caracteres que não sejam digítos.
     *
     * @param value [String] valor a ser formatado.
     * @return [String] Somente os dígitos do CPF ou CNPJ informado.
     */
    public static String formatCPForCNPJToOnlyDigits(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_OR_EMPTY);
        }
        String stringNumber = formatCPForCPNJ(value, false);
        return stringNumber.replaceAll(REGEX_ONLY_NUMBERS, "").trim();
    }

    /**
     * Remove os caracteres que não sejam digitos.
     *
     * @param value [String] valor a ser formatado.
     * @param check [boolean] se true verifica se é um CPF ou CNPJ valido, se false apenas realiza a formatação
     * @return [String] Somente os dígitos do CPF ou CNPJ informado.
     */
    public static String formatCPForCNPJToOnlyDigits(String value, boolean check) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_OR_EMPTY);
        }
        String stringNumber = formatCPForCPNJ(value, check);
        return stringNumber.replaceAll(REGEX_ONLY_NUMBERS, "").trim();
    }

    /**
     * Formata o CPF e esconde alguns digitos.
     *
     * @param value {@code String} valor a ser formatado.
     * @return CPF com o formato *.000.*-00
     */
    public static String formatAndHideCPFDigits(String value) {
        if (value == null || value.isEmpty()) {
            return HIDE_CHAR;
        }
        var stringNumber = formatCPForCNPJToOnlyDigits(value, false);
        return stringNumber.replaceAll(
                "(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "*.$2.*-$4");
    }

    /**
     * Verifica se o valor corresponde a um CPF.
     *
     * @param value
     *            [long] valor à ser testado
     *
     * @return [boolean] true caso seja um valor válido, false caso contrário
     */
    private static boolean isCPF(Long value) {

        String cpf = CPF_NFORMAT.format(value);

        int firstPart = calcDigit(cpf.substring(0, 9), pesoCPF);
        int lastPart = calcDigit(cpf.substring(0, 9) + firstPart, pesoCPF);

        return cpf.substring(9).equals(
                String.format("%d%d", firstPart, lastPart));
    }

    /**
     * Verifica se um valor corresponde à um CNPJ.
     *
     * @param value
     *            [long] valor à ser testado
     *
     * @return [boolean] true caso seja um valor válido, false caso contrário
     */
    private static boolean isCNPJ(Long value) {

        String cnpj = CNPJ_NFORMAT.format(value);

        Integer firstPart = calcDigit(cnpj.substring(0, 12), pesoCNPJ);
        Integer lastPart = calcDigit(cnpj.substring(0, 12) + firstPart,
                pesoCNPJ);

        return cnpj.substring(12).equals(
                String.format("%d%d", firstPart, lastPart));
    }

    /**
     * Calcula digito verificador para CPF or CPNJ
     *
     * @param stringBase
     *            [string] base do calculo do digito verificador
     *
     * @param weight
     *            array[int] representa os peso de cada caracter que compõe um
     *            CPF ou CNPJ
     *
     * @return [int] digito verificador
     */
    private static int calcDigit(String stringBase, int[] weight) {
        int sum = 0;
        int digit;
        for (int index = stringBase.length() - 1; index >= 0; index--) {
            digit = Integer.parseInt(stringBase.substring(index, index + 1));
            sum += digit * weight[weight.length - stringBase.length() + index];
        }
        sum = 11 - sum % 11;
        return sum > 9 ? 0 : sum;
    }
}
