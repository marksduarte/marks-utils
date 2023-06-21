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

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        Assert.state(context != null, "O contexto do Spring ainda não foi inicializado!");
        return context.getBean(clazz);
    }

    public static boolean isPropertyActivated(String key) {
        Assert.state(context != null, "O contexto do Spring ainda não foi inicializado!");
        var value = context.getEnvironment().getProperty(key);
        return StringUtils.containsAnyIgnoreCase(value, "true", "on");
    }
}
