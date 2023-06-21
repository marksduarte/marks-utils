/**
 * Classe utilitária para utilizar os beans inicializados no contexto do Spring sem precisar da Injeção de Dependência.
 *
 * @author Marks Duarte
 * @version 1.0
 */
@Component
public final class BeanUtils {
    // adicionado como 'static' para chamar direto na classe.
    private static ApplicationContext context;

    private BeanUtils(ApplicationContext context) {
        BeanUtils.context = context;
    }

    /**
     * Recupera o Bean instanciado no contexto.
     * @param clazz Classe a ser localizada.
     * @return Objeto bean instanciado no contexto.
     * @param <T> Tipo do objeto da classe.
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        Assert.state(context != null, "O contexto do Spring ainda não foi inicializado!");
        return context.getBean(clazz);
    }

    /**
     * Verifica se a propriedade existe no arquivo de 'applicaton.properties' existe e está ativa: true ou on.
     * @param key Chave tipo: spring.data.key
     * @return true ou false
     */
    public static boolean isPropertyActivated(String key) {
        Assert.state(context != null, "O contexto do Spring ainda não foi inicializado!");
        var value = context.getEnvironment().getProperty(key);
        return StringUtils.containsAnyIgnoreCase(value, "true", "on");
    }

    /**
     * Copia as propriedades usando reflection incluindo valores nulos.
     * @param src {@Link Object} Objeto de origem
     * @param dest {@Link Object} Objeto de destino
     */
    public static void copyProperties(Object src, Object dest) {
        copyProperties(src, dest, true);
    }

    /**
     * Copia as propriedades usando reflection.
     * @param src {@Link Object} Objeto de origem
     * @param dest {@Link Object} Objeto de destino
     * @param copyNull Copia ou ignora valores nulos
     */
    public static void copyProperties(Object src, Object dest, boolean copyNull) {
        Arrays.stream(src.getClass().getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()) && m.getName().startsWith("get"))
                .forEach(m -> {
                    try {
                        var returnType = m.getReturnType();
                        var mName = m.getName().replace("get", "set");
                        var setter = dest.getClass().getMethod(mName, returnType);
                        var value = m.invoke(src);
                        if (value != null || copyNull) {
                            setter.invoke(dest, value);
                        }
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static String normalizeMethodName(String mName) {
        if (mName == null)
            return null;
        return mName.replaceAll("^[get|set]", "");
    }
}
