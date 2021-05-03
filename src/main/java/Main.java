import io.javalin.Javalin;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(final String ... args){
        final Javalin application = Javalin.create().start(7000);

        application.get("/", context -> {
            context.redirect("/login");
        });

        application.get("/login", context -> {
            context.render("templates/login.vm");
        });

        application.get("/login/home", context -> {
            final Map<String,Object> templateData = new HashMap<>();
            templateData.put("username", "TEMP_USERNAME");
            context.render("templates/logged_in_template.vm");
        });


    }
}
