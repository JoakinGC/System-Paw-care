package com.veterinary.sistema.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.veterinary.sistema.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.veterinary.sistema.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.veterinary.sistema.domain.User.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Authority.class.getName());
            createCache(cm, com.veterinary.sistema.domain.User.class.getName() + ".authorities");
            createCache(cm, com.veterinary.sistema.domain.Terapia.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Terapia.class.getName() + ".enfermedads");
            createCache(cm, com.veterinary.sistema.domain.Factores.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Factores.class.getName() + ".enfermedads");
            createCache(cm, com.veterinary.sistema.domain.Enfermedad.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Enfermedad.class.getName() + ".razas");
            createCache(cm, com.veterinary.sistema.domain.Enfermedad.class.getName() + ".especies");
            createCache(cm, com.veterinary.sistema.domain.Enfermedad.class.getName() + ".terapias");
            createCache(cm, com.veterinary.sistema.domain.Enfermedad.class.getName() + ".factores");
            createCache(cm, com.veterinary.sistema.domain.Enfermedad.class.getName() + ".historials");
            createCache(cm, com.veterinary.sistema.domain.Especie.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Especie.class.getName() + ".mascotas");
            createCache(cm, com.veterinary.sistema.domain.Especie.class.getName() + ".enfermedads");
            createCache(cm, com.veterinary.sistema.domain.Raza.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Raza.class.getName() + ".mascotas");
            createCache(cm, com.veterinary.sistema.domain.Raza.class.getName() + ".enfermedads");
            createCache(cm, com.veterinary.sistema.domain.Mascota.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Mascota.class.getName() + ".historials");
            createCache(cm, com.veterinary.sistema.domain.Mascota.class.getName() + ".citas");
            createCache(cm, com.veterinary.sistema.domain.Dueno.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Dueno.class.getName() + ".mascotas");
            createCache(cm, com.veterinary.sistema.domain.Cita.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Cita.class.getName() + ".mascotas");
            createCache(cm, com.veterinary.sistema.domain.Veterinario.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Veterinario.class.getName() + ".historials");
            createCache(cm, com.veterinary.sistema.domain.Veterinario.class.getName() + ".citas");
            createCache(cm, com.veterinary.sistema.domain.Veterinario.class.getName() + ".estudios");
            createCache(cm, com.veterinary.sistema.domain.Estudios.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Estudios.class.getName() + ".veterinarios");
            createCache(cm, com.veterinary.sistema.domain.Estetica.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Estetica.class.getName() + ".citas");
            createCache(cm, com.veterinary.sistema.domain.CuidadoraHotel.class.getName());
            createCache(cm, com.veterinary.sistema.domain.CuidadoraHotel.class.getName() + ".citas");
            createCache(cm, com.veterinary.sistema.domain.Historial.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Historial.class.getName() + ".tratamientos");
            createCache(cm, com.veterinary.sistema.domain.Historial.class.getName() + ".medicamentos");
            createCache(cm, com.veterinary.sistema.domain.Historial.class.getName() + ".enfermedads");
            createCache(cm, com.veterinary.sistema.domain.Tratamiento.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Medicamento.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Medicamento.class.getName() + ".historials");
            createCache(cm, com.veterinary.sistema.domain.Usuario.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Usuario.class.getName() + ".compras");
            createCache(cm, com.veterinary.sistema.domain.Compra.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Compra.class.getName() + ".datelleCompras");
            createCache(cm, com.veterinary.sistema.domain.DatelleCompra.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Producto.class.getName());
            createCache(cm, com.veterinary.sistema.domain.Producto.class.getName() + ".datelleCompras");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
