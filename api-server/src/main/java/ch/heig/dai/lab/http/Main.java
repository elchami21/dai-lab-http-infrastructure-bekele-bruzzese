package ch.heig.dai.lab.http;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private static final ConcurrentHashMap<String, Quote> quotes = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
        }).start(7000);

        // CRUD endpoints
        app.get("/api/quotes", Main::getAllQuotes);
        app.get("/api/quotes/{id}", Main::getQuote);
        app.post("/api/quotes", Main::createQuote);
        app.put("/api/quotes/{id}", Main::updateQuote);
        app.delete("/api/quotes/{id}", Main::deleteQuote);

        // Add some sample quotes
        quotes.put("1", new Quote("1", "The best way to predict the future is to create it.", "Abraham Lincoln"));
        quotes.put("2", new Quote("2", "Innovation distinguishes between a leader and a follower.", "Steve Jobs"));
    }

    private static void getAllQuotes(Context ctx) {
        ctx.json(new ArrayList<>(quotes.values()));
    }

    private static void getQuote(Context ctx) {
        String id = ctx.pathParam("id");
        Quote quote = quotes.get(id);
        if (quote != null) {
            ctx.json(quote);
        } else {
            ctx.status(404).result("Quote not found");
        }
    }

    private static void createQuote(Context ctx) {
        Quote quote = ctx.bodyAsClass(Quote.class);
        if (quotes.containsKey(quote.id())) {
            ctx.status(400).result("Quote with this ID already exists");
            return;
        }
        quotes.put(quote.id(), quote);
        ctx.status(201).json(quote);
    }

    private static void updateQuote(Context ctx) {
        String id = ctx.pathParam("id");
        Quote quote = ctx.bodyAsClass(Quote.class);
        if (!quotes.containsKey(id)) {
            ctx.status(404).result("Quote not found");
            return;
        }
        quotes.put(id, quote);
        ctx.json(quote);
    }

    private static void deleteQuote(Context ctx) {
        String id = ctx.pathParam("id");
        Quote removed = quotes.remove(id);
        if (removed != null) {
            ctx.status(204);
        } else {
            ctx.status(404).result("Quote not found");
        }
    }
}

record Quote(String id, String text, String author) {}