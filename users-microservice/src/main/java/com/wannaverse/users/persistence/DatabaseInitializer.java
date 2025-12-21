package com.wannaverse.users.persistence;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        createPgTrgmExtension();
        createSearchVectorTriggerFunction();
        createSearchVectorTrigger();
        createSearchVectorIndex();
    }

    private void createPgTrgmExtension() {
        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS pg_trgm;");
    }

    private void createSearchVectorTriggerFunction() {
        String sql =
                """
                CREATE OR REPLACE FUNCTION users_t_search_vector_trigger() RETURNS trigger AS $$
                BEGIN
                  NEW.search_vector :=
                    setweight(to_tsvector('english', coalesce(NEW.email_address, '')), 'A') ||
                    setweight(to_tsvector('english', coalesce(NEW.first_name, '')), 'B') ||
                    setweight(to_tsvector('english', coalesce(NEW.last_name, '')), 'B') ||
                    setweight(to_tsvector('english', coalesce(NEW.display_name, '')), 'C');
                  RETURN NEW;
                END
                $$ LANGUAGE plpgsql;
                """;
        jdbcTemplate.execute(sql);
    }

    private void createSearchVectorTrigger() {
        String sql =
                """
                DO $$
                BEGIN
                  IF NOT EXISTS (
                    SELECT 1 FROM pg_trigger WHERE tgname = 'users_tsvector_update'
                  ) THEN
                    CREATE TRIGGER users_tsvector_update
                    BEFORE INSERT OR UPDATE ON users_t
                    FOR EACH ROW EXECUTE FUNCTION users_t_search_vector_trigger();
                  END IF;
                END
                $$;
                """;
        jdbcTemplate.execute(sql);
    }

    private void createSearchVectorIndex() {
        jdbcTemplate.execute(
                """
                    CREATE INDEX IF NOT EXISTS idx_users_search_vector
                    ON users_t USING GIN (search_vector);
                """);
    }
}
