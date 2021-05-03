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

        application.get("/login/success", context -> {
            context.render("templates/successful_login.vm");
        });


    }
}
